package iss.ca.androidca;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    Context context;
    ArrayList<imageModel> imageList;
    ArrayList<imageModel> questionList;
    int imageId;
    public ImageAdapter(Context context, ArrayList<imageModel> imageList, ArrayList<imageModel> questionList) {
        this.context = context;
        this.imageList = imageList;
        this.questionList = questionList;
        this.imageId = imageId;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }
    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_list, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageViewerQuestion(imageView, position);

        ImageView imageView2 = (ImageView) convertView.findViewById(R.id.image2);
        imageViewerImages(imageView2, position);

        return convertView;
    }

    public void imageViewerQuestion(ImageView imageView, int position){
        imageView.setImageResource(questionList.get(position).getmThumbIds());
        imageView.setPadding(15,25,15,25);
        imageView.setVisibility(View.INVISIBLE);
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
            }
        }, 5000);
        imageView.setAdjustViewBounds(true);
    }

    public void imageViewerImages(ImageView imageView2, int position){
        imageView2.setImageBitmap(imageList.get(position).getBitmap());
        imageView2.setPadding(15,25,15,25);
        imageView2.setVisibility(View.VISIBLE);
        imageView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView2.setVisibility(View.INVISIBLE);
            }
        }, 5000);
        imageView2.setAdjustViewBounds(true);
    }

}