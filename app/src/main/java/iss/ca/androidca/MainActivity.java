package iss.ca.androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends AppCompatActivity{

    Button startGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.btnStartGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startWebBrowserActivity();
            }
        });

    }

    private void startWebBrowserActivity()
    {
        Intent intent = new Intent(this,WebBrowser.class);
        startActivity(intent);
    }


}

