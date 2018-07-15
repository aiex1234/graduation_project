package com.example.ysh.hyena;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ReigsterActivity extends AppCompatActivity {
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

    TextInputLayout til_register_email;
    TextInputLayout til_register_password;
    TextInputLayout til_register_password2;
    TextInputEditText et_register_email;
    TextInputEditText et_register_password;
    TextInputEditText et_register_password2;

    Button btn_register_register;
    private FirebaseAuth mAuth_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reigster);

        til_register_email = findViewById(R.id.til_register_email);
        til_register_password = findViewById(R.id.til_register_password);
        til_register_password2 = findViewById(R.id.til_register_password2);

        et_register_email = findViewById(R.id.et_register_email);
        et_register_password = findViewById(R.id.et_register_password);
        et_register_password2 = findViewById(R.id.et_register_password2);
        btn_register_register = findViewById(R.id.btn_register_register);
        mAuth_register = FirebaseAuth.getInstance();
    }

    // 이메일 유효성 검사
    private boolean validateEmail() {
        String emailInput = til_register_email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            til_register_email.setError("이메일을 입력해주세요");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            til_register_email.setError("이메일 형식이 아닙니다");
            return false;
        } else {
            til_register_email.setError(null);
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean validatePassword() {
        String passwordInput = til_register_password.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            til_register_password.setError("비밀번호를 입력해주세요");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            til_register_password.setError("비밀번호는 특수문자와 공백을 제외한 6~12자리 입니다.");
            return false;
        } else {
            til_register_password.setError(null);
            return true;
        }
    }

    // 비밀번호2 유효성 검사
    private boolean validatePassword2() {
        String passwordInput2 = til_register_password2.getEditText().getText().toString().trim();
        if (passwordInput2.isEmpty()) {
            til_register_password2.setError("비밀번호를 입력해주세요");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            til_register_password2.setError("비밀번호는 특수문자와 공백을 제외한 6~12자리 입니다.");
            return false;
        } else {
            til_register_password2.setError(null);
            return true;
        }
    }

    private void createUser(String email, String password) {
        mAuth_register.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(validateEmail() && validatePassword() == true){
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth_register.getCurrentUser();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ReigsterActivity.this, "회원가입 실패.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
