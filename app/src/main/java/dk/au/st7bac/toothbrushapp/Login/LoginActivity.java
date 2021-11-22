package dk.au.st7bac.toothbrushapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.au.st7bac.toothbrushapp.MainActivity;
import dk.au.st7bac.toothbrushapp.R;

public class LoginActivity extends AppCompatActivity {

    //https://www.youtube.com/watch?v=HYzw8LFvmw4
    //https://www.youtube.com/watch?v=xJ_6eMKpVLQ
    //https://www.youtube.com/watch?v=ItqsK5uhLBg


    private Button register, login;

    private EditText email, password;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.RegisterButton);

        email = findViewById(R.id.EmailEditTextLogin);
        password = findViewById(R.id.PasswordEditTextLogin);
        login = findViewById(R.id.loginButton);


        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                //loginUser(txt_email, txt_password);

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(StartActivity.this, StartActivity.class));
                    //finish();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });
    }

    private void loginUser (String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                FirebaseUser user = auth.getCurrentUser();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }


    /*
    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(StartActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        });
    }

     */






}