package com.javaking.ourmeal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.javaking.ourmeal.model.Health;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistActivity  extends AppCompatActivity {

    private static final String IP = "http://172.30.1.37:8080";//학원

    RadioButton radiobtn_male;
    RadioButton radiobtn_female;

    private Handler handler;

    Button btn_member_address;
    Button btn_cancel;
    Button btn_submit;

    EditText member_id;
    EditText member_pw1;
    EditText member_pw2;
    EditText member_name;
    EditText member_birth;
    EditText member_phone;
    EditText member_address_01;
    EditText member_address_02;
    EditText member_address_03;
    EditText member_email;

    TextView member_id_error;
    TextView member_pw1_error;
    TextView member_pw2_error;
    TextView member_name_error;
    TextView member_birth_error;
    TextView member_sex_error;
    TextView member_phone_error;
    TextView member_address_error;
    TextView member_email_error;

    //성별
    RadioButton man;
    RadioButton woman;
    RadioGroup sex;
    String gender;

    private WebView daum_webView;
    private TextView daum_result;

    public void initRefs() {
        radiobtn_male = findViewById(R.id.radiobtn_male);
        radiobtn_female = findViewById(R.id.radiobtn_female);
        btn_member_address = findViewById(R.id.btn_member_address);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);
        member_id = findViewById(R.id.member_id);
        member_pw1 = findViewById(R.id.member_pw1);
        member_pw2 = findViewById(R.id.member_pw2);
        member_name = findViewById(R.id.member_name);
        member_birth = findViewById(R.id.member_birth);
        member_phone = findViewById(R.id.member_phone);
        member_address_01 = findViewById(R.id.member_address_01);
        member_address_02 = findViewById(R.id.member_address_02);
        member_address_03 = findViewById(R.id.member_address_03);
        member_email = findViewById(R.id.member_email);
        member_id_error = findViewById(R.id.member_id_error);
        member_pw1_error = (TextView)findViewById(R.id.member_pw1_error);
        member_pw2_error = findViewById(R.id.member_pw2_error);
        member_name_error = findViewById(R.id.member_name_error);
        member_birth_error = findViewById(R.id.member_birth_error);
        member_sex_error = findViewById(R.id.member_sex_error);
        member_phone_error = findViewById(R.id.member_phone_error);
        member_address_error = findViewById(R.id.member_address_error);
        member_email_error = findViewById(R.id.member_email_error);
        man = (RadioButton)findViewById(R.id.radiobtn_female);
        woman = (RadioButton)findViewById(R.id.radiobtn_male);
        sex = (RadioGroup)findViewById(R.id.sex);
    }

    public void setEvents() {
        sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(man.isChecked()){
                    gender = "W";
                }

                if(woman.isChecked()){
                    gender = "M";
                }
            }
        });

        btn_member_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View address = View.inflate(getApplicationContext(),
                        R.layout.activity_daum_web_view, null);
                Dialog dialog = new Dialog(RegistActivity.this);

                // WebView 설정
                daum_webView = address.findViewById(R.id.daum_webview);
                daum_result = address.findViewById(R.id.daum_result);

                // JavaScript 허용
                daum_webView.getSettings().setJavaScriptEnabled(true);

                // JavaScript의 window.open 허용
                daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
                daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

                // web client 를 chrome 으로 설정
                daum_webView.setWebChromeClient(new WebChromeClient());

                // webview url load. jsp 파일 주소
                daum_webView.loadUrl(IP + "/OurMeal/m_juso");
                // daum_webView.loadUrl("http://192.168.0.11:8080/OurMeal/m_juso");
                dialog.setContentView(address);
                dialog.setTitle("주소 검색");
                dialog.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                String id = member_id.getText().toString().trim();
                if (id.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "ID 값은 필수 항목이니 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_id_error.setText("ID 값은 필수 항목이니 입력해 주세요.");
                    return;
                } else {
                    member_id_error.setText("");
                }

                String password1 = member_pw1.getText().toString().trim();
                if( password1.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "패스워드 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_pw1_error.setText("패스워드 값을 입력해 주세요.");
                    return;
                } else {
                    member_pw1_error.setText("");
                }

                String password2 = member_pw2.getText().toString().trim();
                if( !password2.equals(password1)) {
                    Toast.makeText(getApplicationContext(),
                            "패스워드가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    member_pw2_error.setText("패스워드가 일치하지 않습니다.");
                    return;
                } else {
                    member_pw2_error.setText("");
                }

                String name = member_name.getText().toString().trim();
                if( name.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "NAME 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_name_error.setText("NAME 값을 입력해 주세요.");
                    return;
                } else {
                    member_name_error.setText("");
                }

                String birth = member_birth.getText().toString().trim();
                if( birth.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "BIRTH 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_birth_error.setText("BIRTH 값을 입력해 주세요.");
                    return;
                } else {
                    member_birth_error.setText("");
                }

                String tel = member_phone.getText().toString().trim();
                if( tel.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "TEL 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_phone_error.setText("TEL 값을 입력해 주세요.");
                    return;
                } else {
                    member_phone_error.setText("");
                }

                String address_01 = member_address_01.getText().toString().trim();
                if( address_01.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "ADDRESS_01 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_address_error.setText("ADDRESS_01 값을 입력해 주세요.");
                    return;
                } else {
                    member_address_error.setText("");
                }

                String address_02 = member_address_02.getText().toString().trim();
                if( address_02.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "ADDRESS_02 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_address_error.setText("ADDRESS_02 값을 입력해 주세요.");
                    return;
                } else {
                    member_address_error.setText("");
                }

                String address_03 = member_address_03.getText().toString().trim();
                if( address_03.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "ADDRESS_03 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_address_error.setText("ADDRESS_03 값을 입력해 주세요.");
                    return;
                } else {
                    member_address_error.setText("");
                }

                String email = member_email.getText().toString().trim();
                if (email.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "EMAIL 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_email_error.setText("EMAIL 값을 입력해 주세요");
                    return;
                } else {
                    member_email_error.setText("");
                }

                CookieManager.getInstance().removeAllCookies(null);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(IP + "/OurMeal/m_regist");
                            // URL url = new URL("http://192.168.0.11:8080/OurMeal/m_regist");
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setRequestMethod("POST");
                            String id = "member_id=" + member_id.getText().toString();
                            String pw1 = "&member_pw=" + member_pw1.getText().toString();
                            String name = "&member_name=" + member_name.getText().toString();
                            String birth = "&member_birth=" + member_birth.getText().toString();
                            String sex = "&Member_sex=" + gender;
                            String phone = "&member_phone=" + member_phone.getText().toString();

                            String address = "&member_address=" + member_address_01.getText().toString() +
                                    " " + member_address_02.getText().toString() +
                                    " " + member_address_03.getText().toString();
                            String email = "&member_email=" + member_email.getText().toString();

                            httpURLConnection.getOutputStream().write(id.getBytes());
                            httpURLConnection.getOutputStream().write(pw1.getBytes());
                            httpURLConnection.getOutputStream().write(name.getBytes());
                            httpURLConnection.getOutputStream().write(birth.getBytes());
                            httpURLConnection.getOutputStream().write(sex.getBytes());
                            httpURLConnection.getOutputStream().write(phone.getBytes());
                            httpURLConnection.getOutputStream().write(address.getBytes());
                            httpURLConnection.getOutputStream().write(email.getBytes());

                            if (httpURLConnection.getResponseCode() == 200) {
                                // Success
                                BufferedReader in =
                                        new BufferedReader(
                                                new InputStreamReader(
                                                        httpURLConnection.getInputStream()));
                                StringBuffer buffer = new StringBuffer();
                                String temp = null;
                                while((temp = in.readLine())!=null)
                                    buffer.append(temp);

                                Gson gson = new Gson();
                                final String result = gson.fromJson(buffer.toString(), String.class);
                                Log.d("아이유",result);
                                if(result.equals("1")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Context context = getApplicationContext();
                                            CharSequence text = "회원 등록 성공";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                            toast.setGravity(Gravity.TOP|Gravity.LEFT, 500, 0);
                                            finish();
                                        }
                                    });
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Context context = getApplicationContext();
                                            CharSequence text = "회원 등록 실패";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, text, duration);
                                            toast.show();
                                            toast.setGravity(Gravity.TOP|Gravity.LEFT, 500, 0);
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

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initRefs();
        setEvents();

        handler = new Handler();
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_result.setText(String.format("%s %s (%s)", arg1, arg2, arg3));
                    member_address_01.setText(String.format("%s", arg1));
                    member_address_02.setText(String.format("%s (%s)", arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                }
            });
        }
    }
}
