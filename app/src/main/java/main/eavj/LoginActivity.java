package main.eavj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import main.eavj.ObjectClasses.User;

public class LoginActivity extends Activity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText etEmail, etPassword;
    TextView tvSignUp;
    Button btnLogin;
    ProgressBar progressBar;
    DatabaseReference db;
    ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users");
        etEmail = (EditText) findViewById(R.id.loginEmail);
        etPassword = (EditText) findViewById(R.id.loginPassword);
        tvSignUp = (TextView) findViewById(R.id.loginSignUp);
        btnLogin = (Button) findViewById(R.id.loginBtnLogin);
        progressBar = (ProgressBar) findViewById(R.id.loginProgressBar);
        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    users.add(dsp.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.loginBtnLogin:
            {
                userLogin();
                break;
            }
            case R.id.loginSignUp:
            {
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            }
        }
    }

    private void userLogin() {
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Minimum lenght of password should be 6");
            etPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    String email = mAuth.getCurrentUser().getEmail();
                    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    String role = "";
                    for (User user : users
                         ) {
                        if (user.getUserName().equals(email))
                            role = user.getRole();
                    }
                    if (role.equals("admin")) {
                        startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    }
                   else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
