package iss.ca.androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WebBrowser extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    Button beginGame;
    Button fetchBtn;
    EditText fetchUrl;
    ProgressBar bar;
    GridView simpleGrid;
    List<Bitmap> bitmaps;
    boolean running = false;
    ArrayList<PositionClass> positions;
    boolean imgClick;
    String[] filenames = { "1.jpg", "2.jpg", "3.jpg" , "4.jpg", "5.jpg", "6.jpg" , "7.jpg", "8.jpg", "9.jpg" , "10.jpg", "11.jpg", "12.jpg" , "13.jpg", "14.jpg", "15.jpg" , "16.jpg", "17.jpg", "18.jpg", "19.jpg", "20.jpg" };

    protected BroadcastReceiver receiver = new BroadcastReceiver()
    {


        @Override
        public void onReceive(Context context, Intent intent) {
            int num = intent.getIntExtra("num",0);
            String action = intent.getAction();

            if (action.equals("download_ok"))
            {
                bitmaps.add(createBitmap(filenames[num-1]));

                setProgressBar(num);
                setProgressText(num);

                TextView textBar = findViewById(R.id.progressText);
                textBar.setVisibility(View.VISIBLE);

                ProgressBar progressBar = findViewById(R.id.progressbar);
                progressBar.setVisibility(View.VISIBLE);

                testing(bitmaps);
            }

            if(num==20 & action.equals("download_ok")){

               stopService(new Intent(WebBrowser.this, DownloadService.class));
                Button button = findViewById(R.id.fetchBtn);
                if (button.getText().equals("stop")){
                    button.setText("fetch");
                }
                TextView textBar = findViewById(R.id.progressText);
                textBar.setVisibility(View.INVISIBLE);

                ProgressBar progressBar = findViewById(R.id.progressbar);
                progressBar.setVisibility(View.INVISIBLE);

                running = false;

                imgClick = true;
            }

        }
    };

    public void testing(List<Bitmap> bitmaps){
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), bitmaps);
        simpleGrid.setAdapter(customAdapter);
        simpleGrid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(imgClick){
            for(int i=0;i<positions.size();i++)
            {
                PositionClass positionClass = positions.get(i);
                int Posid = positionClass.getId();
                int select = positionClass.getSelect();
                if(Posid == position){
                    if(select==0){
                        select = 1;
                        positionClass.setSelect(select);
                        ImageView imageView = view.findViewById(R.id.icon);
                        imageView.setAlpha(0.1f);
                        break;
                    }
                    else{
                        select = 0;
                        positionClass.setSelect(select);
                        ImageView imageView = view.findViewById(R.id.icon);
                        imageView.setAlpha(1.0f);
                        break;
                    }
                }

            }
        }
    }

    protected Bitmap createBitmap(String filename) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, filename);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        return bitmap;
    }

    public void setProgressText(int num) {
        TextView textView = findViewById(R.id.progressText);
        textView.setText("Downloading " + num + " of 20 images...");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        beginGame = findViewById(R.id.btnBegin);
        beginGame.setOnClickListener(this);

        simpleGrid = (GridView) findViewById(R.id.gridview);

        TextView textBar = findViewById(R.id.progressText);
        textBar.setVisibility(View.INVISIBLE);

        ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        bar = findViewById(R.id.progressbar);
        IntentFilter filter = new IntentFilter();
        filter.addAction("download_ok");
        registerReceiver(receiver, filter);


        fetchBtn = findViewById(R.id.fetchBtn);
        if(fetchBtn!=null){
            fetchBtn.setOnClickListener(this);
        }
    }

    public void setProgressBar(int num) {
        bar.setProgress(num);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.fetchBtn){
            resetPositions();
            hideSoftKeyboard(v);
            imgClick = false;

            fetchUrl = findViewById(R.id.fetchUrl);
            String url = fetchUrl.getText().toString();

            if(!checkURL(url)){
                Toast toast = Toast.makeText(this, "URL INVALID", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            fetchUrl.onEditorAction(EditorInfo.IME_ACTION_DONE);

            Button button = findViewById(R.id.fetchBtn);
            if (button.getText().equals("stop")){
                button.setText("fetch");
                //simpleGrid.removeAllViewsInLayout();
            }


            if (running) {
                reset();

                try {
                    Thread newThread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            unregisterReceiver(receiver);

                            IntentFilter filter = new IntentFilter();
                            filter.addAction("download_ok");
                            registerReceiver(receiver, filter);
                            running = !running;


                        }
                    });
                    Thread.sleep(2000);
                    newThread.start();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }

            }

            if (!running) {
                defaultSetup();
                bitmaps = new ArrayList<Bitmap>();
                bitmaps.clear();


                Toast toast = Toast.makeText(this, "downloading start", Toast.LENGTH_LONG);
                toast.show();
                running = true;
                startDownloadService(url);

            }
        }

        if(id==R.id.btnBegin){
            int amtList = getAmount(positions);
            if(amtList==6){
                ArrayList<Integer> selectedPictures = selectedPictures(positions);
                Intent intent = new Intent(this,GameScreen.class);
                intent.setAction("startGame");
                intent.putExtra("pos1", selectedPictures.get(0));
                intent.putExtra("pos2", selectedPictures.get(1));
                intent.putExtra("pos3", selectedPictures.get(2));
                intent.putExtra("pos4", selectedPictures.get(3));
                intent.putExtra("pos5", selectedPictures.get(4));
                intent.putExtra("pos6", selectedPictures.get(5));

                startActivity(intent);
            }
            else if(amtList>6){
                Toast toast = Toast.makeText(this,"You have selected more than 6 images",Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(amtList<6){
                Toast toast = Toast.makeText(this,"You need to select 6 images",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void startDownloadService(String url)
    {
        Button button = findViewById(R.id.fetchBtn);
        if (button.getText().equals("fetch")){
            button.setText("stop");
        }

        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction("download");
        intent.putExtra("url", url);
        startService(intent);


    }

    public void reset(){
        Intent intent = new Intent (this, DownloadService.class);
        stopService(intent);

    }

    public void defaultSetup(){
        simpleGrid.removeAllViewsInLayout();
        setProgressText(0);
        setProgressBar(0);
    }

    public void resetPositions()
    {
        positions = new ArrayList<PositionClass>();
        positions.add(new PositionClass(0));
        positions.add(new PositionClass(1));
        positions.add(new PositionClass(2));
        positions.add(new PositionClass(3));
        positions.add(new PositionClass(4));
        positions.add(new PositionClass(5));
        positions.add(new PositionClass(6));
        positions.add(new PositionClass(7));
        positions.add(new PositionClass(8));
        positions.add(new PositionClass(9));
        positions.add(new PositionClass(10));
        positions.add(new PositionClass(11));
        positions.add(new PositionClass(12));
        positions.add(new PositionClass(13));
        positions.add(new PositionClass(14));
        positions.add(new PositionClass(15));
        positions.add(new PositionClass(16));
        positions.add(new PositionClass(17));
        positions.add(new PositionClass(18));
        positions.add(new PositionClass(19));


    }

    public int getAmount(ArrayList<PositionClass> positions){
        int count = 0;
        for(int i=0;i<positions.size();i++)
        {
            PositionClass positionClass = positions.get(i);
            int select = positionClass.getSelect();
            if(select==1){
                count++;
            }

        }return count;
    }

    public ArrayList<Integer> selectedPictures(ArrayList<PositionClass> positions){
        ArrayList<Integer> count = new ArrayList<Integer>();
        for(int i=0;i<positions.size();i++)
        {
            PositionClass positionClass = positions.get(i);
            int Posid = positionClass.getId();
            int select = positionClass.getSelect();
            if(select==1){
                count.add(Posid);
            }

        }return count;
    }

    public boolean checkURL(String url){
        boolean check = false;
        String front = "";
        if(url.length()<5){
            check = false;
        }
        else{
            front = url.substring(0,5);
            if(front.equals("https")){
                check = true;
            }
        }

        return check;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
