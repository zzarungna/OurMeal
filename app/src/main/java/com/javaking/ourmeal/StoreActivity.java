package com.javaking.ourmeal;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;


import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.javaking.ourmeal.model.Food_menu;
import com.javaking.ourmeal.model.Star_bulletin;
import com.javaking.ourmeal.model.Store;


import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StoreActivity extends AppCompatActivity {
    private static String LOG_TAG = "MAINACTIVITY";
    HashMap<String, Object> map = new HashMap<>();

    //private static final String IP = "http://172.30.1.37:8080";//학원
    private static final String IP = "http://192.168.0.17:8080";//학원

    //로그인한 회원 아이디는
    String member_id = CookieManager.getInstance().getCookie("login_id");

    RelativeLayout mapView;
    TextView store_title;
    TextView score_avg;
    Store store;
    LinearLayout img_layout;

    ImageButton btn_review;
    ImageButton btn_menu;

    TextView store_info;
    TextView store_address;
    TextView str_reviewCount;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Button btn_more;

    Boolean profile_check;
    View dialogView;
    View menuDialogView;
    ImageButton image_btn;
    String image_path = null;
    String image_name = null;

    Button main_btn;

    //다른데서 넘어온 스토어 코드값
    String getStorecode = null;
    TextView menuTitle;

    WebView webView;

    public RequestManager mGlideRequestManager;

    final String sc = "";

    final int RC = 0;
    public int num = RC;
    final ArrayList <Star_bulletin> list = new ArrayList<>();

    public void initRefs() {
        store_title= findViewById(R.id.store_title);
        score_avg = findViewById(R.id.score_avg);
        img_layout = findViewById(R.id.img_layout);
        btn_review = findViewById(R.id.btn_review);
        btn_menu = findViewById(R.id.btn_menu);
        store_info = findViewById(R.id.store_info);
        store_address = findViewById(R.id.store_address);
        str_reviewCount = (TextView)findViewById(R.id.str_reviewCount);
        btn_more = findViewById(R.id.btn_more);
        profile_check = false;
        menuTitle = findViewById(R.id.menuTitle);

        //상단 버튼
        main_btn = findViewById(R.id.main_btn);
        webView = (WebView)findViewById(R.id.web_view);
    }

    public int more_button(){
        num = num+5;
        return num;
    }

    //사진 파일 첨부후..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Log.d("아이유", "테스트");

            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);

                final Uri uri = data.getData();
                image_path = getFilesDir().toString();
                image_name = getName(uri);

                // 이미지 표시
                image_btn.setImageBitmap(Bitmap.createScaledBitmap(img,730, 710, false));
                profile_check = true;
                in.close();

                //upload before file delete
                File file_delete = new File(image_path);
                if(file_delete.exists()){
                    if(file_delete.isDirectory()){
                        File[] files = file_delete.listFiles();

                        for(int i =0; i<files.length; i++){
                            if(files[i].delete()){
                                Log.d("기존 파일 삭제", files[i].getName());
                            }else{
                                Log.d("기존 파일 삭제 실패", files[i].getName());
                            }
                        }
                    }
                }else{
                    Log.d("기존 파일","삭제할게 없음.");
                }

                //데이타 업로드시 data 폴더에 이미지 파일 저장하기.
                File file = new File(image_name);
                FileOutputStream fos = openFileOutput(image_name, 0);

                //안드로이드 로컬 데이터에 사진이미지 저장
                img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setEvents() {

        //홈버튼 클릭
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntet = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(mainIntet);
            }
        });


        //메뉴정보 확인 다이얼로그 시작.
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialogView = (View)View.inflate(StoreActivity.this, R.layout.menu_information, null);
                AlertDialog.Builder menudig = new AlertDialog.Builder(StoreActivity.this);

                menudig.setView(menuDialogView);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL endPoint = new URL(IP + "/OurMeal/m/menuInfo");

                            HttpURLConnection myConnection =
                                    (HttpURLConnection) endPoint.openConnection();
                            myConnection.setRequestMethod("POST");

                            //final String id = "user1";
                            //final String store_code = "S19010800001";

                            final String requestParam = String.format("id=%s&store_code=%s", member_id,getStorecode);

                            myConnection.setDoOutput(true);
                            myConnection.getOutputStream().write(requestParam.getBytes());

                            if (myConnection.getResponseCode() == 200) {
                                // Success
                                BufferedReader in =
                                        new BufferedReader(
                                                new InputStreamReader(
                                                        myConnection.getInputStream()));
                                StringBuffer buffer = new StringBuffer();
                                String temp = null;
                                while((temp = in.readLine())!=null)
                                    buffer.append(temp);

                                Gson gson = new Gson();
                                final List<Food_menu> food_menulist = gson.fromJson(buffer.toString(), new TypeToken<List<Food_menu>>(){}.getType());

                                TextView menu_name = menuDialogView.findViewById(R.id.menu_name);
                                TextView menu_infor = menuDialogView.findViewById(R.id.menu_infor);
                                TextView menu_price = menuDialogView.findViewById(R.id.menu_price);
                                TextView menu_allergy = menuDialogView.findViewById(R.id.menu_allergy);
                                TextView menu_kcal = menuDialogView.findViewById(R.id.menu_kcal);

                                if(food_menulist==null){
                                    menu_name.setText("등록된 메뉴 정보가 없습니다.");
                                }else{
                                    menu_name.setText("메뉴 : " + food_menulist.get(0).getFm_name());
                                    menu_infor.setText("설명 : " + food_menulist.get(0).getFm_info());
                                    menu_price.setText("가격 : " +food_menulist.get(0).getFm_price()+"원");

                                    if(food_menulist.get(0).getFm_allergy().equals("null ")){
                                        menu_allergy.setText("알레르기 정보가 없습니다.");
                                    }else{
                                        menu_allergy.setText("알레르기 : " + food_menulist.get(0).getFm_allergy());
                                    }

                                    menu_kcal.setText("열량 : " + food_menulist.get(0).getFm_kcal()+"㎉");
                                }




                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ImageView menu_image = menuDialogView.findViewById(R.id.menu_image);
                                        Glide.with(getApplicationContext()).load(IP+ "/OurMeal"+food_menulist.get(0).getFm_image()).into(menu_image);

                                        if(food_menulist.size()!=0){
                                            LinearLayout mainlayout = (LinearLayout)menuDialogView.findViewById(R.id.menu_layout);

                                            for(int i = 1; i<food_menulist.size(); i++){
                                                LinearLayout dynamicView = (LinearLayout)View.inflate(StoreActivity.this, R.layout.menu_information, null);

                                                ImageView sub_menu_image = dynamicView.findViewById(R.id.menu_image);
                                                TextView sub_menu_name = dynamicView.findViewById(R.id.menu_name);
                                                TextView sub_menu_infor = dynamicView.findViewById(R.id.menu_infor);
                                                TextView sub_menu_price = dynamicView.findViewById(R.id.menu_price);
                                                TextView sub_menu_allergy = dynamicView.findViewById(R.id.menu_allergy);
                                                TextView sub_menu_kcal = dynamicView.findViewById(R.id.menu_kcal);
                                                TextView sub_menuTitle = dynamicView.findViewById(R.id.menuTitle);

                                                sub_menuTitle.setVisibility(View.INVISIBLE);
                                                sub_menu_name.setText("메뉴 : "+food_menulist.get(i).getFm_name());
                                                sub_menu_infor.setText("설명 : " +food_menulist.get(i).getFm_info());
                                                sub_menu_price.setText("가격 : " + food_menulist.get(i).getFm_price()+"원");
                                                if(food_menulist.get(i).getFm_allergy().equals("null ")){
                                                    sub_menu_allergy.setText("알레르기 정보가 없습니다.");
                                                }else{
                                                    sub_menu_allergy.setText("알레르기 : " + food_menulist.get(i).getFm_allergy());
                                                }

                                                sub_menu_kcal.setText("열량 : " + food_menulist.get(i).getFm_kcal()+"㎉");
                                                Glide.with(getApplicationContext()).load(IP+ "/OurMeal"+food_menulist.get(i).getFm_image()).into(sub_menu_image);

                                                mainlayout.addView(dynamicView);
                                            }
                                        }
                                    }
                                });
                                in.close();
                            } else {
                                // Error
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패1", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            Log.d(LOG_TAG, e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(), "등록된 메뉴 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                    TextView menu_name = menuDialogView.findViewById(R.id.menu_name);
                                    menu_name.setText("등록된 메뉴 정보가 없습니다.");
                                }
                            });
                        }
                    }
                });

                menudig.setNegativeButton("확인", null);
                menudig.show();
            }
        });

        //리뷰 등록 시작 다이얼로그 > 파일 선택 작성 정보 인서트 및 파일 업로드 완료. 프로필 이미지 뜨는거 모두 확인함.
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(member_id==null){
                    Toast.makeText(getApplicationContext(),"로그인후 리뷰 작성이 가능 합니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogView = (View)View.inflate(StoreActivity.this, R.layout.review_write, null);
                AlertDialog.Builder dig = new AlertDialog.Builder(StoreActivity.this);
                dig.setView(dialogView);

                image_btn = dialogView.findViewById(R.id.imageButton);
                image_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent data = new Intent();
                        data.setType("image/*");
                        data.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(data, 1);
                    }
                });

                dig.setPositiveButton("리뷰 등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!profile_check){
                            Toast.makeText(getApplicationContext(), "이미지를 첨부해 주세요!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final EditText review = (EditText)dialogView.findViewById(R.id.review_text);
                        final RatingBar rb = (RatingBar)dialogView.findViewById(R.id.start_score);

                        if(review.getText().toString().trim().length() == 0){
                            Toast.makeText(getApplicationContext(), "리뷰 내용을 작성해 주세요!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //String msg = "입력된 리뷰 : " + review.getText();
                        if(profile_check){
                            //웹서버에 리뷰 등록
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String attachmentFileName = null;

                                        if(profile_check) {
                                            //파일 이름
                                            attachmentFileName  = image_name;
                                            Log.d("CheckImage", Integer.toString(image_path.length()));
                                        }
                                        Log.d("CheckImage 경로", image_path);
                                        Log.d("CheckImage 이름", attachmentFileName);
                                        String crlf = "\r\n";
                                        String twoHyphens = "--";
                                        String boundary = "*****";

                                        HttpURLConnection httpUrlConnection = null;
                                        URL url = new URL(IP + "/OurMeal/store/write_review");
                                        httpUrlConnection = (HttpURLConnection) url.openConnection();
                                        httpUrlConnection.setUseCaches(false);
                                        httpUrlConnection.setDoOutput(true);
                                        httpUrlConnection.setDoInput(true);
                                        httpUrlConnection.setRequestMethod("POST");

                                        //final String id = "user";
                                        //final String store_code = "S19010800001";
                                        final String content = review.getText().toString();
                                        final String sb_number = String.valueOf(rb.getRating());

                                        // key, value 형태 // Content-Type: multipart/form-data; boundary=*****
                                        httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                                        DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

                                        if(profile_check) {
                                            request.writeBytes(twoHyphens + boundary + crlf);
                                            request.writeBytes("Content-Disposition: form-data; name= \"file\" ;filename=\"" + attachmentFileName + "\"" + crlf);
                                            request.writeBytes(crlf);

                                            FileInputStream fStream = new FileInputStream(image_path+"/" + image_name);
                                            byte buffer5[] = new byte[1024];
                                            int length = -1;
                                            while ((length = fStream.read(buffer5)) != -1) {
                                                request.write(buffer5, 0, length);
                                            }
                                            request.writeBytes(crlf);
                                        }

                                        //18 좉같다 진짜 이거는...아오
                                        request.writeBytes(twoHyphens + boundary + crlf);
                                        request.writeBytes("Content-Disposition: form-data; name=\"id\"" + crlf);
                                        request.writeBytes(crlf);
                                        request.writeBytes(member_id + crlf);
                                        //이거는 진짜 심하다...

                                        //18 좉같다 진짜 이거는...아오
                                        request.writeBytes(twoHyphens + boundary + crlf);
                                        request.writeBytes("Content-Disposition: form-data; name=\"store_code\"" + crlf);
                                        request.writeBytes(crlf);
                                        request.writeBytes(getStorecode + crlf);
                                        //이거는 진짜 심하다...

                                        //18 좉같다 진짜 이거는...아오
                                        request.writeBytes(twoHyphens + boundary + crlf);
                                        request.writeBytes("Content-Disposition: form-data; name=\"content\"" + crlf);
                                        request.writeBytes(crlf);
                                        request.writeUTF(content);
                                        request.writeBytes(crlf);
                                        //이거는 진짜 심하다...

                                        //18 좉같다 진짜 이거는...아오
                                        request.writeBytes(twoHyphens + boundary + crlf);
                                        request.writeBytes("Content-Disposition: form-data; name=\"sb_number\"" + crlf);
                                        request.writeBytes(crlf);
                                        request.writeBytes(sb_number + crlf);
                                        //이거는 진짜 심하다...


                                        // 마지막에 닫어줘야한다 // 끝태그
                                        request.writeBytes(twoHyphens + boundary);

                                        if (httpUrlConnection.getResponseCode() == 200) {
                                            // Success
                                            BufferedReader in =
                                                    new BufferedReader(
                                                            new InputStreamReader(
                                                                    httpUrlConnection.getInputStream()));
                                            StringBuffer buffer = new StringBuffer();
                                            String temp = null;
                                            while((temp = in.readLine())!=null)
                                                buffer.append(temp);

                                            Gson gson = new Gson();
                                            final String result = gson.fromJson(buffer.toString(), String.class);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(result.equals("0")){
                                                        Toast.makeText(getApplicationContext(), "리뷰 등록 실패", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getApplicationContext(), "리뷰 등록 성공.", Toast.LENGTH_SHORT).show();
                                                        main_refresh();
                                                    }
                                                }
                                            });
                                            in.close();
                                        }

                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }

                                }

                            });
                        }
                    }
                });

                dig.setNegativeButton("취소", null);
                dig.show();

            }



        });



        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),Integer.toString(num),Toast.LENGTH_SHORT).show();



                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL endPoint =
                                    new URL(IP+"/OurMeal/m_storePage/reviewAdd");
                            HttpURLConnection myConnection =
                                    (HttpURLConnection) endPoint.openConnection();

                            // 메소드 방식 변경
                            myConnection.setRequestMethod("POST");

                            String strNum = Integer.toString(more_button());

                            Log.d(LOG_TAG,strNum);
                            final String num = "num="+strNum;
                            //String store_code = "&store_code="+store.getStore_code();
                            String store_code = "&store_code="+getStorecode;

                            // 출력 스트림을 사용할 것이다.
                            myConnection.setDoOutput(true);
                            myConnection.getOutputStream().write(num.getBytes());
                            myConnection.getOutputStream().write(store_code.getBytes());

                            if(myConnection.getResponseCode() == 200){
                                // Success
                                BufferedReader in =
                                        new BufferedReader(
                                                new InputStreamReader(
                                                        myConnection.getInputStream()));
                                StringBuffer buffer = new StringBuffer();
                                String temp = null;

                                while((temp = in.readLine()) != null)
                                    buffer.append(temp);

                                Log.d(LOG_TAG,buffer.toString());

                                Gson gson = new Gson();

                                JSONObject json = new JSONObject(buffer.toString());

                                final String json_append_review = json.getString("append_review");
                                Log.d(LOG_TAG,json_append_review);

                                // 리뷰 리스트에 내용 담기
                                JSONArray json_list =  json.getJSONArray("review_list");
                                final ArrayList <Star_bulletin> list = new ArrayList<>();
                                for(int i = 0; i < json_list.length();i++){
                                    Star_bulletin sb = new Star_bulletin();
                                    JSONObject jo = json_list.getJSONObject(i);

                                    sb.setMember_id(jo.getString("member_id"));
                                    sb.setSb_image(jo.getString("sb_image"));
                                    sb.setSb_content(jo.getString("sb_content"));
                                    sb.setSb_score(jo.getString("sb_score"));
                                    sb.setSb_no(jo.getString("sb_no"));
                                    sb.setSb_u_date(jo.getString("sb_u_date"));
                                    sb.setStore_code(jo.getString("store_code"));

                                    sb.setMember_image(jo.getString("member_image"));

                                    list.add(sb);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ReviewAdapter myAdapter = new ReviewAdapter(list, mGlideRequestManager);


                                        mRecyclerView.setAdapter(myAdapter);




                                        if (Integer.parseInt(json_append_review)>= store.getStore_reviewCount()) {
                                            btn_more.setVisibility(View.GONE);
                                        }else {
                                            btn_more.setText("더보기(" + json_append_review + "/" + store.getStore_reviewCount() + ")");
                                            btn_more.setVisibility(View.VISIBLE);
                                        }


                                    }
                                });



                            } else {

                                Log.d(LOG_TAG,"연결실패");

                            }


                        } catch (Exception e) {
                            Log.d(LOG_TAG,e.getMessage());

                        }
                    }
                });


            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        initRefs();

        //다른데서 넘오언 스토어 코드값.
        Intent get_data = getIntent();
        getStorecode = get_data.getStringExtra("store_code");

        //Toast.makeText(getApplicationContext(), "스토어 코드 값." +code, Toast.LENGTH_SHORT).show();

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }


        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        mGlideRequestManager = Glide.with(getApplicationContext());

        int permission_internet = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);

        if(permission_internet == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},12);

        }


        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this) {
        };

        //String testSc = "store_code=S19010800001";
        String testSc = "store_code="+getStorecode;
        sc.compareTo(testSc);

        Log.d(LOG_TAG,sc);

        connected_db();
        ArrayList<Food_menu>menu = (ArrayList<Food_menu>) map.get("menu_list");
        setEvents();

    }



    public  void connected_db(){
        String test ="";
        AsyncTask.execute(new Runnable() {
            @Override

            public void run() {
                try {
                    URL endPoint =
                            new URL(IP+"/OurMeal/m_storePage?"+"store_code="+getStorecode);
                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();
                    //myConnection.setRequestMethod("GET");

                    if(myConnection.getResponseCode() == 200) {
                        // Success
                        BufferedReader in =
                                new BufferedReader(
                                        new InputStreamReader(
                                                myConnection.getInputStream(),"UTF-8"));
                        StringBuffer buffer = new StringBuffer();
                        String temp = null;

                        while ((temp = in.readLine()) != null)
                            buffer.append(temp);

                        Log.d(LOG_TAG,buffer.toString());
                        Gson gson = new Gson();
                        JSONObject json = new JSONObject(buffer.toString());
                        String json_store = json.getJSONObject("store").toString();

                        // 가게 정보 담기
                        store = gson.fromJson(json_store,Store.class);
                        map.put("store",store);


                        // 리뷰 리스트에 내용 담기
                        JSONArray json_list =  json.getJSONArray("review_list");
                        final ArrayList <Star_bulletin> list = new ArrayList<>();
                        for(int i = 0; i < json_list.length();i++){
                            Star_bulletin sb = new Star_bulletin();
                            JSONObject jo = json_list.getJSONObject(i);

                            sb.setMember_id(jo.getString("member_id"));
                            sb.setSb_image(jo.getString("sb_image"));
                            sb.setSb_content(jo.getString("sb_content"));
                            sb.setSb_score(jo.getString("sb_score"));
                            sb.setSb_no(jo.getString("sb_no"));
                            sb.setSb_u_date(jo.getString("sb_u_date"));
                            sb.setStore_code(jo.getString("store_code"));

                            sb.setMember_image(jo.getString("member_image"));

                            list.add(sb);
                        }

                        // 이미지 리스트
                        JSONArray json_imageList =  json.getJSONArray("image_list");
                        final ArrayList <Star_bulletin> imageList = new ArrayList<>();
                        for(int i = 0; i < json_imageList.length();i++){
                            Star_bulletin sb = new Star_bulletin();
                            JSONObject jo = json_imageList.getJSONObject(i);


                            sb.setSb_image(jo.getString("sb_image"));


                            imageList.add(sb);
                        }

                        map.put("imageList",imageList);
                        map.put("list",list);
                        // 메뉴 리스트에 내용 담기

                        JSONArray json_menu_list =  json.getJSONArray("menuList");
                        ArrayList <Food_menu> menu_list = new ArrayList<>();
                        for(int i = 0; i < json_menu_list.length();i++){
                            Food_menu fm = new Food_menu();
                            JSONObject jo = json_menu_list.getJSONObject(i);

                            fm.setStore_code(jo.getString("store_code"));
                            fm.setFm_code(jo.getString("fm_code"));
                            fm.setFm_name(jo.getString("fm_name"));
                            fm.setFm_image(jo.getString("fm_image"));
                            fm.setFm_info(jo.getString("fm_info"));
                            fm.setFm_price(jo.getString("fm_price"));
                            fm.setFm_kcal(jo.getString("fm_kcal"));
                            fm.setFm_allergy(jo.getString("fm_allergy"));
                            menu_list.add(fm);
                        }

                        map.put("menu_list",menu_list);

                        map.put("test","test");

                        Log.d(LOG_TAG,"avg : " + store.getScore_avg());
                        Log.d(LOG_TAG,"review_count : " + store.getStore_reviewCount());

                        in.close();

                        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist

                        // 맨 위에 이미지
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Log.d(LOG_TAG,"imageList size : "+Integer.toString(imageList.size()));
                                for(int i = 0 ; i < imageList.size(); i++){

                                    if(!imageList.get(i).getSb_image().trim().equals("")) {
                                        Log.d(LOG_TAG,"image " + "(" + i + ")" + imageList.get(i).getSb_image());
                                        String str_imgPath = IP+"/OurMeal/"+imageList.get(i).getSb_image();

                                        // Glide.with(getApplicationContext()).load("http://192.168.25.183:8080/OurMeal/"+list.get(i).getSb_image()).into(imageView1);

                                        ImageView iv = new ImageView(getApplicationContext());
                                        iv.setLayoutParams(new ViewGroup.LayoutParams(new LinearLayout.LayoutParams(470, 350)));
                                        iv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                                        iv.setPadding(0,0,20,0);
                                        img_layout.addView(iv);
                                        Glide.with(getApplicationContext()).load(str_imgPath).into(iv);

                                    }
                                }

                                store_title.setText(store.getStore_title());
                                score_avg.setText(store.getScore_avg());
                                store_info.setText(store.getStore_info());
                                store_address.setText(store.getStore_address());

                                //지도
                                WebSettings webSettings = webView.getSettings();
                                webSettings.setJavaScriptEnabled(true);
                                webView.loadUrl("https://m.map.daum.net/");
                                webView.setWebViewClient(new WebViewClient());

                                str_reviewCount.setText("주요 리뷰("+store.getStore_reviewCount()+")");

                                mRecyclerView.setLayoutManager(mLayoutManager);

                                mRecyclerView.setHasFixedSize(true);


                                ReviewAdapter myAdapter = new ReviewAdapter(list,mGlideRequestManager);

                                mRecyclerView.setAdapter(myAdapter);
                                btn_more.setText("더보기(5/"+store.getStore_reviewCount()+")");
                                if(store.getStore_reviewCount() < 6){
                                    btn_more.setVisibility(View.GONE);


                                    Log.d(LOG_TAG,"END");

                                }

                            }
                        });
/*


                 data.add("http://172.30.1.53:8080/OurMeal/"+list.get(i).getSb_image());
                    autoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
                    AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(getApplicationContext(), data);
                    autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
                    autoViewPager.setInterval(4000); // 페이지 넘어갈 시간 간격 설정
                    autoViewPager.startAutoScroll(); //Auto Scroll 시작*/
           /*
                    for(int i = 0; i < list.size(); i++){
                        Log.d(LOG_TAG,"list ("+ (i+1) +") ID : " + list.get(i).getMember_id()  );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") CONTENT : " + list.get(i).getSb_content()  );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") IMAGE : " + list.get(i).getSb_image()  );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") MEMBER_IMAGE : " + list.get(i).getMember_image());

                    }
                     for(int i = 0; i < menu_list.size(); i++){
                        Log.d(LOG_TAG,"list ("+ (i+1) +") NAME : " + menu_list.get(i).getFm_name()  );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") INFO : " +menu_list.get(i).getFm_info()  );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") IMAGE : " + menu_list.get(i).getFm_image() );
                        Log.d(LOG_TAG,"list ("+ (i+1) +") ALLERGY : " + menu_list.get(i).getFm_allergy());

                    }

                    Log.d(LOG_TAG,"store_title : " + store.getStore_title());*/

                    /*
                    JsonArray jsonArray = jsonObject.getAsJsonArray("e");

                    JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();
                    JsonPrimitive jsonPrimitive = jsonObject1.getAsJsonPrimitive("bb");
                    int value = jsonPrimitive.getAsInt();*/




                    /*
                    Star_bulletin list = (Star_bulletin) data.get("list");
                    Star_bulletin score_list = (Star_bulletin) data.get("score_list");
                    Star_bulletin image_list = (Star_bulletin) data.get("image_list");
                    String size = (String)data.get("size");
                    String starAvg = (String)data.get("starAvg");
                    String reviewCount = (String)data.get("reviewCount");


                    Log.d(LOG_TAG, String.valueOf(store.get("store_image")));
                    */

                    }


                } catch (Exception e) {
                    Log.d(LOG_TAG,e.getMessage());

                }
            }


        });


    }

    //액티비티 새로고침
    private void main_refresh(){
        Intent main_intent = getIntent();
        finish();
        startActivity(main_intent);
    }

    // 파일명 찾기
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}

