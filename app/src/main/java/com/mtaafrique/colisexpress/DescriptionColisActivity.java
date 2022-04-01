package com.mtaafrique.colisexpress;

import android.app.Activity;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient ;
import org.apache.http.impl.client.DefaultHttpClient;

public class DescriptionColisActivity extends Activity {

    //Send button to send data via mail if internet connection or SMS otherwise
    private Button envoyer = null;
    //Cancel button to come back on quotation form.
    private Button annuler = null;
    EditText descriptionColis;
    EditText poidsColis;
    EditText longueurColis;
    EditText largeurColis;
    EditText hauteurColis;
    final Context context = this;
    String phoneNumCmr = "+237698932110;";
    String phoneNumInternational = "+33634615550;";
    String sms;
    String mail;
    String mailContent;
    boolean isConnected = false;
    ProgressDialog dialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_colis);

        intent = getIntent();
        if(intent != null){

            //Loading button's ID
            envoyer = (Button)findViewById(R.id.envoyer);
            annuler = (Button)findViewById(R.id.annuler1);

            //Loading EditText's ID
            descriptionColis = (EditText) findViewById(R.id.descriptionColis);
            poidsColis = (EditText) findViewById(R.id.poidsColis);
            longueurColis = (EditText) findViewById(R.id.longueurColis);
            largeurColis = (EditText) findViewById(R.id.largeurColis);
            hauteurColis = (EditText) findViewById(R.id.hauteurColis);

            //Listener of send button

            envoyer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if(!isEmpty()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Tous les champs sont requis.", Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                    }
                    else{
                        //Execution of a thread to detect internet connection
                        new NetworkAsyncTask().execute();
                        dialog = ProgressDialog.show(context, "", "Veuillez patienter...", true);
                    }

                }
            });

            annuler.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent1 = new Intent(DescriptionColisActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            });

        }
        else{
            Toast.makeText(getApplicationContext(), "Une erreur est survenue, l'application va redémarrer", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DescriptionColisActivity.this, MainActivity.class);
            startActivity(intent1);
        }
    }

    private boolean isEmpty() {

        EditText descriptionColis = (EditText) findViewById(R.id.descriptionColis);
        EditText poidsColis = (EditText) findViewById(R.id.poidsColis);
        EditText longueurColis = (EditText) findViewById(R.id.longueurColis);
        EditText largeurColis = (EditText) findViewById(R.id.largeurColis);

        if(descriptionColis.getText().toString().length() == 0)
            return false;
        if(poidsColis.getText().toString().length() == 0)
            return false;
        if(longueurColis.getText().toString().length() == 0)
            return false;
        if(largeurColis.getText().toString().length() == 0)
            return false;

        return true;

    }

    public static String isOnline() {
        Log.e("url", "Checking connection");
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

    public static String GET(String url){
        //InputStream inputStream = null;
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
            Log.d("httpResponse", String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            if(httpResponse.getStatusLine().getStatusCode()== 200){
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            String message;
            if(result == "200")
                message = "Votre message a été envoyé avec succès!";
            else
            if(result == "404")
                message = result+"Une erreur est survenue, veuillez réessayer ultérieurement.";
            else
            if(result == "204")
                message = "Une erreur est survenue, veuillez réessayer ultérieurement.";
            else{
                message = "Une erreur est survenue, veuillez réessayer ultérieurement.";
            }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Demande d\'expédition");

            // set dialog message
            alertDialogBuilder
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent i = new Intent(DescriptionColisActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    private class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return isOnline();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            if("1".equals(result)){

                //Construction du Mail
                mailContent = "Expediteur;";
                mailContent= mailContent+"Titre:"+intent.getStringExtra("titreExpediteur")+";";
                mailContent=mailContent+"Nom:"+intent.getStringExtra("nomExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"Telephone:"+intent.getStringExtra("telephoneExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"Email:"+intent.getStringExtra("emailExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"Pays:"+intent.getStringExtra("paysExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"Ville:"+intent.getStringExtra("villeExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"Adresse:"+intent.getStringExtra("adresseExpediteur").replace(' ','_')+";";
                mailContent= mailContent+"CodePostal:"+intent.getStringExtra("codePostalExpediteur").replace(' ','_')+";";
                mailContent = mailContent+"_;";
                mailContent = mailContent+"Destinataire;";
                mailContent= mailContent+"Titre:"+intent.getStringExtra("titreDestinataire")+";";
                mailContent= mailContent+"Nom:"+intent.getStringExtra("nomDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"Telephone:"+intent.getStringExtra("telephoneDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"Email:"+intent.getStringExtra("emailDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"Pays:"+intent.getStringExtra("paysDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"Ville:"+intent.getStringExtra("villeDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"Adresse:"+intent.getStringExtra("adresseDestinataire").replace(' ','_')+";";
                mailContent= mailContent+"CodePostal:"+intent.getStringExtra("codePostalDestinataire").replace(' ','_')+";";
                mailContent = mailContent+"_;";
                mailContent = mailContent+"Colis;";
                mailContent = mailContent+"D:"+descriptionColis.getText().toString().replace(' ','_')+";";
                mailContent = mailContent+"P:"+poidsColis.getText().toString().replace(' ','_')+"kg;";
                mailContent = mailContent+"L:"+longueurColis.getText().toString().replace(' ','_')+"cm;";
                mailContent = mailContent+"l:"+largeurColis.getText().toString().replace(' ','_')+"cm;";
                mailContent = mailContent+"H:"+hauteurColis.getText().toString().replace(' ','_')+"cm;";

                mail="Informations sur l'expéditeur\n"+
                        "\n\nInformation sur le colis\n"+
                        "\nDescription : "+descriptionColis.getText().toString()+
                        "\nPoids : "+poidsColis.getText().toString()+" Kg"+
                        "\nLongueur : "+longueurColis.getText().toString()+" cm"+
                        "\nLargeur : "+largeurColis.getText().toString()+" cm"+
                        "\nHauteur : "+hauteurColis.getText().toString()+" cm";
                System.out.println(mail);
                String url = "http://mta-afrique.com/mailsender/api.php";
                url = url+"?mail="+mailContent;
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTask().execute(url);
            }
            else{
                dialog.dismiss();
                //Construction du SMS
                String description ="";
                if(descriptionColis.getText().toString().length() > 20)
                    description = descriptionColis.getText().toString().substring(0, 20);
                else
                    description = descriptionColis.getText().toString();

                sms="Demande d'expédition :"+intent.getStringExtra("titreExpediteur")+
                        " "+intent.getStringExtra("nomExpediteur")+
                        ","+intent.getStringExtra("telephoneExpediteur")+
                        ","+intent.getStringExtra("emailExpediteur")+
                        "de "+intent.getStringExtra("villeExpediteur")+
                        "  "+intent.getStringExtra("paysExpediteur")+
                        " � "+intent.getStringExtra("titreDestinataire")+
                        " "+intent.getStringExtra("nomDestinataire")+
                        ","+intent.getStringExtra("telephoneDestinataire")+
                        ","+intent.getStringExtra("emailDestinataire")+
                        " de "+intent.getStringExtra("villeDestinataire")+
                        "  "+intent.getStringExtra("paysDestinataire")+
                        ", Desc:"+description+
                        ",Pds: "+poidsColis.getText().toString()+
                        ",Dim: "+longueurColis.getText().toString()+
                        "*"+largeurColis.getText().toString()+
                        "*"+hauteurColis.getText().toString()+"";
                //L'utilisateur n'est pas connecté à internet, on envoi les données par SMS sous confirmation
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set title
                alertDialogBuilder.setTitle("Connexion internet requise");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Veuillez vous connecter à internet pour envoyer les informations.")
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
}
