package edu.scu.volunteerconnect;


import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ADDEVENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button volunteer = (Button) findViewById(R.id.Volunteer);
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VolunteerHomePage.class);
                intent.putExtra("hi", "login");
                startActivity(intent);//.startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });

        Button organizer = (Button) findViewById(R.id.Organizer);
        organizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("hi", "login");
                startActivity(intent);//.startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.uninstall:
                Uri packageURI = Uri.parse("package:edu.scu.volunteerconnect");
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);

                //   packageURI);
                startActivity(uninstallIntent);
                break;


        }
        return true;
    }
}
