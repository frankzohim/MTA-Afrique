package com.mtaafrique.colisexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ContactActivity extends AppCompatActivity {

    //Send button
    private Button envoyer = null;
    //Cancel button.
    private Button annuler = null;
    final Context context = this;
    //Mail to send
    String mail;
    String civilite;
    ProgressDialog dialog;
    EditText nom;
    EditText prenom;
    EditText email;
    EditText telephone;
    EditText objet;
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Loading data for drop down Titre
        final Spinner listeTitre = (Spinner)findViewById(R.id.spinner_contact);
        String[] items = new String[]{"M", "Mme", "Mlle", "Sté"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        listeTitre.setAdapter(adapter);

        //Retrieving button's ID
        envoyer = (Button)findViewById(R.id.envoyer_contact);
        annuler = (Button)findViewById(R.id.annuler_contact);

        nom = (EditText) findViewById(R.id.nom_contact);
        prenom = (EditText) findViewById(R.id.prenom_contact);
        email = (EditText) findViewById(R.id.email_contact);
        telephone = (EditText) findViewById(R.id.telephone_contact);
        objet = (EditText) findViewById(R.id.objet);
        message = (EditText) findViewById(R.id.message);

        //Listener of send button

        envoyer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!isEmpty()){
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Les champs avec * sont obligatoires.", Toast.LENGTH_SHORT);
                    LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
                    TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
                    toastTV2.setTextSize(30);
                    toast2.show();
                }

                else{
                    EditText emailValidate = (EditText) findViewById(R.id.email_contact);
                    String emailv = emailValidate.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!emailv.matches(emailPattern)){
                        Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Email valide.", Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                    }

                    else{

                        civilite = listeTitre.getSelectedItem().toString();

                        //Ex�cution d'un thread pour d�tecter la pr�sence de la connexion internet
                        new NetworkAsyncTask().execute();
                        dialog = ProgressDialog.show(context, "", "Veuillez patienter...", true);
                    }

                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent1 = new Intent(ContactActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    private boolean isEmpty() {

        EditText nom = (EditText) findViewById(R.id.nom_contact);
        EditText email = (EditText) findViewById(R.id.email_contact);
        EditText telephone = (EditText) findViewById(R.id.telephone_contact);
        EditText objet = (EditText) findViewById(R.id.objet);
        EditText message = (EditText) findViewById(R.id.message);

        if(nom.getText().toString().length() == 0)
            return false;
        if(telephone.getText().toString().length() == 0)
            return false;
        if(email.getText().toString().length() == 0)
            return false;
        if(objet.getText().toString().length() == 0)
            return false;
        if(message.getText().toString().length() == 0)
            return false;

        return true;
    }

    private class SendMailAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "0";
            try {
                GmailSender sender = new GmailSender("mtaafrique@gmail.com", "Me89@#001@hg96");
                result = sender.sendMail("Demande d'information",mail,"mtaafrique@gmail.com","delanofofe@gmail.com,contact@mta-dc.com");
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            String message;
            if("1".equals(result)){
                message = "Votre message a été envoyé avec succés.Nous vous contacterons dès que possible.";
            }
            else
                message ="Votre message n'a pas �t� envoy� veuillez r�essayez.";

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            // set title
            alertDialogBuilder.setTitle("Demande d'information");

            // set dialog message
            alertDialogBuilder
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(ContactActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
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

                //Corps du mail
                mail="Civilit� : "+civilite+
                        "\nNom : "+nom.getText().toString()+
                        "\nPrenom : "+prenom.getText().toString()+
                        "\nEmail : "+email.getText().toString()+
                        "\nT�l�phone : "+telephone.getText().toString()+
                        "\nObjet : "+objet.getText().toString()+
                        "\nMessage : "+message.getText().toString();

                // call AsynTask to perform Sending mail operation on separate thread
                new SendMailAsyncTask().execute(mail);
            }
            else{
                dialog.dismiss();
                //L'utilisateur n'est pas connect� � internet, on affiche un message d'information
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set title
                alertDialogBuilder.setTitle("Connexion internet requise");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Vous devez �tre connecter � Internet pour valider ce formulaire.")
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
