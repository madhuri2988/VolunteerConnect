package edu.scu.volunteerconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.ChildrenNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    @JsonIgnoreProperties(ignoreUnknown = true)
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    private static final int REQUEST_SIGNUP = 0;
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/users";
    Firebase firebaseRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Firebase.setAndroidContext(this);
        firebaseRoot = new Firebase(FIREBASE_URL);

        ImageView img = (ImageView) findViewById(R.id.logo);
        img.setImageResource(R.drawable.vc);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        loginButton= (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                login();
            }
        });
        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent,REQUEST_SIGNUP);
            }
        });
    }
    public  void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
      /*  firebaseRoot.authWithPassword(email, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        Toast.makeText(getBaseContext(), "Login ....", Toast.LENGTH_LONG).show();
                        Log.d("name","DDD");
                        Map < String, String > map = new HashMap<String, String>();
                        map.put("provider", authData.getProvider());
                        if (authData.getProviderData().containsKey("emailId")) {
                            map.put("displayName", authData.getProviderData().get("username").toString());
                            Log.d("name",authData.getProviderData().get("username").toString());
                        }
                     //   firebaseRoot.child("users").child(authData.getUid()).setValue(map);
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                    }
                });*/
        firebaseRoot.addValueEventListener(new ValueEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());

                Map<String, Map<String, String>> td = (HashMap<String,Map<String, String>>) snapshot.getValue();
                for (Map.Entry<String,Map<String, String>> entry : td.entrySet()) {

                    Map<String, String> childs = entry.getValue();
                    for (Map.Entry<String,String> entry2 : childs.entrySet()) {
                        if(email.equalsIgnoreCase(entry2.getValue())){
                            Log.e("auth","Authenticated");
                            Intent intent = new Intent(getApplicationContext(), OrganizerHomepage.class);
                            startActivity(intent);
                        }
                        Log.d("**", entry2.getValue());

                    }
                    // ...
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: " ,firebaseError.getMessage());
            }
        });

        new android.os.Handler().
                postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                onLoginSuccess();
                                // onLoginFailed();
                                progressDialog.dismiss();
                            }
                        }

                        , 3000);

    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }
    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}

