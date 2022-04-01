package com.mtaafrique.colisexpress;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    //Button that launched quotation form
    private Button cotation = null;

    //Button that launched traking form.
    private Button tracing = null;

    //Button that launched contact form
    private Button contact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loading buttons ID
        cotation = (Button)findViewById(R.id.cotation);
        tracing = (Button)findViewById(R.id.tracing);
        contact = (Button)findViewById(R.id.contact);

        //Listener on cotation button
        cotation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), CotationActivity.class);
                startActivity(intent);

            }
        });

        //Listener on tracking button
        tracing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, TracingActivity.class);
                startActivity(intent);

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);

            }
        });

    }
}


