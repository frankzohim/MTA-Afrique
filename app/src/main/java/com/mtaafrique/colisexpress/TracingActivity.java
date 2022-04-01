package com.mtaafrique.colisexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient ;
import org.apache.http.impl.client.DefaultHttpClient;


public class TracingActivity extends AppCompatActivity {

    //Button suivre to go on second form
    private Button suivre = null;
    //Cancel button to go back home.
    private Button annuler = null;
    final Context context = this;
    TextView affichage;
    String isOnline = "0";
    ProgressDialog dialog;
    EditText numeroColis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        //Loading button's ID
        suivre = (Button)findViewById(R.id.suivre_colis);
        annuler = (Button)findViewById(R.id.annuler_tracing);
        affichage = (TextView) findViewById(R.id.affichage);
        numeroColis = (EditText) findViewById(R.id.numero_colis);

        suivre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(numeroColis.getText().toString().length()==0){
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Veuillez renseignez le numéro du colis", Toast.LENGTH_SHORT);
                    LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
                    TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
                    toastTV2.setTextSize(30);
                    toast2.show();
                }
                else{

                    //Running a thread to detect internet connection
                    new NetworkAsyncTask().execute();
                    dialog = ProgressDialog.show(context, "Chargement des données", "Veuillez patienter...", true);
                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(TracingActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        Log.d("url", url);
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            //Log.d("Result bb", result);
            // receive response as inputStream
            //if(httpResponse != null)
            //Log.d("httpResponse", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if(httpResponse.getStatusLine().getStatusCode()== 200){
                inputStream = httpResponse.getEntity().getContent();
                if(inputStream != null){
                    result = convertInputStreamToString(inputStream);
                }

                else
                    result = "200";
                Log.d("Result", result);
            }
            else
            if(httpResponse.getStatusLine().getStatusCode()== 204)
                result = "204";
            else
                result = "404";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static String isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            if(exitValue == 0)
                return "1";
            else
                return "0";


        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return "0";
    }

    public static String isInternetWorking() {
        String  success = "0";
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            if(connection.getResponseCode() == 200)
                success = "1";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }



    private class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            //return isOnline();
            return isInternetWorking();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            if("1".equals(result)){
                affichage.setText("");
                //String url = "http://10.0.2.2/rest/api.php";
                String url = "http://mta-afrique.com/rest/api.php";
                url = url+"?id="+numeroColis.getText().toString();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute(url);
            }
            else{
                dialog.dismiss();
                //User is not connected to internet, we display an information message
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set title
                alertDialogBuilder.setTitle("Connexion internet requise");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Vous devez être connecter à Internet pour valider ce formulaire.")
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if(result == "200")
                affichage.setText("Une erreur est survenue, veuillez réessayer ultérieurement."+result.toString());
            else
            if(result == "404")
                affichage.setText("Une erreur est survenue, veuillez réessayer ultérieurement.");
            else
            if(result == "204")
                affichage.setText("Le numéro de colis que vous avez entré est introuvable. Vérifiez-le et essayez à nouveau.");
            else{
                Intent intent2 = new Intent(TracingActivity.this, TracingResultActivity.class);
                intent2.putExtra("result", result);
                startActivity(intent2);
            }
        }
    }
}
