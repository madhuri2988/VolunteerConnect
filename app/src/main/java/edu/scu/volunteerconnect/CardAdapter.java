package edu.scu.volunteerconnect;

/**
 * Created by MadhuriKambalapalli on 1/31/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CardAdapter extends FirebaseRecyclerAdapter<CardAdapter.ViewHolder,Event> {

    List<Event> mItems;
    private Context context;
    String[] dates;
    int year;
    int month;
    int day;
    Bitmap bitmap;
   public CardAdapter(Query query, Class<Event> itemClass, @Nullable ArrayList<Event> items,
                    @Nullable ArrayList<String> keys,String category) {
       super(query, itemClass, items, keys,category);
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view, viewGroup, false);
        context=viewGroup.getContext();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final Event event=getItem(i);


        String eventDates=event.getDate();
        dates=eventDates.split("/");

        year=Integer.parseInt(dates[0]);
        month=Integer.parseInt(dates[1]);
        day=Integer.parseInt(dates[2]);

        viewHolder.tvDesc.setText(event.getEventTitle());
        viewHolder.evDateMonth.setText("Feb");

        String monthString = new DateFormatSymbols().getMonths()[month];
        viewHolder.evDateMonth.setText(monthString.substring(0,3));

       viewHolder.evDateDay.setText(""+day);

        String image64=event.getImaglebase64();
        byte[] decodedByte = Base64.decode(image64, 0);

        if(decodedByte!=null) {
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        }

        //getLatLong(event.getAddress()+","+event.getCountry()+","+event.getZipcode());
        viewHolder.imgThumbnail.setImageBitmap(bitmap);


        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    {


                        String[] start=event.getStartTime().split(":");
                        String[] end=event.getEndTime().split(":");

                        int startHr=Integer.parseInt(start[0].trim());
                        int startMin=Integer.parseInt(start[1].trim());
                        int endHr=Integer.parseInt(end[0].trim());
                        int endMin=Integer.parseInt(end[1].trim());

                        Calendar beginCal = Calendar.getInstance();
                        beginCal.set(year,month,day,startHr, startMin);


                        Calendar endCal = Calendar.getInstance();
                        endCal.set(year,month,day, endHr, endMin);

                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginCal.getTimeInMillis());

                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endCal.getTimeInMillis());
                        intent.putExtra(CalendarContract.Events.TITLE, event.getEventTitle());
                        //intent.putExtra(CalendarContract.Events.DESCRIPTION, event.getEvenDetails());
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, event.getAddress());
                        context.startActivity(intent);

                    }

                } else {
                    Intent iint = new Intent(context, EventDetailsActivity.class);
                    iint.putExtra("event", event);
                    context.startActivity(iint);


                }
            }
        });
    }
   /*
*/


   public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

       public ImageView imgThumbnail;
       public TextView tvEvent;
       public TextView tvDesc;
       public TextView evDateMonth;
       public TextView evDateDay;
       private ItemClickListener clickListener;

       public ViewHolder(View itemView) {
           super(itemView);
           imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
           tvEvent = (TextView) itemView.findViewById(R.id.tv_event);
           tvDesc = (TextView) itemView.findViewById(R.id.tv_des_event);
           evDateMonth = (TextView) itemView.findViewById(R.id.evDateMonth);
           evDateDay = (TextView) itemView.findViewById(R.id.evDateDay);

           itemView.setOnClickListener(this);
           itemView.setOnLongClickListener(this);
       }

       public void setClickListener(ItemClickListener itemClickListener) {
           this.clickListener = itemClickListener;
       }

       @Override
       public void onClick(View view) {
           clickListener.onClick(view, getPosition(), false);
       }

       @Override
       public boolean onLongClick(View view) {
           clickListener.onClick(view, getPosition(), true);
           return true;
       }
   }
       @Override
       protected void itemAdded(Event item, String key, int position) {
           Log.d("MyAdapter", "Added a new item to the adapter.");
       }

       @Override
       protected void itemChanged(Event oldItem, Event newItem, String key, int position) {
           Log.d("MyAdapter", "Changed an item.");
       }

       @Override
       protected void itemRemoved(Event item, String key, int position) {
           Log.d("MyAdapter", "Removed an item from the adapter.");
       }

       @Override
       protected void itemMoved(Event item, String key, int oldPosition, int newPosition) {
           Log.d("MyAdapter", "Moved an item.");
       }

}



