package com.javaking.ourmeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.javaking.ourmeal.model.S_Store;
import com.javaking.ourmeal.model.Store;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String IP = "http://192.168.0.17:8080";//학원

    TextView search_title;
    ImageView serach_result_img;
    TextView serach_result_title;
    TextView serach_result_num;
    TextView serach_result_addr;
    TextView serach_result_rcount;
    Button main_Btn;

    Toolbar toolBar;

    private void init() {
        search_title = (TextView)findViewById(R.id.search_title);
        serach_result_img = (ImageView)findViewById(R.id.serach_result_img);
        serach_result_title = (TextView)findViewById(R.id.serach_result_title);
        serach_result_num = (TextView)findViewById(R.id.serach_result_num);
        serach_result_addr = (TextView)findViewById(R.id.serach_result_addr);
        serach_result_rcount = (TextView)findViewById(R.id.serach_result_rcount);
        main_Btn = (Button)findViewById(R.id.main_btn);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        init();

        String s_title = getIntent().getStringExtra("search");
        final ArrayList<S_Store> result = (ArrayList<S_Store>)getIntent().getSerializableExtra("list");

        Log.d("이미지 경로", IP+ "/OurMeal/" +result.get(0).getStore_image());
        Glide.with(getApplicationContext()).load(IP+"/OurMeal/"+result.get(0).getStore_image()).into(serach_result_img);
        serach_result_title.setText(result.get(0).getStore_title());
        serach_result_num.setText(result.get(0).getScore_avg());
        serach_result_addr.setText(result.get(0).getStore_address());
        serach_result_rcount.setText(String.valueOf(result.get(0).getStore_reviewCount()));
        search_title.setText(s_title +"에 대한 검색 결과");

        //스토어 페이지 전환
        serach_result_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "스토어로 넘기자", Toast.LENGTH_SHORT).show();
                Intent store_intent = new Intent(getApplicationContext(), StoreActivity.class);
                store_intent.putExtra("store_code", result.get(0).getStore_code());
                startActivity(store_intent);
            }
        });

        //0개 이후는 동적으로 생성
        LinearLayout mainlayout = (LinearLayout)findViewById(R.id.search_main);

        for(int i = 1; i<result.size(); i++){
            LinearLayout dynamicView = (LinearLayout)View.inflate(SearchActivity.this, R.layout.search_result, null);

            TextView sub_search_title = dynamicView.findViewById(R.id.search_title);
            ImageView sub_result_image = dynamicView.findViewById(R.id.serach_result_img);
            TextView sub_result_Title = dynamicView.findViewById(R.id.serach_result_title);
            TextView sub_result_num = dynamicView.findViewById(R.id.serach_result_num);
            TextView sub_result_addr = dynamicView.findViewById(R.id.serach_result_addr);
            TextView sub_result_rcount = dynamicView.findViewById(R.id.serach_result_rcount);
            Button sub_top_btn = dynamicView.findViewById(R.id.main_btn);

            sub_search_title.setText(s_title +"에 대한 검색 결과");
            Glide.with(getApplicationContext()).load(IP+"/OurMeal/"+result.get(i).getStore_image()).into(sub_result_image);
            sub_result_Title.setText(result.get(i).getStore_title());
            sub_result_num.setText(result.get(i).getScore_avg());
            sub_result_addr.setText(result.get(i).getStore_address());
            sub_result_rcount.setText(String.valueOf(result.get(i).getStore_reviewCount()));

            //메인버튼은 하나만 나오도록
            if(i>0){
                sub_top_btn.setVisibility(View.INVISIBLE);
                sub_search_title.setHeight(0);
                sub_search_title.setVisibility(View.INVISIBLE);
            }


            mainlayout.addView(dynamicView);

            click_method(sub_result_image, result.get(i).getStore_code());
        }

        main_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntet = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(mainIntet);
            }
        });


    }

    //클릭 메소드
    private void click_method(ImageView view, final String s_code){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent store_intent = new Intent(getApplicationContext(), StoreActivity.class);
                store_intent.putExtra("store_code", s_code);
                startActivity(store_intent);
            }
        });
    }

}
