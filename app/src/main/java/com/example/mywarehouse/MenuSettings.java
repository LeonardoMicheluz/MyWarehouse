package com.example.mywarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuSettings extends AppCompatActivity {

    private TextView lblMax;
    private TextView lblAttuale;
    private EditText cambia;
    private TextView txtAttuale;
    private TextView txtMax;
    private ImageView capacita;
    private ImageView elimina;
    private Button btnCambia;
    private Button btnCapacita;
    private Button btnCambia2;
    private Button btnElimina;

    private int capacitaAttuale;        //variabile per la capacità attuale del magazzino
    private int capacitaMax=0;          //variabile per la capacità massima del magazzino
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_settings);

        lblMax = (TextView) findViewById(R.id.lblMax);
        lblAttuale = (TextView) findViewById(R.id.lblAttuale);
        cambia = (EditText) findViewById(R.id.txtCambia);
        txtAttuale = (TextView) findViewById(R.id.txtAttuale);
        txtMax = (TextView) findViewById(R.id.txtMax);
        capacita = (ImageView) findViewById(R.id.imgCapacita);
        elimina = (ImageView) findViewById(R.id.imgElimina);
        btnCambia = (Button) findViewById(R.id.btnCambiaCap);
        btnCapacita = (Button) findViewById(R.id.btnCapacita);
        btnCambia2 = (Button) findViewById(R.id.btnCambia);
        btnElimina = (Button) findViewById(R.id.btnElimina);

        Intent i = getIntent();             //riprendo i valori della activity precedente
        capacitaMax=i.getIntExtra("cap",0);
        txtMax.setText(""+capacitaMax);

        capacitaAttuale=db.Count();
        txtAttuale.setText(""+capacitaAttuale);

        btnCapacita.setOnClickListener(new View.OnClickListener() {     //pulsante per visualizzare informazioni sulle capacità
            @Override
            public void onClick(View v) {
                capacita.setVisibility(View.INVISIBLE);         //layout
                btnCapacita.setVisibility(View.INVISIBLE);
                btnElimina.setVisibility(View.INVISIBLE);
                elimina.setVisibility(View.INVISIBLE);
                MostraCap();            //funzione layout (riga 122)
            }
        });

        btnCambia.setOnClickListener(new View.OnClickListener() {       //pulsante per inserire la capacità nuova
            @Override
            public void onClick(View v) {
                NascondiCap();          //funzione layout (riga 130)
            }
        });

        btnCambia2.setOnClickListener(new View.OnClickListener() {      //pulsante per mddificare la capacità massima
            @Override
            public void onClick(View v) {
                btnCambia.setVisibility(View.INVISIBLE);

                if(cambia.getText().toString().isEmpty() || Integer.parseInt(cambia.getText().toString())<capacitaAttuale)
                    Toast.makeText(MenuSettings.this, "Inserisci un valore corretto!",Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(MenuSettings.this, "capacità modificata!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();       //ritorno alla activity precedente la nuova capacità
                    intent.putExtra("cap", Integer.parseInt(cambia.getText().toString()));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {      //pulsante per eliminare tutti i record
            @Override
            public void onClick(View v) {       //alert dialouge di sicurezza
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuSettings.this);
                builder.setTitle("Elimina");
                builder.setMessage("Sei sicuro di voler eliminare l'intero magazzino?");
                builder.setCancelable(false);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {   //se l'utente risponde si elimino tutto
                        db.DeleteAll();
                        txtAttuale.setText(""+db.Count());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {        //altrimenti chiudo l'alert
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void MostraCap(){           //funzione layout
        lblAttuale.setVisibility(View.VISIBLE);
        lblMax.setVisibility(View.VISIBLE);
        txtAttuale.setVisibility(View.VISIBLE);
        txtMax.setVisibility(View.VISIBLE);
        btnCambia.setVisibility(View.VISIBLE);
    }

    private void NascondiCap(){         //funzione layout
        lblAttuale.setVisibility(View.INVISIBLE);
        lblMax.setVisibility(View.INVISIBLE);
        txtAttuale.setVisibility(View.INVISIBLE);
        txtMax.setVisibility(View.INVISIBLE);
        btnCapacita.setVisibility(View.INVISIBLE);
        btnCambia2.setVisibility(View.VISIBLE);
        cambia.setVisibility(View.VISIBLE);
        btnCambia.setVisibility(View.INVISIBLE);
        elimina.setVisibility(View.INVISIBLE);
        btnElimina.setVisibility(View.INVISIBLE);
    }
}