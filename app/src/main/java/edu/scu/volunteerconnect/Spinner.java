package edu.scu.volunteerconnect;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Priyanka on 1/29/2016.
 */
public class Spinner extends Activity implements AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}