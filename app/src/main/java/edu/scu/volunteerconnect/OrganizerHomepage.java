package edu.scu.volunteerconnect;


        import android.app.Notification;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.support.v4.app.NotificationCompat;

        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
//import com.google.android.gms.common.data.DataBufferUtils;

public class OrganizerHomepage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/volunteer/";

    Firebase firebaseRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_homepage);
        firebaseRoot = new Firebase(FIREBASE_URL);
        final int notificationId = 1111;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        firebaseRoot.addValueEventListener(new ValueEventListener() {
            String ee;

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SharedPreferences prefs = getSharedPreferences("My_pref",MODE_PRIVATE);
                String e = prefs.getString("evenTitle", "Volunteer Notification");
                //  if(event!=null)
                int requestCode = 2222;
                Intent resultIntent = new Intent(getApplicationContext(), OrganizerHomepage.class);
                //  resultIntent.setAction(a);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(getApplicationContext(), requestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.vc)
                                .setContentTitle("Event Title: "+e)
                                .setContentText("New volunteer joined!")
                                .setContentIntent(resultPendingIntent)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_SOUND);


                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify(notificationId, mBuilder.build());
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
                         //Run service and listen


                    Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                    startActivity(intent);


            // Handle the camera action
        } else if (id == R.id.nav_list) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            intent.putExtra("category","Children");
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), BroadcastSms.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

