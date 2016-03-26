package edu.scu.volunteerconnect;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.provider.CalendarContract;
        import android.support.design.widget.Snackbar;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Base64;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
import com.firebase.client.Firebase;
        import android.view.View;
        import android.text.SpannableStringBuilder;

        import java.io.InputStream;
        import java.text.*;
        import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity {
    ImageView detailImage;

    TextView tvLink;
    TextView tvDetails;

    Event event;
    TextView tvLocation;
    TextView call;
    TextView eventCal;
    Firebase firebaseRoot;
    static int count =0 ;
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/volunteer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        event = (Event) getIntent().getSerializableExtra("event");

        getSupportActionBar().setTitle(event.getEventTitle());
        detailImage = (ImageView) findViewById(R.id.detailImage);
        tvLink = (TextView) findViewById(R.id.tvLink);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        call = (TextView) findViewById(R.id.callText);
        eventCal = (TextView) findViewById(R.id.tvCalendar);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        call.setText(event.getPhoneNumber());
        eventCal.setText(event.getDate() + " Time " + event.getStartTime() + " to " + event.getEndTime());

        tvLocation.setText(event.getAddress() + "," + event.getCountry() + "," + event.getZipcode());


        String image64 = event.getImaglebase64();
        byte[] decodedByte = Base64.decode(image64, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

        detailImage.setImageBitmap(bitmap);


        tvDetails.setText(event.getEvenDetails());
        tvLink.setText(event.getOrganLink());

        SpannableStringBuilder snackBarText = new SpannableStringBuilder();

        int bold = snackBarText.length();
        snackBarText.append("Interested?? ");

        final View coordinatorLayout = findViewById(R.id.coordinatedLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackBarText, Snackbar.LENGTH_INDEFINITE)
                .setAction("Add!!", clickListener);
        snackbar.setActionTextColor(Color.parseColor("#40c3ff"));

        snackbar.show();
    }

    final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addCalendarEvent();
addVolunteerToEvent(event.getEventTitle());

        }
    };

    public void click(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.tvLink: // R.id.textView1
                String myUriString = "http://"+event.getOrganLink();

                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(myUriString));
                break;
            case R.id.tvLocation: // R.id.textView2
                String thePlace = event.getAddress() + "," + event.getCountry();
                intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=(" + thePlace + ")"));
                break;
            case R.id.callText:
                String myPhoneNumberUri = "tel:" + event.getPhoneNumber();
                intent = new Intent(Intent.ACTION_CALL,
                        Uri.parse(myPhoneNumberUri));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    requestCameraPermission();
                    return;
                }
                break;
            default: return;

        }
        startActivity(intent);
    }
    private  void requestCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
        {            // you can show dialog here for grant permission (camera) and handle dialog event according to your need
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);

        }
        else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        }
    }
    private void addVolunteerToEvent(final String eventTitle) {
        firebaseRoot = new Firebase(FIREBASE_URL);
        count= count+1;

        Firebase eventTitleref = firebaseRoot.child(eventTitle);
        Map<String, Object> volunteercount = new HashMap<String, Object>();
        volunteercount.put("volunteer", (Integer) count);
        eventTitleref.updateChildren(volunteercount);

        SharedPreferences.Editor editor = getSharedPreferences("My_pref",MODE_PRIVATE).edit();
        editor.putString("evenTitle",eventTitle);

        editor.commit();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String e = prefs.getString("Details --evenTitle", "Volunteer Notification");


    }
    public void addCalendarEvent(){

        String evDate=event.getDate();
        String[] dates=evDate.split("/");
        Calendar beginCal = Calendar.getInstance();
        int year=Integer.parseInt(dates[0]);
        int month=Integer.parseInt(dates[1]);
        int day=Integer.parseInt(dates[2]);

        String[] start=event.getStartTime().split(":");
        String[] end=event.getEndTime().split(":");

        int startHr=Integer.parseInt(start[0].trim());
        int startMin=Integer.parseInt(start[1].trim());
        int endHr=Integer.parseInt(end[0].trim());
        int endMin=Integer.parseInt(end[1].trim());
        beginCal.set(year,month,day,startHr, startMin);


        Calendar endCal = Calendar.getInstance();
        endCal.set(year,month,day, endHr, endMin);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis());

        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endCal.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.TITLE, event.getEventTitle());

        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddress());
        startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "I am attending this event, Would you like to join!!?? \n Event Name: "+event.getEventTitle()+" Date: " + event.getDate()+"Organization Link: "+event.getOrganLink()+" -Sent from VC ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, event.getEventTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


                break;

            default:
                Toast.makeText(getApplicationContext(), "Unknown action", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
