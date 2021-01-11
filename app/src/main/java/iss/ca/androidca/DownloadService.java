package iss.ca.androidca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends Service {

    String[] filenames = { "1.jpg", "2.jpg", "3.jpg" , "4.jpg", "5.jpg", "6.jpg" , "7.jpg", "8.jpg", "9.jpg" , "10.jpg", "11.jpg", "12.jpg" , "13.jpg", "14.jpg", "15.jpg" , "16.jpg", "17.jpg", "18.jpg", "19.jpg", "20.jpg" };

    Thread bkgThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        String action = intent.getAction();
        if (action.compareToIgnoreCase("download") == 0) {
             bkgThread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    //String url = "https://stocksnap.io/";
                    try
                    {
                        String url = intent.getStringExtra("url");
                        Document doc = Jsoup.connect(url).get();
                        int i = 1;
                        //String test2 = "jpg";
                        Elements imageElements = doc.getElementsByTag("img");
                        for (Element element : imageElements) {
                            String test = element.attr("src");
                            String test1 = test.substring(test.length() - 3);

                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }

                            if (test1.equals("jpg")) {
                                if (downloadToSave(test, filenames[i - 1])) {
                                    //System.out.println("src: "+element.attr("src"));
                                    Intent intent = new Intent();
                                    intent.setAction("download_ok");
                                    intent.putExtra("image", test);
                                    intent.putExtra("num", i);
                                    sendBroadcast(intent);
                                }
                                i++;

                            }

                            if (i == 21) {
                                //if(i==3){
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            });
            bkgThread.start();
        }

        // don't restart this task if killed by Android system
        return START_NOT_STICKY;
    }

    public boolean downloadToSave(String where, String filename) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, filename);



        try {
            URL url = new URL(where);
            URLConnection conn = url.openConnection();

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) != -1)
                out.write(buf, 0, bytesRead);

            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bkgThread.interrupt();

    }
}