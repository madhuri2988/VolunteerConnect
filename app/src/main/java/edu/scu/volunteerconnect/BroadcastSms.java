package edu.scu.volunteerconnect;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BroadcastSms extends AppCompatActivity {
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/volunteer/";
    //  https://burning-torch-6090.firebaseio.com/demo/volunteer/fun%20event
    Firebase firebaseRoot;
    String event;
    String msg;
    List number;
    String n;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_sms);

        message=(EditText)findViewById(R.id.message);
        Firebase.setAndroidContext(getApplicationContext());
        firebaseRoot = new Firebase(FIREBASE_URL);

        final android.widget.Spinner spinner = (android.widget.Spinner) findViewById(R.id.spinnerevent);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.events, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        event = spinner.getSelectedItem().toString(); //get eventname
        msg = message.getText().toString(); //get message
        number= new ArrayList();

        firebaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                n = snapshot.child(event).child("phone").getValue().toString(); //get numbers
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Button sendsms = (Button)findViewById(R.id.sendsms);
        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(n!=null) {
                    n = n.substring(1, n.length() - 1);

                }

                String phoneNumber = "3027660587";//,"4086018689"};
                String[] p = n.split(",");//{"3027660587", "4086018689"};
                String [] n = new String[p.length];

                for(int j=0;j<p.length;j++){
                    n= p[j].split("=");
                    number.add(j,n[1]);

                }
                String smsBody = message.getText().toString();
                if(smsBody==null){
                    smsBody="Thank you";
                }

                String SMS_SENT = "SMS_SENT";
                String SMS_DELIVERED = "SMS_DELIVERED";
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                    return;

                }

                for (int i = 0; i < number.size(); i++) {

                    PendingIntent sentPendingIntent =
                            PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_SENT), 0);
                    PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(SMS_DELIVERED), 0);

// For when the SMS has been sent
                    registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                    Toast.makeText(context, "Generic failure cause", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_NO_SERVICE:
                                    Toast.makeText(context, "Service is currently unavailable", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_NULL_PDU:
                                    Toast.makeText(context, "No pdu provided", Toast.LENGTH_SHORT).show();
                                    break;
                                case SmsManager.RESULT_ERROR_RADIO_OFF:
                                    Toast.makeText(context, "Radio was explicitly turned off", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }, new IntentFilter(SMS_SENT));

// For when the SMS has been delivered
                    registerReceiver(new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();

                                    break;
                                case Activity.RESULT_CANCELED:
                                    Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }, new IntentFilter(SMS_DELIVERED));

// Get the default instance of SmsManager
                    SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS

                    smsManager.sendTextMessage(number.get(i).toString(), null, smsBody, sentPendingIntent, deliveredPendingIntent);
                }
            }
        });

    }
    private  void requestCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
        {            // you can show dialog here for grant permission (camera) and handle dialog event according to your need
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);

        }
        else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }
}
