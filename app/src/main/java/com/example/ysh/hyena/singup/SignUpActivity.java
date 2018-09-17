package com.example.ysh.hyena.singup;

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

public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private TextInputLayout tilRegisterEmail;
    private TextInputLayout tilRegisterPassword;
    private TextInputLayout tilRegisterPassword2;

    private Button btnComplete;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reigster);

        tilRegisterEmail = findViewById(R.id.til_register_email);
        tilRegisterPassword = findViewById(R.id.til_register_password);
        tilRegisterPassword2 = findViewById(R.id.til_register_password2);

        btnComplete = findViewById(R.id.btn_register_register);
        firebaseauth = FirebaseAuth.getInstance();
    }

    public void onClick_register(View view) {
        String email = tilRegisterEmail.getEditText().getText().toString().trim();
        String password = tilRegisterPassword.getEditText().getText().toString();
        String password2 = tilRegisterPassword2.getEditText().getText().toString();

        if(!password.equals(password2)) {
            tilRegisterPassword2.setError("비밀번호가 일치하지 않습니다.");
            Toast.makeText(SignUpActivity.this,
                    "1번은" + tilRegisterPassword.getEditText().getText().toString()
                            + "2번은" + tilRegisterPassword2.getEditText().getText().toString(),
                    Toast.LENGTH_SHORT).show();
        } else {
            tilRegisterPassword2.setError(null);
            if((validateEmail() && validatePassword() && validatePassword2()) == true) {
                createUser(email, password);
            }
        }
    }

    // 이메일 유효성 검사
    private boolean validateEmail() {
        String emailInput = tilRegisterEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            tilRegisterEmail.setError("이메일을 입력해주세요");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            tilRegisterEmail.setError(getString(R.string.err_message_invaild_email));
            return false;
        } else {
            tilRegisterEmail.setError(null);
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean validatePassword() {
        String passwordInput = tilRegisterPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            tilRegisterPassword.setError("비밀번호를 입력해주세요");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            tilRegisterPassword.setError("비밀번호는 영문과 숫자를 혼합하고 \n특수문자와 공백을 제외한 6~12자리 입니다.");
            return false;
        } else {
            tilRegisterPassword.setError(null);
            return true;
        }
    }

    // 비밀번호2 유효성 검사
    private boolean validatePassword2() {
        String passwordInput2 = tilRegisterPassword2.getEditText().getText().toString().trim();
        if (passwordInput2.isEmpty()) {
            tilRegisterPassword2.setError("비밀번호를 입력해주세요");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput2).matches()) {
            tilRegisterPassword2.setError("비밀번호는 특수문자와 공백을 제외한 6~12자리 입니다.");
            return false;
        } else {
            tilRegisterPassword2.setError(null);
            return true;
        }

    }

    private void createUser(String email, String password) {
        firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "회원가입 실패.  \n이미 존재하는 아이디 입니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
