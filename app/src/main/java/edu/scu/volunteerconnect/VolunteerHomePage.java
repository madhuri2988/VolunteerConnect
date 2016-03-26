package edu.scu.volunteerconnect;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class VolunteerHomePage extends Activity {
    GridView gridview;
    private ArrayList<String> listCategory;
    Integer[] smallImages = {
            R.drawable.children,R.drawable.disabled,R.drawable.education,
            R.drawable.envi,R.drawable.homeless,R.drawable.community
    };

    Bundle myOriginalMemoryBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home_page);


        prepareList();
        myOriginalMemoryBundle = savedInstanceState;
        // setup GridView with its custom adapter and listener
        gridview = (GridView) findViewById(R.id.gridView);

        gridview.setAdapter(new GridViewAdapter(this, smallImages, listCategory));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplicationContext(),"You clicked"+listCategory.get(position)+" category",Toast.LENGTH_SHORT);
                toast.show();

                Intent loginIntent = new Intent(getApplicationContext(),ListActivity.class);
                loginIntent.putExtra("category",listCategory.get(position));
                startActivity(loginIntent);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.location_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.location, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }


    public void prepareList() {
        listCategory = new ArrayList<String>();


        listCategory.add("Children");
        listCategory.add("Disabled");
        listCategory.add("Education");
        listCategory.add("Environment");
        listCategory.add("Homeless");
        listCategory.add("Community");
    }
}
