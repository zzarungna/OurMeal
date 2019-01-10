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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String IP = "http://192.168.0.17:8080"; //집
    private static String LOG_TAG = "아이유";

    Boolean search_status;
    Toolbar toolBar;
    EditText str_main_search;
    View dialogView;

    //검색 결과
    ArrayList<S_Store> search_store_list = new ArrayList<>();

    //검색어
    String search_result = null;
    public void initRefs() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        str_main_search = findViewById(R.id.str_main_search);
        search_status = false;
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
                    search_result = str_main_search.getText().toString();
                    Toast.makeText(getApplicationContext(),"엔터 검색어 : " + search_result, Toast.LENGTH_SHORT).show();

                    //검색 메소드 실행//검색 테스트용 이유저
                    String test = "이유저";
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
        return  true;
    }
    // 툴바 메뉴&검색 사용 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_login:
                dialogView = (View)View.inflate(
                        MainActivity.this,
                        R.layout.activity_login, null);
                AlertDialog.Builder dig =
                        new AlertDialog.Builder(MainActivity.this);
                dig.setView(dialogView);
                dig.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText str_id =
                                (EditText)dialogView.findViewById(R.id.str_id);
                        EditText str_pw =
                                (EditText)dialogView.findViewById(R.id.str_pw);

                        String msg = "입력된 ID : " + str_id.getText().toString() + "\n";
                        msg += "입력된 PASSWORD : " + str_pw.getText().toString();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
                dig.setNegativeButton("취소", null);
                dig.show();
                Toast.makeText(this, "로그인", Toast.LENGTH_LONG).show();
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
                            search_status = true;
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

        if(search_status){
            //여기서 검색 결과 레이아웃으로 이동
            Intent search_intent = new Intent(getApplicationContext(), SearchActivity.class);
            Log.d("아이유", String.valueOf(search_store_list.size()));

            //검색어 전달등 마무리하면 끝날듯.
            search_intent.putExtra("search", search_result);
            search_intent.putExtra("list", search_store_list);

            startActivity(search_intent);
        }
    }



}
