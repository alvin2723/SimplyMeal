package id.ac.umn.simplymeal.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import id.ac.umn.simplymeal.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {
    private EditText emailReset;
    private Button resetPasswordBtn, backLogin;
    private FirebaseAuth mAuth;
    private String resetEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        emailReset = findViewById(R.id.email_text);
        resetPasswordBtn = findViewById(R.id.reset_pass_btn);

        backLogin = findViewById(R.id.back_btn);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String resetEmail = emailReset.getText().toString().trim();
                if(TextUtils.isEmpty(resetEmail)){
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(resetEmail).addOnCompleteListener(ForgotPassActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassActivity.this, "Reset Link Sent to Your Email",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(ForgotPassActivity.this, "Unable to Send Reset Email",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
