package com.mtaafrique.colisexpress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TracingResultActivity extends AppCompatActivity {

    private Button tracing = null;
    TextView affichage;
    TextView acheminement;
    TextView arrivee;
    TextView entete;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing_result);

        final Intent intent = getIntent();
        if(intent != null){

            tracing = (Button)findViewById(R.id.new_tracing_result);
            affichage = (TextView) findViewById(R.id.affichage_tracing_result);
            acheminement = (TextView) findViewById(R.id.acheminement_tracing_result);
            arrivee = (TextView) findViewById(R.id.arrivee_tracing_result);
            entete = (TextView) findViewById(R.id.resultat_tracing_entete);
            //affichage.setText(intent.getStringExtra("result"));

            result = intent.getStringExtra("result");

            // Retrieving JSON Object
            try{
                JSONObject jsonObject = new JSONObject(result);

                // Retrieving DT array
                JSONArray arrayDt = new JSONArray(jsonObject.getString("dt"));
                //JSONArray arrayDe = new JSONArray(jsonObject.getString("de"));
                //JSONArray arraySuivideGlobal = new JSONArray(jsonObject.getString("suivideglobal"));
                JSONObject dt = new JSONObject(arrayDt.getString(0));
                Log.d("CodeCourrier", dt.getString("CodeCourrier"));
                entete.setText("Tracing du DT №  "+dt.getString("CodeCourrier")+"  Emis le "+dt.getString("DateDepot"));
                affichage.setText("Expéditeur: "+dt.getString("Civilite")+" "+dt.getString("NomExpediteur")+"\n"+
                        "Ville départ: "+dt.getString("libelleVilleDepart")+"\n"+
                        "Destinataire: "+dt.getString("Civilite2")+" "+dt.getString("NomDestinataire")+"\n"+
                        "Ville arrivée: "+dt.getString("libelleVilleArrivee")+"\n"+
                        "Nature: "+dt.getString("NatureCourrier")+"\n"+
                        "Description: "+dt.getString("Observation")+"\n"+
                        "Quantité: "+dt.getString("Quantite")+"\n"+
                        "Poids: "+dt.getString("Poids")+" kg\n");

                if(!jsonObject.isNull("de")){
                    Log.d("de", "non vide");
                    JSONArray arrayDe = new JSONArray(jsonObject.getString("de"));
                    int j = 0;
                    JSONObject suiviDeGlobal = new JSONObject();
                    if(!jsonObject.isNull("suivideglobal")){
                        Log.d("suivideglobal", "non vide");
                        //JSONObject getest = new JSONObject(arrayDe.getString(i));
                        suiviDeGlobal = new JSONObject(jsonObject.getString("suivideglobal"));
                        j = 1;
                    }

                    // For all DE we retrieve informations
                    String parcours="";
                    for (int i = 0; i < arrayDe.length(); i++) {
                        // We retrieve a JSON Object from array
                        JSONObject de = new JSONObject(arrayDe.getString(i));
                        parcours +="Par "+de.getString("Moyen")+" De "+de.getString("libelleVilleDepart")+"\n";
                        parcours +="Le "+de.getString("DateDepot")+"\n\n";
                        if(j==1){
                            if(!suiviDeGlobal.isNull(de.getString("CodeDe"))){
                                parcours +="*******SUIVI*******\n\n";
                                JSONArray elt_de = new JSONArray(suiviDeGlobal.getString(de.getString("CodeDe")));
                                for (int f = 0; f < elt_de.length(); f++) {
                                    JSONObject elt_suivi_de = new JSONObject(elt_de.getString(f));
                                    parcours +="Le "+elt_suivi_de.getString("Date")+"\n";
                                    parcours +="A "+elt_suivi_de.getString("Heure")+"\n";
                                    parcours +=elt_suivi_de.getString("Statut")+" "+elt_suivi_de.getString("Obs")+"\n";
                                    parcours +=elt_suivi_de.getString("Obs")+"\n";
                                }
                                parcours +="\n*******FIN SUIVI*******\n\n";
                            }
                        }
                        parcours +="Arrivé a "+de.getString("libelleVilleArrivee")+"\n";
                        parcours +="Le "+de.getString("DateArrivee")+"\n";

                    }
                    acheminement.setText(parcours);
                }
                else
                    Log.d("de", "vide");

                if(!dt.isNull("DateArrivee")){
                    String infosArrive="";
                    infosArrive +="\n";

                    if(!dt.isNull("libelleVilleArrivee"))
                        infosArrive +="Arrivé à : "+dt.getString("libelleVilleArrivee")+"\n";
                    else
                        infosArrive +="Arrivé à : \n";

                    if(!dt.isNull("DateArrivee"))
                        infosArrive +="Le "+dt.getString("DateArrivee")+"\n";
                    else
                        infosArrive +="Le \n";

                    if(!dt.isNull("Datelivraison"))
                        infosArrive +="Livré le "+dt.getString("DateArrivee")+"\n";
                    else
                        infosArrive +="Livré le \n";

                    if(!dt.isNull("HeureLivraison"))
                        infosArrive +="A: "+dt.getString("HeureLivraison")+" h "+dt.getString("MinuteLivraison")+"\n";
                    else
                        infosArrive +="A:  \n";

                    if(!dt.isNull("DechargePar"))
                        infosArrive +="Déchargé par: "+dt.getString("DechargePar")+"\n";
                    else
                        infosArrive +="Déchargé par: \n";

                    if(!dt.isNull("NumeroPiece"))
                        infosArrive +="CNI: "+dt.getString("NumeroPiece")+"\n";
                    else
                        infosArrive +="CNI: \n";

                    if(!dt.isNull("TelDestinataire"))
                        infosArrive +="Tel: "+dt.getString("TelDestinataire")+"\n";
                    else
                        infosArrive +="Tel: \n";

                    if(!dt.isNull("ObservationLivraison"))
                        infosArrive +="Observations: "+dt.getString("ObservationLivraison")+"\n";
                    else
                        infosArrive +="Observations ";
                    infosArrive +="\n";
                    arrivee.setText(infosArrive);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        tracing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent tracingIntent = new Intent(TracingResultActivity.this, MainActivity.class);
                startActivity(tracingIntent);
            }
        });
    }
}
