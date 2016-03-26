package edu.scu.volunteerconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class AddEventActivity extends AppCompatActivity {

    EditText eventdate;

    EditText evenFromtime,eventTotime;
    public static final int FLAG_START_Time = 0;
    public static final int FLAG_END_Time = 1;
    EditText contactName, title,address,zipcode,orgLink,phoneNumber;
    String country;
    android.widget.Spinner spinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        spinner = (android.widget.Spinner) findViewById(R.id.country);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //get current date and time
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth=mMonth+1;
        eventdate = (EditText) findViewById(R.id.date);
        eventdate.setText(mYear + "/" + mMonth + "/" + mDay);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        evenFromtime = (EditText) findViewById(R.id.fromtime);
        evenFromtime.setText(hour+" : "+minute);

        eventTotime = (EditText) findViewById(R.id.totime);
        eventTotime.setText(hour+" : "+minute);


    }
    protected Event getformvalues(){
        Event e = new Event();
        contactName = (EditText) findViewById(R.id.contact_name);
        title =(EditText) findViewById(R.id.event_title);
        address =(EditText) findViewById(R.id.address);
        zipcode= (EditText) findViewById(R.id.zipcode);
        orgLink =(EditText) findViewById(R.id.reglink);
        phoneNumber =(EditText) findViewById(R.id.contactdetail);
        country = spinner.getSelectedItem().toString();



        e.setAddress(address.getText().toString());
        e.setContactName(contactName.getText().toString());
        e.setEventTitle(title.getText().toString());
        e.setZipcode(zipcode.getText().toString());
        e.setOrganLink(orgLink.getText().toString());
        e.setPhoneNumber(phoneNumber.getText().toString());
        e.setDate(eventdate.getText().toString());
        e.setEndTime(eventTotime.getText().toString());
        e.setStartTime(evenFromtime.getText().toString());
        e.setCountry(country);
        return e;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
// Toast.makeText(getApplicationContext(), "Back Added", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
        builder.setTitle("Canel Event")
                .setMessage("Do you want to proceed?")
                .setPositiveButton("Yes, I do!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent main = new Intent(getApplicationContext(), OrganizerHomepage.class);
                        startActivity(main);
                    }
                })
                .setNegativeButton("No, I don't!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Okay", Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
        ;
        builder.create().show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.next:
                Intent intent = new Intent(this, AddEventActivity2.class);

                Event ee=getformvalues();
                intent.putExtra("event",ee);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment1 = new TimePickerFragment();

        TimePickerFragment t = new TimePickerFragment();
        int id = v.getId();
        if (id == R.id.fromtime) {
            t.setFlag(FLAG_START_Time);
            t.show(getSupportFragmentManager(), "timePicker");
        }
        else {
            t.setFlag(FLAG_END_Time);
            t.show(getSupportFragmentManager(), "timePicker");
        }
    }

}
