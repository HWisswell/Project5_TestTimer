package com.wisswell.ch10_ex5;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;



public class MainActivity extends Activity implements OnClickListener {

    private TextView messageTextView;
    private Button stopButton;
    private Button startButton;
    Timer timer = new Timer(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = (TextView) findViewById(R.id.messageTextView);
        stopButton = (Button) findViewById(R.id.stopButton);
        startButton = (Button) findViewById(R.id.startButton);

        stopButton.setOnClickListener(this);
        startButton.setOnClickListener(this);


        startTimer();
    }

    private void startTimer() {
        final long startMillis = System.currentTimeMillis();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;
                updateView(elapsedMillis);

                downloadFile();

;
            }
        };
        timer.schedule(task, 0, 1000);

    }

    public void onPause(){

        stopTimer();
        super.onPause();
    }

    public void stopTimer(){

        timer.cancel();
    }

    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis/1000;
            int downloadTimes = elapsedSeconds/10;

            @Override
            public void run() {
                //messageTextView.setText("Seconds: " + elapsedSeconds);
                messageTextView.setText("File downloaded " + downloadTimes + " time(s)");
            }
        });
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                startTimer();
                break;
            case R.id.stopButton:
                stopTimer();
                break;
        }
    }



    public void downloadFile() {

        final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
        final String FILENAME = "news_feed.xml";





        try{
            // get the URL
            URL url = new URL(URL_STRING);

            // get the input stream
            InputStream in = url.openStream();

            // get the output stream
            FileOutputStream out = openFileOutput(FILENAME, MODE_PRIVATE);

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        }
        catch (IOException e) {
            Log.e("News reader", e.toString());
        }
    }

}