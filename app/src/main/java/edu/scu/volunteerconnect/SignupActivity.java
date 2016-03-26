package edu.scu.volunteerconnect;


        import android.app.ProgressDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.firebase.client.AuthData;
        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    EditText nameText;
    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;

    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/";
    Firebase firebaseRoot;int count =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        firebaseRoot = new Firebase(FIREBASE_URL);

        nameText = (EditText)findViewById(R.id.input_name);
        emailText = (EditText)findViewById(R.id.input_email);
        passwordText = (EditText)findViewById(R.id.input_password);
        signupButton= (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);



        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }
    public void signup() {


        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        count++;
        User user = new User(name, email,password);
        Log.d("URL:", FIREBASE_URL);
        Firebase user1 = firebaseRoot.child("users");//.child("user"+count);
        user1.push().setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                Toast.makeText(getBaseContext(), "Account created. Congratulations!", Toast.LENGTH_LONG).show();

            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

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
    private void showData(String tag, DataSnapshot dataSnapshot) {
        String value;
        if (dataSnapshot.getValue() == null )
            value = "null";
        else
            value = dataSnapshot.getValue().toString();
        Log.d("o/p",tag + " : " + value + "\n");
    }

}
