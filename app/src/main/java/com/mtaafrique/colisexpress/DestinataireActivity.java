package com.mtaafrique.colisexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DestinataireActivity extends Activity {
    //Previous button to come back on sender information
    private Button precedent = null;

    //Next button to go on parcel form
    private Button suivant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinataire);

        final Intent intent = getIntent();
        if(intent != null){
            //Loading data for drop down Titre
            final Spinner listeTitre = (Spinner)findViewById(R.id.spinnerdestinataire);
            String[] items = new String[]{"M", "Mme", "Mlle", "Sté"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
            listeTitre.setAdapter(adapter);

            //Loading data for drop down Pays
            final Spinner listePays = (Spinner)findViewById(R.id.spinnerpays);
            String[] items1 = new String[]{"Cameroun", "France", "Allemagne", "USA", "Canada", "Chine"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
            listePays.setAdapter(adapter1);

            //Loading button's ID
            suivant = (Button)findViewById(R.id.suivant1);
            precedent = (Button)findViewById(R.id.precedent);

            //Listener of next button

            suivant.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if(!isEmpty()){
                        Toast toast = Toast.makeText(getApplicationContext(), "Les champs avec * sont requis.", Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                    }

                    else{

                        int i=0;
                        int j=0;
                        String paysDestinataire = listePays.getSelectedItem().toString();

                        if(!("Cameroun".equals(paysDestinataire))){

                            EditText adresseDestinataire = (EditText) findViewById(R.id.adresseDestinataire);
                            EditText codePostalDestinataire = (EditText) findViewById(R.id.codePostalDestinataire);
                            if(adresseDestinataire.getText().toString().length() == 0 || codePostalDestinataire.getText().toString().length() == 0){
                                Toast toast2 = Toast.makeText(getApplicationContext(), "L'adresse et le code postal sont requis.", Toast.LENGTH_LONG);
                                LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
                                TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
                                toastTV2.setTextSize(30);
                                toast2.show();
                            }
                            else
                                i=1;
                        }
                        else
                            i=1;

                        EditText emailValidate = (EditText) findViewById(R.id.emailDestinataire);
                        String emailv = emailValidate.getText().toString().trim();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
                        if(emailv.length() != 0){
                            if (!emailv.matches(emailPattern)){
                                Toast toast2 = Toast.makeText(getApplicationContext(), "Veuillez entrer un Email valide.", Toast.LENGTH_SHORT);
                                LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
                                TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
                                toastTV2.setTextSize(30);
                                toast2.show();
                            }
                            else
                                j=1;
                        }
                        else
                            j=1;

                        if(i==1 && j==1){
                            EditText nom = (EditText) findViewById(R.id.nomDestinataire);
                            EditText telephone = (EditText) findViewById(R.id.telephoneDestinataire);
                            EditText email = (EditText) findViewById(R.id.emailDestinataire);
                            EditText ville = (EditText) findViewById(R.id.villeDestinataire);
                            EditText adresse = (EditText) findViewById(R.id.adresseDestinataire);
                            EditText codePostal = (EditText) findViewById(R.id.codePostalDestinataire);
                            String titreDestinataire = listeTitre.getSelectedItem().toString();
                            String nomDestinataire = nom.getText().toString();
                            String telephoneDestinataire = telephone.getText().toString();
                            String emailDestinataire = email.getText().toString();
                            paysDestinataire = listePays.getSelectedItem().toString();
                            String villeDestinataire = ville.getText().toString();
                            String adresseDestinataire = adresse.getText().toString();
                            String codePostalDestinataire = codePostal.getText().toString();
                            Intent newIntent = new Intent(DestinataireActivity.this, DescriptionColisActivity.class);

                            //Sending data on receiver
                            newIntent.putExtra("titreDestinataire", titreDestinataire);
                            newIntent.putExtra("nomDestinataire", nomDestinataire);
                            newIntent.putExtra("telephoneDestinataire", telephoneDestinataire);
                            newIntent.putExtra("emailDestinataire", emailDestinataire);
                            newIntent.putExtra("paysDestinataire", paysDestinataire);
                            newIntent.putExtra("villeDestinataire", villeDestinataire);
                            newIntent.putExtra("adresseDestinataire", adresseDestinataire);
                            newIntent.putExtra("codePostalDestinataire", codePostalDestinataire);

                            //Sending data on sender
                            newIntent.putExtra("titreExpediteur", intent.getStringExtra("titreExpediteur"));
                            newIntent.putExtra("nomExpediteur", intent.getStringExtra("nomExpediteur"));
                            newIntent.putExtra("telephoneExpediteur", intent.getStringExtra("telephoneExpediteur"));
                            newIntent.putExtra("emailExpediteur", intent.getStringExtra("emailExpediteur"));
                            newIntent.putExtra("paysExpediteur", intent.getStringExtra("paysExpediteur"));
                            newIntent.putExtra("villeExpediteur",intent.getStringExtra("villeExpediteur"));
                            newIntent.putExtra("adresseExpediteur", intent.getStringExtra("adresseExpediteur"));
                            newIntent.putExtra("codePostalExpediteur", intent.getStringExtra("codePostalExpediteur"));
                            startActivity(newIntent);
                        }
                    }


                }
            });

            //Listener of previous button

            precedent.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(DestinataireActivity.this, CotationActivity.class);
                    startActivity(intent);

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Une erreur est survenue, l'application va redémarrer", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(DestinataireActivity.this, MainActivity.class);
            startActivity(intent1);
        }

    }

    private boolean isEmpty() {

        EditText nomDestinataire = (EditText) findViewById(R.id.nomDestinataire);
        EditText telephoneDestinataire = (EditText) findViewById(R.id.telephoneDestinataire);
        EditText villeDestinataire = (EditText) findViewById(R.id.villeDestinataire);

        if(nomDestinataire.getText().toString().length() == 0)
            return false;
        if(telephoneDestinataire.getText().toString().length() == 0)
            return false;

        if(villeDestinataire.getText().toString().length() == 0)
            return false;

        return true;

    }
}
