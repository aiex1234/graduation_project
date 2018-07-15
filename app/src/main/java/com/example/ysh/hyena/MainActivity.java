package com.example.ysh.hyena;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         // 최소 1개의  숫자
                    //"(?=.*[a-z])" +         // 최소 1개의 소문자
                    //"(?=.*[A-Z])" +         // 최소 1개의 대문자
                    //"(?=.*[@#$%^&+=])" +    // 최소 1개의 특수문자
                    "(?=.*[a-zA-Z])" +        // 모든 문자
                    "(?=\\S+$)" +             // 공백불가능
                    ".{6,12}" +               // 6~12자리의 비밀번호
                    "$");

    TextInputLayout til_email;
    TextInputLayout til_password;
    TextInputEditText et_email;
    TextInputEditText et_password;

    Button btn_login;
    Button btn_register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReigsterActivity.class);
                startActivity(intent);
            }
        });
    }

    // 이메일 유효성 검사
    private boolean validateEmail() {
        String emailInput = til_email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            til_email.setError("이메일을 입력해주세요");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            til_email.setError("이메일 형식이 아닙니다");
            return false;
        } else {
            til_email.setError(null);
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean validatePassword() {
        String passwordInput = til_password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            til_password.setError("비밀번호를 입력해주세요");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            til_password.setError("비밀번호는 특수문자와 공백을 제외한 6~12자리 입니다.");
            return false;
        } else {
            til_password.setError(null);
            return true;
        }
    }

}
