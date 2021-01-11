package iss.ca.androidca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        ArrayList<Integer> positions = new ArrayList<Integer>();

        if (intent.getAction().equals("completed")) {
            setContentView(R.layout.activity_result_page);

            int pos1 = intent.getIntExtra("pos1", 0);
            int pos2 = intent.getIntExtra("pos2", 0);
            int pos3 = intent.getIntExtra("pos3", 0);
            int pos4 = intent.getIntExtra("pos4", 0);
            int pos5 = intent.getIntExtra("pos5", 0);
            int pos6 = intent.getIntExtra("pos6", 0);

            positions.add(pos1);
            positions.add(pos2);
            positions.add(pos3);
            positions.add(pos4);
            positions.add(pos5);
            positions.add(pos6);

            String time = intent.getStringExtra("timeUsed");
            TextView timerResult = findViewById(R.id.completeTime);
            timerResult.setText(time);

            String correct = intent.getStringExtra("correct");
            String wrong = intent.getStringExtra("wrong");
            String total = intent.getStringExtra("attempts");

            TextView correctAttempt = findViewById(R.id.correctAttempt);
            TextView wrongAttempt = findViewById(R.id.wrongAttempt);
            TextView totalAttempt = findViewById(R.id.totalAttempt);

            correctAttempt.setText(correct);
            wrongAttempt.setText(wrong);
            totalAttempt.setText(total);

            TextView textView = findViewById(R.id.textView);
            if (Integer.parseInt(correct) > Integer.parseInt(wrong)) {
                textView.setText("Congratulations! You are good!");
            } else if (Integer.parseInt(correct) == Integer.parseInt(total)) {
                textView.setText("Brilliant! You have a good memory!");
            } else {
                textView.setText("Try harder again~");
            }
        }

        Button button = findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameScreen.class);
                intent.setAction("restart");
                intent.putExtra("pos1", positions.get(0));
                intent.putExtra("pos2", positions.get(1));
                intent.putExtra("pos3", positions.get(2));
                intent.putExtra("pos4", positions.get(3));
                intent.putExtra("pos5", positions.get(4));
                intent.putExtra("pos6", positions.get(5));
                finish();
                startActivity(intent);
            }
        });

        Button buttonSelect = findViewById(R.id.reselect);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameScreen.class);
                intent.setAction("reselect");
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), WebBrowser.class);
        startActivity(intent);
    }
}