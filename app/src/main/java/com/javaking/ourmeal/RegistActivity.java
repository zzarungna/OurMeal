package com.javaking.ourmeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity  extends AppCompatActivity {

    private static final int REQUEST_CODE_INSERT = 1;
    RadioButton radiobtn_male;
    RadioButton radiobtn_female;

    Button btn_member_address;
    Button btn_cancel;
    Button btn_submit;

    EditText member_id;
    EditText member_pw1;
    EditText member_pw2;
    EditText member_name;
    EditText member_birth;
    EditText member_phone;
    EditText member_address;
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
        member_address = findViewById(R.id.member_address);
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
    }

    public void setEvents() {
        btn_member_address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), DaumWebViewActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                if( password2.length() != password1.length() ) {
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

                String address = member_address.getText().toString().trim();
                if( address.length() == 0 ) {
                    Toast.makeText(getApplicationContext(),
                            "ADDRESS 값을 입력해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    member_address_error.setText("ADDRESS 값을 입력해 주세요.");
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
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initRefs();
        setEvents();
    }
}

