package com.mtaafrique.colisexpress;

import androidx.appcompat.app.AppCompatActivity;

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

public class CotationActivity extends AppCompatActivity {

    //Next button to go on second form
    private Button suivant = null;

    //Cancel button to come back home
    private Button annuler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotation);

        //Loading data for drop down Titre
        final Spinner listeTitre = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"M", "Mme", "Mlle", "Sté"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        listeTitre.setAdapter(adapter);

        //Loading data for drop down Pays
        final Spinner listePays = (Spinner)findViewById(R.id.spinner2);
        String[] items1 = new String[]{"Cameroun", "France", "Allemagne", "USA", "Canada", "Chine"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        listePays.setAdapter(adapter1);

        //Loading Button's ID
        suivant = (Button)findViewById(R.id.suivant);
        annuler = (Button)findViewById(R.id.annuler);

        //Listener of next button

        suivant.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(!isEmpty()){
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Les champs avec * sont requis.", Toast.LENGTH_LONG);
                    LinearLayout toastLayout2 = (LinearLayout) toast2.getView();
                    TextView toastTV2 = (TextView) toastLayout2.getChildAt(0);
                    toastTV2.setTextSize(30);
                    toast2.show();
                }

                else{
                    int i=0;
                    int j=0;
                    String paysExpediteur = listePays.getSelectedItem().toString();
                    if(!("Cameroun".equals(paysExpediteur))){

                        EditText adresse = (EditText) findViewById(R.id.adressetxt);
                        EditText codePostal = (EditText) findViewById(R.id.codepostaltxt);
                        if(adresse.getText().toString().length() == 0 || codePostal.getText().toString().length() == 0){
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

                    EditText emailValidate = (EditText) findViewById(R.id.emailtxt);
                    String emailv = emailValidate.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
                    if(emailv.length() != 0){
                        if(!emailv.matches(emailPattern)){
                            Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Email valide.", Toast.LENGTH_SHORT);
                            LinearLayout toastLayout = (LinearLayout) toast.getView();
                            TextView toastTV = (TextView) toastLayout.getChildAt(0);
                            toastTV.setTextSize(30);
                            toast.show();
                        }
                        else
                            j=1;
                    }

                    else
                        j=1;

                    if(i==1 && j==1){

                        EditText nom = (EditText) findViewById(R.id.nom);
                        EditText telephone = (EditText) findViewById(R.id.telephonetxt);
                        EditText email = (EditText) findViewById(R.id.emailtxt);
                        EditText ville = (EditText) findViewById(R.id.villetxt);
                        EditText adresse = (EditText) findViewById(R.id.adressetxt);
                        EditText codePostal = (EditText) findViewById(R.id.codepostaltxt);
                        String titreExpediteur = listeTitre.getSelectedItem().toString();
                        String nomExpediteur = nom.getText().toString();
                        String telephoneExpediteur = telephone.getText().toString();
                        String emailExpediteur = email.getText().toString();
                        paysExpediteur = listePays.getSelectedItem().toString();
                        String villeExpediteur = ville.getText().toString();
                        String adresseExpediteur = adresse.getText().toString();
                        String codePostalExpediteur = codePostal.getText().toString();
                        Intent intent = new Intent(CotationActivity.this, DestinataireActivity.class);
                        intent.putExtra("titreExpediteur", titreExpediteur);
                        intent.putExtra("nomExpediteur", nomExpediteur);
                        intent.putExtra("telephoneExpediteur", telephoneExpediteur);
                        intent.putExtra("emailExpediteur", emailExpediteur);
                        intent.putExtra("paysExpediteur", paysExpediteur);
                        intent.putExtra("villeExpediteur", villeExpediteur);
                        intent.putExtra("adresseExpediteur", adresseExpediteur);
                        intent.putExtra("codePostalExpediteur", codePostalExpediteur);
                        startActivity(intent);
                    }

                }
            }
        });

        //Listener of cancel button

        annuler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CotationActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean isEmpty() {

        EditText nom = (EditText) findViewById(R.id.nom);
        EditText telephone = (EditText) findViewById(R.id.telephonetxt);
        EditText ville = (EditText) findViewById(R.id.villetxt);


        if(nom.getText().toString().length() == 0)
            return false;
        if(telephone.getText().toString().length() == 0)
            return false;
        if(ville.getText().toString().length() == 0)
            return false;

        return true;
    }
}
