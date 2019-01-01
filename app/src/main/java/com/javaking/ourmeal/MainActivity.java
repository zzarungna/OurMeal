package com.javaking.ourmeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;
    SearchView main_search;
    EditText str_main_search;

    View dialogView;

    public void initRefs() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        main_search = toolBar.findViewById(R.id.main_search);
        str_main_search = findViewById(R.id.str_main_search);

    }

    public void setEvents() {
        /*main_search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = str_main_search.getText().toString();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });*/
    }

// 툴바 생성 메소드 & 툴바 검색 기능 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        /*SearchView main_search = (SearchView)menu.findItem(R.id.main_search).getActionView();
        main_search.setMaxWidth(Integer.MAX_VALUE);
        main_search.setQueryHint("모델명으로 검색합니다.");
        MenuItem item_like = menu.add(0,0,0,"히든 메뉴");
        item_like.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });*/
        return  true;
    }
// 툴바 메뉴&검색 사용 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.main_search) {
            return true;
        }*/
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

}
