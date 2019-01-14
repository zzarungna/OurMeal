package com.javaking.ourmeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaking.ourmeal.model.S_Store;
import com.javaking.ourmeal.model.Store;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//abcd
    private static final String IP = "http://192.168.10.50:8080";
    private static String LOG_TAG = "아이유";

    String msg = "아이디를 입력하세요";
    int erorr_code;

    Toolbar toolBar;
    View dialogView;
    Menu visible; // 메뉴 버튼 활성화 & 비활성화

    EditText str_main_search;
    ArrayList<S_Store> search_store_list = new ArrayList<>();
    String search_result;

    public void initRefs() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        str_main_search = findViewById(R.id.str_main_search);
        search_result = str_main_search.getText().toString();
    }

    public void setEvents() {

        str_main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //첫화면 검색 포커스를 없앤후 클릭시 포커스 활성
                str_main_search.setFocusableInTouchMode(true);
            }
        });

        //엔터키 검색
        str_main_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                    //검색 메소드 실행//검색 테스트용 이유저
                    String test = "만리재로";
                    searchSelect(test);
                }
                return false;
            }
        });

    }

    // 툴바 생성 메소드 & 툴바 검색 기능 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        visible = menu;
        return  true;
    }
    // 툴바 메뉴&검색 사용 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        if (CookieManager.getInstance().getCookie(IP +"/OurMeal") != null) {
            visible.findItem(R.id.menu_login).setVisible(false);
            visible.findItem(R.id.menu_logout).setVisible(true);
            visible.findItem(R.id.menu_regist).setVisible(false);
            visible.findItem(R.id.menu_mypage).setVisible(true);
            visible.findItem(R.id.menu_partner).setVisible(true);
            visible.findItem(R.id.menu_store).setVisible(true);
            visible.findItem(R.id.menu_article).setVisible(true);
        } else {
            visible.findItem(R.id.menu_login).setVisible(true);
            visible.findItem(R.id.menu_logout).setVisible(false);
            visible.findItem(R.id.menu_regist).setVisible(true);
            visible.findItem(R.id.menu_mypage).setVisible(false);
            visible.findItem(R.id.menu_partner).setVisible(false);
            visible.findItem(R.id.menu_store).setVisible(false);
            visible.findItem(R.id.menu_article).setVisible(false);
        }
        switch (item.getItemId()){
            case R.id.menu_login:
                dialogView = (View)View.inflate(MainActivity.this, R.layout.activity_login, null);
                AlertDialog.Builder menu_login = new AlertDialog.Builder(MainActivity.this);
                final EditText str_id =
                        (EditText)dialogView.findViewById(R.id.str_id);
                final EditText str_pw =
                        (EditText)dialogView.findViewById(R.id.str_pw);
                menu_login.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if (str_id.getText().toString().trim().length() == 0) {
                            Toast.makeText(MainActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(str_pw.getText().toString().trim().length() == 0) {
                            Toast.makeText(MainActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL endPoint =
                                            new URL(IP +"/OurMeal/restful/login");
                                    HttpURLConnection myConnection =
                                            (HttpURLConnection) endPoint.openConnection();
                                    myConnection.setRequestMethod("POST");
                                    myConnection.setDoOutput(true);
                                    myConnection.setDoInput(true);
                                    String cookieString =
                                            CookieManager.getInstance().getCookie(
                                                    IP+"/OurMeal");
                                    if (cookieString != null) {
                                        myConnection.setRequestProperty("Cookie", cookieString);
                                    }

                                    String requestParam = String.format("member_id=%s&member_pw=%s", str_id.getText().toString(), str_pw.getText().toString());

                                    myConnection.getOutputStream().write(requestParam.getBytes());

                                    if (myConnection.getResponseCode() == 200) {
                                        Map<String, List<String>> headerFields = myConnection.getHeaderFields();
                                        String COOKIES_HEADER = "Set-Cookie";
                                        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                                        if (cookiesHeader != null) {
                                            for (String cookie : cookiesHeader) {
                                                String cookieName = HttpCookie.parse(cookie).get(0).getName();
                                                String cookieValue = HttpCookie.parse(cookie).get(0).getValue();

                                                cookieString = cookieName + "=" + cookieValue;
                                                CookieManager.getInstance().setCookie(IP+"/OurMeal", cookieString);
                                            }
                                            Log.d(LOG_TAG, CookieManager.getInstance().getCookie(IP+"/OurMeal"));
                                        }
                                    } else {
                                        erorr_code = 1;
                                    }
                                } catch (Exception e) {
                                    Log.d(LOG_TAG, e.getMessage());
                                }
                            }
                        });
                        if (erorr_code == 1) {
                            Toast.makeText(MainActivity.this, "서버 요청 에러", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                menu_login.setView(dialogView);
                menu_login.setNegativeButton("취소", null);
                menu_login.show();
                break;
            case R.id.menu_logout:
                CookieManager.getInstance().removeAllCookies(null);
                Toast.makeText(this, "로그아웃(쿠키가 삭제됨)", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_regist:
                Toast.makeText(this, "회원가입", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_mypage:
                Toast.makeText(this, "마이페이지", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
                break;
            /*
            case R.id.menu_partner:
                Toast.makeText(this, "파트너신청", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), PartnerActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_store:
                Toast.makeText(this, "스토어", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), StoreActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_article:
                Toast.makeText(this, "게시판", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplicationContext(), ArticleActivity.class);
                startActivity(intent);
                break;
                */
            case R.id.main_search:
                search_result = str_main_search.getText().toString();

                //검색 메소드 실행//검색 테스트용 이유저
                String test = "이유저";
                searchSelect(test);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRefs();
        setEvents();
        setSupportActionBar(toolBar);


    }

    //검색 웹서버 통신 메소드
    private void searchSelect(final String search_Text){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL endPoint = new URL(IP + "/OurMeal/m/search");

                    HttpURLConnection myConnection =
                            (HttpURLConnection) endPoint.openConnection();
                    myConnection.setRequestMethod("POST");

                    final String requestParam = String.format("search=%s", search_Text);

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
                        search_store_list = gson.fromJson(buffer.toString(), new TypeToken<ArrayList<S_Store>>(){}.getType());


                        if(search_store_list!=null){
                            //검색 결과
                            String search_text = str_main_search.getText().toString();

                            //여기서 검색 결과 레이아웃으로 이동
                            Intent search_intent = new Intent(getApplicationContext(), SearchActivity.class);
                            Log.d("아이유", String.valueOf(search_store_list.size()));

                            //인텐트로 값전달
                            search_intent.putExtra("search", search_text);
                            search_intent.putExtra("list", search_store_list);

                            startActivity(search_intent);

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), search_Text+"에 대한 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
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
