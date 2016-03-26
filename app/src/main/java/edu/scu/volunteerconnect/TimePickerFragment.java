package edu.scu.volunteerconnect;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Priyanka on 2/13/2016.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    String s;
    public static final int FLAG_START_Time = 0;
    public static final int FLAG_END_Time = 1;

    private int flag = 11;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    public void setFlag(int i) {
        flag = i;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
if(flag == FLAG_START_Time){
    ((EditText) getActivity().findViewById(R.id.fromtime)).setText( hourOfDay+ " : "+ minute);

}else if (flag == FLAG_END_Time){

    ((EditText) getActivity().findViewById(R.id.totime)).setText(hourOfDay + " : " + minute);
}
    }
}