package id.ac.umn.simplymeal.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import id.ac.umn.simplymeal.MainActivity;
import id.ac.umn.simplymeal.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity  {
    private EditText usrEmail, usrPass;
    private Button btn_login, btn_register;
    private TextView forgotPass;
    private DatabaseReference refs;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Users user;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pd = new ProgressDialog(LoginActivity.this);

        user = new Users();
        refs = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        usrEmail = (EditText)findViewById(R.id.login_email);
        usrPass = (EditText)findViewById(R.id.login_pass);

        btn_login =  (Button)findViewById(R.id.btnLogin);
        btn_register =  (Button)findViewById(R.id.to_Register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedStore();

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisScreen = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regisScreen);
            }
        });
        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotScreen = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(forgotScreen);
            }
        });

    }

    private void sharedStore() {

        usrEmail.setError(null);

        usrPass.setError(null);
        View fokus = null;
        boolean cancel = false;

        
        String email = usrEmail.getText().toString();
        String pass = usrPass.getText().toString();
        String cekEmail="", cekPass= "";
        if (TextUtils.isEmpty(email)){
            usrEmail.setError("This field is required");
            fokus =  usrEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(pass)){
            usrPass.setError("This field is required");
            fokus = usrPass;
            cancel = true;
        }

        if (cancel) fokus.requestFocus();
        else homeScreen(email, pass);

    }

    private void homeScreen(final String email, final String pass) {
        pd.setMessage("Logging In...");
        pd.show();
        pd.setCancelable(false);
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                }
                else{
                    Preferences.setLoggedInEmail(getBaseContext(),email.replace(".",","));
                    Preferences.setLoggedInStatus(getBaseContext(),true);
                    finish();
                    pd.dismiss();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        });

    }



}
