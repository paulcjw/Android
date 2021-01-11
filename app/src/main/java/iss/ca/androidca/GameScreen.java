package iss.ca.androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameScreen extends AppCompatActivity
implements AdapterView.OnItemClickListener{
    MediaPlayer player;
    ArrayList<imageModel> arrayList = new ArrayList<imageModel>();
    ArrayList<imageModel> images;
    ArrayList<imageModel> tempList = new ArrayList<imageModel>();  // for card matching
    ArrayList<Integer> positions = new ArrayList<Integer>();
    ImageView[] viewList = new ImageView[2];  // for location tracking of card 1 & 2


    String[] filenames = { "1.jpg", "2.jpg", "3.jpg" , "4.jpg", "5.jpg", "6.jpg" , "7.jpg", "8.jpg", "9.jpg" , "10.jpg", "11.jpg", "12.jpg" , "13.jpg", "14.jpg", "15.jpg" , "16.jpg", "17.jpg", "18.jpg", "19.jpg", "20.jpg" };
    List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    int count = 0;
    int attempts = 0;
    int correctAttempts = 0;
    int wrongAttempts = 0;
    int start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent intent = getIntent();

        if (intent.getAction().equals("startGame")) {
            addImagesIntoArray(intent);
            randomImages();

            inputScreenImages();
        }
        else if (intent.getAction().equals("restart")){
            addImagesIntoArray(intent);
            randomImages();
            inputScreenImages();
        }
        else if (intent.getAction().equals("reselect")){
            Intent intent2 = new Intent (this, WebBrowser.class);
            startActivity(intent2);

        }

    }

    public void randomImages() {
        Random rand = new Random();
        Collections.shuffle(images);
    }

    public void addImagesIntoArray(Intent intent) {

        int pos1 = intent.getIntExtra("pos1",0);
        int pos2 = intent.getIntExtra("pos2",0);
        int pos3 = intent.getIntExtra("pos3",0);
        int pos4 = intent.getIntExtra("pos4",0);
        int pos5 = intent.getIntExtra("pos5",0);
        int pos6 = intent.getIntExtra("pos6",0);

        positions.add(pos1);
        positions.add(pos2);
        positions.add(pos3);
        positions.add(pos4);
        positions.add(pos5);
        positions.add(pos6);

        bitmaps.add(createBitmap(filenames[pos1]));
        bitmaps.add(createBitmap(filenames[pos2]));
        bitmaps.add(createBitmap(filenames[pos3]));
        bitmaps.add(createBitmap(filenames[pos4]));
        bitmaps.add(createBitmap(filenames[pos5]));
        bitmaps.add(createBitmap(filenames[pos6]));

        images = new ArrayList<imageModel>();
        images.add(new imageModel(bitmaps.get(0), 1));
        images.add(new imageModel(bitmaps.get(1), 2));
        images.add(new imageModel(bitmaps.get(2), 3));
        images.add(new imageModel(bitmaps.get(3), 4));
        images.add(new imageModel(bitmaps.get(4), 5));
        images.add(new imageModel(bitmaps.get(5), 6));
        images.add(new imageModel(bitmaps.get(0), 1));
        images.add(new imageModel(bitmaps.get(1), 2));
        images.add(new imageModel(bitmaps.get(2), 3));
        images.add(new imageModel(bitmaps.get(3), 4));
        images.add(new imageModel(bitmaps.get(4), 5));
        images.add(new imageModel(bitmaps.get(5), 6));



    }

    protected Bitmap createBitmap(String filename) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, filename);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }

    public void inputScreenImages(){
        GridView gridView = (GridView) findViewById(R.id.gridView);

        if (arrayList.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                imageModel imagemodel = new imageModel();
                imagemodel.setmThumbIds(R.drawable.question);
                //add in array list
                arrayList.add(imagemodel);
            }
        }
        if (images != null) {
            ImageAdapter adpter = new ImageAdapter(getApplicationContext(), images, arrayList);
            gridView.setAdapter(adpter);
            gridView.setOnItemClickListener(this);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ImageView imageView = view.findViewById(R.id.image);
        ImageView imageView2 = view.findViewById(R.id.image2);
        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.timer);


        if (imageView.getVisibility() == View.VISIBLE) {
            if (start == 0){
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                simpleChronometer.start();
                start++;
            }
            if (count == 0)
            {
                imageView.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.VISIBLE);

                imageModel image = images.get(position);
                tempList.add(image);
                viewList[0] = imageView;
                viewList[1] = imageView2;
                count++;


            }
            else if (count == 1) {
                attempts++;
                count = 0;
                // for card matching
                imageModel image2 = images.get(position);
                imageModel image1 = tempList.get(tempList.size()-1);

                // for location tracking of card 1 & 2
                ImageView temp = viewList[0];
                ImageView temp2 = viewList[1];



                if (image1.getmThumbIds() != image2.getmThumbIds()) { // for card matching
                    player = MediaPlayer.create(this, R.raw.fail);
                    player.start();
                    //                   Toast.makeText(this, "Mismatch!", Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.VISIBLE);
                            imageView2.setVisibility(View.INVISIBLE);
                            temp.setVisibility(View.VISIBLE);
                            temp2.setVisibility(View.INVISIBLE);

                        }
                    }, 1000);
                    wrongAttempts++;
                    tempList.clear();
                }
                else { // for card matching
                    player = MediaPlayer.create(this, R.raw.success);
                    player.start();
                    imageView.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);

                    TextView textView = findViewById(R.id.matches);
                    int number = Integer.parseInt(textView.getText().toString());
                    number++;
                    textView.setText("" + number);
                    //                   count = 0;
                    correctAttempts++;

                    if (number == images.size() / 2) {
                        Toast.makeText(this, "Congratulations! All matched!", Toast.LENGTH_SHORT).show();
                        simpleChronometer.stop();
                        start = 0;
                        player = MediaPlayer.create(this, R.raw.clear);
                        player.start();
                        Intent intent = new Intent(this, ResultPage.class);
                        intent.setAction("completed");
                        intent.putExtra("timeUsed", simpleChronometer.getText().toString());
                        intent.putExtra("correct", String.valueOf(correctAttempts));
                        intent.putExtra("wrong", String.valueOf(wrongAttempts));
                        intent.putExtra("attempts", String.valueOf(attempts));
                        intent.putExtra("pos1", positions.get(0));
                        intent.putExtra("pos2", positions.get(1));
                        intent.putExtra("pos3", positions.get(2));
                        intent.putExtra("pos4", positions.get(3));
                        intent.putExtra("pos5", positions.get(4));
                        intent.putExtra("pos6", positions.get(5));

                        startActivity(intent);

                    }
                }
            }
        }

    }


}