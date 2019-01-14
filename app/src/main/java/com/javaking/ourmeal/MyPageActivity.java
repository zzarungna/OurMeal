package com.javaking.ourmeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaking.ourmeal.model.Health;
import com.javaking.ourmeal.model.Member;
import com.javaking.ourmeal.model.S_Store;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyPageActivity extends AppCompatActivity {

    private static final String LOG_TAG = "아이유";
    private static final String IP = "http://192.168.0.17:8080";//집

    Toolbar toolBar;
    String image_path = null;
    String image_name = null;

    // 개인 정보 수정
    LinearLayout layout_mb_dataupdate;
    Button btn_mb_dataupdate;
    EditText str_mb_name;
    EditText str_mb_email;
    EditText str_mb_phone;
    EditText str_mb_happy;
    // 비밀 번호 변경
    LinearLayout layout_mb_pwupdate;
    Button btn_mb_pwupdate;
    EditText str_mb_real_pw; // 기존 비밀번호
    EditText str_mb_pw; // 새로운 비밀번호
    EditText str_mb_pwre; // 새로운 비밀번호 확인
    // 신체 정보 입력
    LinearLayout layout_mb_helth;
    Button btn_mb_helth;
    EditText str_mb_cm;
    EditText str_mb_kg;
    EditText str_mb_kcal;
    // 프로필 사진 등록
    LinearLayout layout_mb_image;
    Button btn_mb_image; // 이미지 사진 입력필드 열기 버튼
    ImageView image_mb; // 이미지 출력 뷰
    Button btn_mb_imageset; // 사진 첨부 버튼
    // 확인 취소 버튼
    Button btn_my_cancel;
    Button btn_my_ok;

    //테스트 로그
    TextView testLog;

    //회원 로그인 아이디 나중에 로그인한 값으로 대체하면된다.
    String member_id = CookieManager.getInstance().getCookie("login_id");

    public void initRefs() {

        //상단바
        toolBar = (Toolbar) findViewById(R.id.toolBar_mypage);

        // 개인 정보 수정btn_my_ok
        layout_mb_dataupdate = (LinearLayout)findViewById(R.id.layout_mb_dataupdate);
        btn_mb_dataupdate = findViewById(R.id.btn_mb_dataupdate);
        str_mb_name = (EditText)findViewById(R.id.str_mb_name);
        str_mb_email = (EditText)findViewById(R.id.str_mb_email);
        str_mb_phone = (EditText)findViewById(R.id.str_mb_phone);
        str_mb_happy = (EditText)findViewById(R.id.str_mb_happy);
        // 비밀 번호 변경
        layout_mb_pwupdate = (LinearLayout)findViewById(R.id.layout_mb_pwupdate);
        btn_mb_pwupdate = findViewById(R.id.btn_mb_pwupdate);
        str_mb_real_pw = (EditText)findViewById(R.id.str_mb_real_pw);
        str_mb_pw = (EditText)findViewById(R.id.str_mb_pw);
        str_mb_pwre = (EditText)findViewById(R.id.str_mb_pwre);
        // 신체 정보 입력
        layout_mb_helth = (LinearLayout)findViewById(R.id.layout_mb_helth);
        btn_mb_helth = findViewById(R.id.btn_mb_helth);
        str_mb_cm = findViewById(R.id.str_mb_cm);
        str_mb_kg = findViewById(R.id.str_mb_kg);
        str_mb_kcal = findViewById(R.id.str_mb_kcal);
        // 프로필 사진 등록
        layout_mb_image = (LinearLayout)findViewById(R.id.layout_mb_image);
        btn_mb_image = findViewById(R.id.btn_mb_image);
        image_mb = (ImageView)findViewById(R.id.image_mb);
        btn_mb_imageset = findViewById(R.id.btn_mb_imageset);
        // 확인 취소 버튼
        btn_my_cancel = findViewById(R.id.btn_my_cancel);
        btn_my_ok = findViewById(R.id.btn_my_ok);

        //칼로리 읽기 전용
        str_mb_kcal.setFocusable(false);
        str_mb_kcal.setClickable(false);

        testLog = (TextView)findViewById(R.id.textView_log);
    }

    int num = 0;

    public void setEvents() {

        //홈버튼 클릭
        toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntet = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(mainIntet);
            }
        });

        //취소 버튼
        btn_my_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPageActivity.super.onBackPressed();
            }
        });

        //이미지 파일 첨부
        btn_mb_imageset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);

            }
        });


        final int [] dataupdate = {0};
        btn_mb_dataupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (dataupdate[0] == 0) {
                    layout_mb_dataupdate.setVisibility(View.VISIBLE);
                    dataupdate[0] = 1;
                } else {
                    layout_mb_dataupdate.setVisibility(View.GONE);
                    dataupdate[0] = 0;
                }
            }
        });
        final int [] pwupdate = {0};
        btn_mb_pwupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pwupdate[0] == 0) {
                    layout_mb_pwupdate.setVisibility(View.VISIBLE);
                    pwupdate[0] = 1;
                } else {
                    layout_mb_pwupdate.setVisibility(View.GONE);
                    pwupdate[0] = 0;
                }
            }
        });
        final int [] helth = {0};
        btn_mb_helth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (helth[0] == 0) {
                    layout_mb_helth.setVisibility(View.VISIBLE);
                    helth[0] = 1;
                } else {
                    layout_mb_helth.setVisibility(View.GONE);
                    helth[0] = 0;
                }
                //select

                //신체 사이즈 입력 하면 하루 권장 섭취 칼로리 나옴.
                //신체 사이즈 관련 처리
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL endPoint = new URL(IP + "/OurMeal/myPage/Health_Select");

                            HttpURLConnection myConnection =
                                    (HttpURLConnection) endPoint.openConnection();
                            myConnection.setRequestMethod("POST");

                            String requestParam = String.format("member_id=%s", member_id);

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
                                final Health health = gson.fromJson(buffer.toString(), Health.class);

                                if(health!=null){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            str_mb_cm.setHint(health.getHealth_height().toString());
                                            str_mb_kg.setHint(health.getHealth_weight().toString());
                                            str_mb_kcal.setText(health.getHealth_basal().toString()+"㎉");
                                        }
                                    });
                                }
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
                                    Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
        final int [] image = {0};
        btn_mb_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (image[0] == 0) {
                    layout_mb_image.setVisibility(View.VISIBLE);
                    image[0] = 1;
                } else {
                    layout_mb_image.setVisibility(View.GONE);
                    image[0] = 0;
                }
            }
        });

        //개인정보 데이터 가져온다.
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endPoint = new URL(IP + "/OurMeal/myPage/select");

                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();
                    myConnection.setRequestMethod("POST");

                    final String requestParam = String.format("member_id=%s", member_id);
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
                        Member member = gson.fromJson(buffer.toString(), Member.class);

                        str_mb_name.setHint(member.getMember_name().toString());
                        str_mb_email.setHint(member.getMember_email().toString());
                        str_mb_phone.setHint(member.getMember_phone().toString());
                        str_mb_happy.setHint(member.getMember_birth().toString());
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
                            Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        btn_my_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지 사진이 바꼈을때
                if(num != 0 ){
                    //웹서버에 사진이미지 파일 전송.
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String attachmentName = "file";
                                String attachmentFileName = null;

                                if(image_path!=null) {
                                    //파일 이름
                                    attachmentFileName  = image_name;
                                    Log.d("CheckImage", Integer.toString(image_path.length()));
                                }
                                Log.d("CheckImage 경로", image_path);
                                String crlf = "\r\n";
                                String twoHyphens = "--";
                                String boundary = "*****";

                                HttpURLConnection httpUrlConnection = null;
                                URL url = new URL(IP + "/OurMeal/myPage/profile_image_upload");

                                httpUrlConnection = (HttpURLConnection) url.openConnection();
                                httpUrlConnection.setUseCaches(false);
                                httpUrlConnection.setDoOutput(true);
                                httpUrlConnection.setRequestMethod("POST");

                                // key, value 형태 // Content-Type: multipart/form-data; boundary=*****
                                httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                                DataOutputStream request = new DataOutputStream(httpUrlConnection.getOutputStream());

                                // 프로필 파일 넘기기
                                // --*****\r\n
                                // Content-Disposition: form-data; name="file" ;
                                //
                                // 파일 값
                                //

                                // 0이 아닐때만 보내도록 // 이미지 첨부하지 않은때도 있으므로
                                if(image_path!=null) {
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
                                request.writeBytes("Content-Disposition: form-data; name=\"member_id\"" + crlf);
                                request.writeBytes(crlf);
                                request.writeBytes(member_id + crlf);
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
                                            if(!result.equals("0")){
                                                Toast.makeText(getApplicationContext(), "사진 등록 및 변경 성공", Toast.LENGTH_SHORT).show();
                                                Glide.with(getApplicationContext()).load(IP+"/OurMeal" + result).into(image_mb);
                                            }else{
                                                Toast.makeText(getApplicationContext(), "사진 등록 및 변경 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                        }

                    });

                }

                //개인정보 수정 진행
                if( str_mb_name.getText().toString().trim().length() > 0 ||
                        str_mb_email.getText().toString().trim().length() > 0 ||
                        str_mb_phone.getText().toString().trim().length() > 0 ||
                        str_mb_happy.getText().toString().trim().length() > 0){

                    //개인정보 데이터 수정
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL endPoint = new URL(IP + "/OurMeal/myPage/profile_update");

                                HttpURLConnection myConnection =
                                        (HttpURLConnection) endPoint.openConnection();
                                myConnection.setRequestMethod("POST");

                                final String name = str_mb_name.getText().toString();
                                final String email = str_mb_email.getText().toString();
                                final String phone = str_mb_phone.getText().toString();
                                final String birth = str_mb_happy.getText().toString();
                                final String requestParam = String.format("name=%s&email=%s&phone=%s&birth=%s&member_id=%s", name,email,phone,birth,member_id);

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
                                    final String result = gson.fromJson(buffer.toString(), String.class);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(result.equals("0")){
                                                Toast.makeText(getApplicationContext(), "개인정보 수정 실패", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), "개인정보 수정 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                                str_mb_name.setText("");
                                                str_mb_email.setText("");
                                                str_mb_phone.setText("");
                                                str_mb_happy.setText("");

                                                str_mb_name.setHint(name);
                                                str_mb_email.setHint(email);
                                                str_mb_phone.setHint(phone);
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
                                        Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }


                if(!str_mb_pw.getText().toString().equals(str_mb_pwre.getText().toString())){
                    Toast.makeText(getApplicationContext(), "입력한 비밀번호와 비밀번호 확인이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //개인정보 패스워드 업데이트 진행
                if( str_mb_real_pw.getText().toString().trim().length() > 0 ||
                        str_mb_pw.getText().toString().trim().length() > 0 ||
                        str_mb_pwre.getText().toString().trim().length() > 0 ){

                    //비밀번호 정보 수정
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL endPoint = new URL(IP + "/OurMeal/myPage/password_update"); //집

                                HttpURLConnection myConnection =
                                        (HttpURLConnection) endPoint.openConnection();
                                myConnection.setRequestMethod("POST");

                                //pw 와 기존 pwre가 맞지 않은 경우는 토스트로 알람을 뛰워준다.
                                final String realpw = str_mb_real_pw.getText().toString();
                                final String pw = str_mb_pw.getText().toString();
                                final String pwre = str_mb_pwre.getText().toString();

                                final String requestParam = String.format("realpw=%s&pw=%s&pwre=%s&member_id=%s", realpw,pw,pwre,member_id);

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
                                    final String result = gson.fromJson(buffer.toString(), String.class);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(result.equals("0")){
                                                Toast.makeText(getApplicationContext(), "기존 비밀번호와 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                                //비밀번호 정보 클리어
                                                str_mb_real_pw.setText("");
                                                str_mb_pw.setText("");
                                                str_mb_pwre.setText("");
                                            }else{
                                                Toast.makeText(getApplicationContext(), "비밀번호 수정 완료!", Toast.LENGTH_SHORT).show();
                                                //비밀번호 정보 클리어
                                                str_mb_real_pw.setText("");
                                                str_mb_pw.setText("");
                                                str_mb_pwre.setText("");
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
                                        Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

                //신체 사이즈 입력 하면 하루 권장 섭취 칼로리 나옴.
                if(str_mb_cm.getText().toString().trim().length() > 0 || str_mb_kg.getText().toString().trim().length() > 0){

                    //신체 사이즈 관련 처리
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL endPoint = new URL(IP + "/OurMeal/myPage/Health_IU");

                                HttpURLConnection myConnection =
                                        (HttpURLConnection) endPoint.openConnection();
                                myConnection.setRequestMethod("POST");


                                String cm = str_mb_cm.getText().toString();
                                String kg = str_mb_kg.getText().toString();

                                String requestParam = String.format("cm=%s&kg=%s&member_id=%s", cm,kg,member_id);

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
                                    final Health health = gson.fromJson(buffer.toString(), Health.class);

                                    if(health!=null){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "키 몸무게 정보 등록 및 변경 성공!", Toast.LENGTH_SHORT).show();
                                                str_mb_cm.setText("");
                                                str_mb_kg.setText("");
                                                str_mb_kcal.setText("");

                                                str_mb_cm.setHint(health.getHealth_height().toString() + "cm");
                                                str_mb_kg.setHint(health.getHealth_weight().toString() + "kg" );
                                                str_mb_kcal.setText(health.getHealth_basal().toString() + "㎉");
                                            }
                                        });
                                    }



                                    if(health==null){
                                        final String result = health.toString();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "키 몸무게 정보 등록 실패", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
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
                                        Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        initRefs();
        setEvents();
        setSupportActionBar(toolBar);

        //기본 프로필 이미지 체크 및 가져오기.
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endPoint = new URL(IP + "/OurMeal/myPage/profile_image_select");

                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();
                    myConnection.setRequestMethod("POST");

                    String requestParam = String.format("member_id=%s", member_id);

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
                        final String result = gson.fromJson(buffer.toString(), String.class);

                        if(result!=null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(IP + "/OurMeal" + result).into(image_mb);
                                }
                            });
                        }

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
                            Toast.makeText(getApplicationContext(), "서버 연결 및 메세지 읽기 실패2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == 1) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final Uri uri = data.getData();
                final String path = getPath(uri);
                image_name = getName(uri);
                //final String name = getName(uri);

                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    image_mb.setImageBitmap(Bitmap.createScaledBitmap(img,730, 710, false));
                    //imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 130, 110, false));
                    num = 1;
                    image_path = getFilesDir().toString();

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
    }

    // 실제 경로 찾기
    private String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    // uri 아이디 찾기
    private String getUriId(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns._ID };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 툴바 생성 메소드 & 툴바 검색 기능 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu, menu);
        return  true;
    }

}
