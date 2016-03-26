package edu.scu.volunteerconnect;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Priyanka on 1/29/2016.
 */
public class GridViewAdapter extends BaseAdapter {
    private Activity context;	// calling activity context
    Integer[] smallImages;		// thumbnail data set
    private ArrayList<String> listCategory;
    public GridViewAdapter(Activity activity,
                           Integer[] thumbnails, ArrayList<String> listCategory) {
        super();
        context = activity;
        smallImages = thumbnails;
        this.listCategory =listCategory;
    }

    // how many entries are there in the data set
    public int getCount() {
        return smallImages.length;
    }

    // what is in a given 'position' in the data set
    public Object getItem(int position) {
        return smallImages[position];
    }

    // what is the ID of data item in given 'position'
    public long getItemId(int position) {
        return position;
    }



    public class SquareImageView extends ImageView {
        public SquareImageView(Context context) {
            super(context);
        }
        @Override
        public void onMeasure(int width, int height) {
            super.onMeasure(width, width);
        }
    }
    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = context.getLayoutInflater();

        if(convertView==null)
        {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.row_grid, null);
            convertView.setBackgroundResource(R.drawable.gridborder);
            view.txtViewTitle = (TextView) convertView.findViewById(R.id.item_text);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.item_image);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtViewTitle.setText(listCategory.get(position));

        view.imgViewFlag.setImageResource(smallImages[position]);

        return convertView;
    }
}//GridViewAdapter

