package com.example.carlinifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button login, mappa;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        mappa = findViewById(R.id.map);
        mappa.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validazione dati
                if(!username.getText().toString().isEmpty()) {
                    //se corretta
                    //autenticazione (remota)
                    downloadJSON("https://carlini5ainf2.altervista.org/MusicAir.php?username="+username.getText().toString()+"&password="+password.getText().toString());
                }else {
                    //se errrata
                    //messaggio di errore TOAST
                    Toast.makeText(getApplicationContext(), getText(R.string.invalid_username), Toast.LENGTH_LONG).show();

                }
            }
        });
        mappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Search.class);
                startActivity(i);
            }
        });
    }
    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL(urlWebService);
                    //connessione con ---> SOCKET ----> STREAM
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();
                    //SOCKET ---> INPUTSTREAM
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    Log.d("ThirdActivity", "doInBackground()" + sb.toString() );
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.d("ThirdActivity", "Exception in doInBackground()");
                    return null;
                }

            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s!="") {
                    try {
                        Toast.makeText(getApplicationContext(), "Loggato con successo!", Toast.LENGTH_LONG).show();
                        mappa.setVisibility(View.VISIBLE);
                        login.setVisibility(View.INVISIBLE);
                        //loadIntoTextview(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "errore", Toast.LENGTH_SHORT).show();
                }
            }

        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
}
