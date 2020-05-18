package id.ac.umn.simplymeal.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import id.ac.umn.simplymeal.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity{

    private EditText newusrUsername,newusrEmail, newusrPass, rePass,newusrPhone,newusrFirstName, newusrLastName, codePhone;
    private FirebaseAuth mAuth;
    private Button btn_register;
    private Users userData;
    private FirebaseDatabase databaseUser;
    private DatabaseReference refs;
    private TextView alreadyMember;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pd = new ProgressDialog(RegisterActivity.this);

        mAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance();
        refs = databaseUser.getReference().child("Users");

        userData = new Users();

        alreadyMember = findViewById(R.id.already_member);
        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });
        newusrUsername = (EditText) findViewById(R.id.register_username);
        newusrFirstName = (EditText)findViewById(R.id.register_firstName);
        newusrLastName = (EditText)findViewById(R.id.register_lastName);
        newusrEmail = (EditText) findViewById(R.id.register_email);
        newusrPass = (EditText) findViewById(R.id.register_pass);
        newusrPhone = (EditText) findViewById(R.id.register_phone);
        codePhone = (EditText) findViewById(R.id.codePhone);
        codePhone.setEnabled(false);
        rePass = (EditText) findViewById(R.id.register_repass);
        rePass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    sharedStore();
                    return true;
                }
                return false;
            }
        });

        btn_register = findViewById(R.id.btnRegister);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedStore();

            }
        });
    }
    private  void sharedStore(){

        newusrUsername.setError(null);
        newusrEmail.setError(null);
        newusrPass.setError(null);
        rePass.setError(null);
        View fokus = null;
        boolean cancel = false;

        final String newUsernm = newusrUsername.getText().toString();
        final String newEmail = newusrEmail.getText().toString();
        final String newFirstName = newusrFirstName.getText().toString();
        final String newLastName = newusrLastName.getText().toString();
        final String newNumber = codePhone.getText().toString() + newusrPhone.getText().toString();
        String formatNumber = PhoneNumberUtils.formatNumberToE164(newNumber,"ID");
        final String newPass = newusrPass.getText().toString();
        String rePassword = rePass.getText().toString();

        if(TextUtils.isEmpty(newEmail)){
            newusrEmail.setError("This Field is required!");

            fokus= newusrEmail;
            cancel = true;
        }
        final boolean[] cancel2 = {false};

        if(TextUtils.isEmpty(newUsernm)){
            newusrUsername.setError("This Field is required!");
            fokus= newusrUsername;
            cancel= true;
        }

        refs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(String.valueOf(dataSnapshot.child("userName").getValue()).contains(newUsernm)){
                    newusrUsername.setError("This User Name is Already Exist");
                   newusrUsername.requestFocus();
                    cancel2[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(TextUtils.isEmpty(newPass)){
            newusrPass.setError("This Field is required!");
            fokus = newusrPass;
            cancel = true;
        }
        else if (!cekPass(newPass,rePassword)){
            newusrPass.setError("This Password is Incorrect!");
            fokus = newusrPass;
            cancel = true;
        }

        if(cancel || cancel2[0]){
            fokus.requestFocus();
        }
        else{
            if(newEmail != null && newPass != null){
                pd.setMessage("Please Wait...");
                pd.show();
                pd.setCancelable(false);
                mAuth.createUserWithEmailAndPassword(newEmail,newPass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            pd.dismiss();
                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.i("TAGnoemail", "onComplete: exist_email");

                                // TODO: Take your action
                            }
                            catch (Exception e)
                            {
                                Log.i("get", "onComplete: " + e.getMessage());
                            }
                            Toast.makeText(RegisterActivity.this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent loginScreen = new Intent(RegisterActivity.this, LoginActivity.class);
                            userData.setEmailUser(newEmail);
                            userData.setUserName(newUsernm);
                            userData.setFirstName(newFirstName);
                            userData.setLastName(newLastName);
                            userData.setBirthOfDate("");
                            userData.setAddress("");
                            userData.setGender("");
                            userData.setProfilePhoto("");
                            userData.setPhoneUser(formatNumber);
                            refs.child(newEmail.replace(".",",")).setValue(userData);
                            pd.dismiss();
                            startActivity(loginScreen);
                        }
                    }
                });
            }

        }
    }

    private boolean cekPass(String newPass, String rePassword) {
        return newPass.equals(rePassword);
    }


}
