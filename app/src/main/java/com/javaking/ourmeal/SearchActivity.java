package com.javaking.ourmeal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.javaking.ourmeal.model.S_Store;
import com.javaking.ourmeal.model.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String IP = "http://192.168.0.17:8080/OurMeal/"; //집

    TextView search_title;
    ImageView serach_result_img;
    TextView serach_result_title;
    TextView serach_result_num;
    TextView serach_result_addr;
    TextView serach_result_rcount;

    private void init() {
        search_title = (TextView)findViewById(R.id.search_title);
        serach_result_img = (ImageView)findViewById(R.id.serach_result_img);
        serach_result_title = (TextView)findViewById(R.id.serach_result_title);
        serach_result_num = (TextView)findViewById(R.id.serach_result_num);
        serach_result_addr = (TextView)findViewById(R.id.serach_result_addr);
        serach_result_rcount = (TextView)findViewById(R.id.serach_result_rcount);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);


        init();

        String s_title = getIntent().getStringExtra("search");
        ArrayList<S_Store> result = (ArrayList<S_Store>)getIntent().getSerializableExtra("list");

        Log.d("이미지 경로", IP+result.get(0).getStore_image());
        Glide.with(getApplicationContext()).load(IP+result.get(0).getStore_image()).into(serach_result_img);
        serach_result_title.setText(result.get(0).getStore_title());
        serach_result_num.setText(result.get(0).getScore_avg());
        serach_result_addr.setText(result.get(0).getStore_address());
        serach_result_rcount.setText(String.valueOf(result.get(0).getStore_reviewCount()));

        search_title.setText( s_title +"에 대한 검색 결과");

        //0개 이후는 동적으로 생성하면 끝.

    }




}
