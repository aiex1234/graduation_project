package com.example.ysh.hyena.Login_Register;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ysh.hyena.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ReigsterActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private TextInputLayout til_register_email;
    private TextInputLayout til_register_password;
    private TextInputLayout til_register_password2;

    private Button btn_register_register;
    private FirebaseAuth mAuth_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reigster);

        til_register_email = findViewById(R.id.til_register_email);
        til_register_password = findViewById(R.id.til_register_password);
        til_register_password2 = findViewById(R.id.til_register_password2);

        btn_register_register = findViewById(R.id.btn_register_register);
        mAuth_register = FirebaseAuth.getInstance();
    }

    public void onClick_register(View view) {
        String email = til_register_email.getEditText().getText().toString().trim();
        String password = til_register_password.getEditText().getText().toString();
        String password2 = til_register_password2.getEditText().getText().toString();

        if(!password.equals(password2)) {
            til_register_password2.setError("비밀번호가 일치하지 않습니다.");
            Toast.makeText(ReigsterActivity.this,
                    "1번은" + til_register_password.getEditText().getText().toString()
                            + "2번은" + til_register_password2.getEditText().getText().toString(),
                    Toast.LENGTH_SHORT).show();
        } else {
            til_register_password2.setError(null);
            if((validateEmail() && validatePassword() && validatePassword2()) == true) {
                createUser(email, password);
            }
        }
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
            til_register_password.setError("비밀번호는 영문과 숫자를 혼합하고 \n특수문자와 공백을 제외한 6~12자리 입니다.");
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
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(ReigsterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(ReigsterActivity.this, "회원가입 실패.  \n이미 존재하는 아이디 입니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
