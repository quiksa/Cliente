package com.example.guilherme.cliente;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.guilherme.cliente.R.id.welcomemsg;

public class MainActivity extends AppCompatActivity {

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, consultar;
    EditText welcomeMsg;


     TCPClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonClear = (Button) findViewById(R.id.clear);
        textResponse = (TextView) findViewById(R.id.response);
        welcomeMsg = (EditText) findViewById(welcomemsg);
        consultar = (Button) findViewById(R.id.consultar);

        editTextAddress.setText("192.168.31.168");
        editTextPort.setText("1234");
        welcomeMsg.setText("/autores");

        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mTcpClient = new TCPClient(editTextAddress.getText().toString(),Integer.parseInt(editTextPort.getText().toString()));
                new connectTask().execute("");
            }
        });

        buttonClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomeMsg.setText("");
            }
        });

        consultar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = welcomeMsg.getText().toString();

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }
            }
        });


    }


    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            //in the arrayList we add the messaged received from server
            Log.e(values[0],"teste");
            textResponse.setText(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            //mAdapter.notifyDataSetChanged();
        }
    }


}
