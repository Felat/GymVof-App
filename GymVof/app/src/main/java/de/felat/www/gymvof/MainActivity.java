package de.felat.www.gymvof;


import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {
    //von XML
    private Button btn;
    private TextView tvw;
    private Button btnDisconnect;
    private TCPClient mTcpClient;
    // Nur ein Test
    public String message1 = "-138-GV3App-{\"types\":[1],\"login\":{\"logout\":false,\"password\":\"Michael2\",\"id\":-1,\"loggedIn\":false,\"student\":true,\"username\":\"mrbauer\",\"register\":false}}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init
        btn = findViewById(R.id.button);
        tvw = findViewById(R.id.textView);
        btnDisconnect = findViewById(R.id.button2);

        // startet coonect task welcher im Hintergrund läuft
        new connectTask().execute("");


        //Onkcick btn
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendet message 1 siehe oben
                mTcpClient.sendMessage(message1);
            }
        });
        //Onclick btn Dsiconnect
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the
                    // sezt text view tvw auf den RTest der vom server abgefangen wird
                    tvw.setText(message.toString());
                    Log.e("In Method ", "S: Received Message: " + message);
                }
            });
            Log.e("TCP CLient", "C: Run Started ");
            mTcpClient.run();
            Log.e("TCP CLient", "C: Run Completed ");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.e("test", "response " + values[0]);


        }

    }
}






