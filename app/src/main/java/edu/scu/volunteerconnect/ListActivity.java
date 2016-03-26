package edu.scu.volunteerconnect;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.Menu;
        import android.view.MenuItem;
        import com.firebase.client.Query;
        import com.firebase.client.Firebase;
        import com.firebase.client.ValueEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.FirebaseError;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;


public class ListActivity extends ActionBarActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private ArrayList<Event> mAdapterItems;
    private ArrayList<String> mAdapterKeys;
    String category;

     final String FIREBASE_URL="https://burning-torch-6090.firebaseio.com/demo/events";
    Firebase firebaseRoot;
    private Query mQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Firebase.setAndroidContext(this);
        firebaseRoot = new Firebase(FIREBASE_URL);
Bundle b=getIntent().getExtras();
        if(b!=null){
            category=b.getString("category");
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapterItems = new ArrayList<Event>();
        mAdapterKeys = new ArrayList<String>();
        mQuery=firebaseRoot;

        mAdapter = new CardAdapter(mQuery, Event.class, mAdapterItems, mAdapterKeys,category);

        mRecyclerView.setAdapter(mAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.mapView:
                Intent mapsIntent = new Intent(getApplicationContext(),MapsActivity.class);
mapsIntent.putExtra("category",category);

                startActivity(mapsIntent);
                break;


        }
        return true;
    }

}
