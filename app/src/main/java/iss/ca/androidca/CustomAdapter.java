package iss.ca.androidca;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    @Nullable
    List<Bitmap> bitmaps;
    LayoutInflater inflter;
    //int num;
    public CustomAdapter(Context applicationContext, List<Bitmap> bitmaps) {
        this.context = applicationContext;
        this.bitmaps = bitmaps;
       //this.num = num;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return bitmaps.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view = inflter.inflate(R.layout.activity_grid_view, null);
        }

         // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
        icon.setImageBitmap(bitmaps.get(i)); // set logo images

        //icon.setId(num);
        return view;
    }
}
