package com.example.mywarehouse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class User extends AppCompatActivity {

    private EditText user;
    private EditText pass;
    private Button btnImp;
    private Button btnAmm;
    private ImageButton btnAdd;
    private ImageButton btnEdit;
    private ImageButton btnDelete;
    private ImageButton btnView;
    private ImageButton btnSearch;
    private ImageButton btnSettings;
    private TextView lblAdd;
    private TextView lblEdit;
    private TextView lblDelete;
    private TextView lblView;
    private TextView lblSearch;
    private TextView lblSettings;
    private TextView lblUser;
    private TextView lblPass;
    private ImageButton check;

    private Database db = new Database(this);;
    private AlertDialog.Builder builder,builder2;
    private int contI=0;        //variabili per controllare se l'utente ha selezionato impiegato o amministratore
    private int contA=0;        //
    private String userImp="",passImp="",userAmm="",passAmm=""; //credenziali degli utenti
    private int capacita=0;     //capacità massima del magazzino
    private SharedPreferences sharedPref;
    private int LAUNCH_ACTIVITY;        //variabili per gli intent
    private int LAUNCH_SETTINGS_ACTIVITY;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Assegnazione();                 //funzione per assegnare i vari componenti (riga 227)
        CreaAlert();                    //funzione per creare gli alertDialogue (riga 279)
        Recupera();                     //prendo i valori dallo sharedPreferences (riga 339)

        if(userImp == null){        //se una credenziale è nulla vuol dire che è la prima volta che si usa l'app
            AlertDialog alert = builder2.create();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {    //mostro l'alert dialouge
                @Override
                public void onDismiss(DialogInterface dialogInterface) {        //passo all'intent successiva
                    LAUNCH_ACTIVITY=0;
                    Intent intent = new Intent(User.this, Credenziali.class);
                    startActivityForResult(intent, LAUNCH_ACTIVITY);
                }
            });
            alert.show();
        }

        check.setOnClickListener(new View.OnClickListener() {       //pulsante per confermare le credenziali
            @Override
            public void onClick(View v) {
                lblUser.setVisibility(View.INVISIBLE);          //layout
                user.setVisibility(View.INVISIBLE);
                lblPass.setVisibility(View.INVISIBLE);
                pass.setVisibility(View.INVISIBLE);
                check.setVisibility(View.INVISIBLE);
                if (contI != 0) {           //se si ha premuto il pulsante impiegato
                    if (user.getText().toString().equals(userImp) && pass.getText().toString().equals(passImp)) {   //e le credenziali sono corrette
                        lblUser.setVisibility(View.INVISIBLE);
                        user.setVisibility(View.INVISIBLE);         //layout
                        lblPass.setVisibility(View.INVISIBLE);
                        pass.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.INVISIBLE);
                        Mostra();                               //funzione per il layout (riga 249)
                        btnImp.setClickable(false);
                        btnAmm.setClickable(true);              //layout
                        contA = 0;                  //imposto la variabile di amministratore a zero
                        user.setText("");
                        pass.setText("");               //layout
                    } else {                //altrimenti se le credenziali sono errate stampo un alertDialogue
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else if (contA != 0) {    //stessa cosa per questo, ma controllo se ho selezionato il pulsante amministratore
                    if (user.getText().toString().equals(userAmm) && pass.getText().toString().equals(passAmm)) {
                        lblUser.setVisibility(View.INVISIBLE);
                        user.setVisibility(View.INVISIBLE);
                        lblPass.setVisibility(View.INVISIBLE);
                        pass.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.INVISIBLE);
                        Mostra();
                        btnImp.setClickable(true);
                        btnAmm.setClickable(false);
                        contA = 0;
                        user.setText("");
                        pass.setText("");
                    } else {
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });

        btnImp.setOnClickListener(new View.OnClickListener() {      //pulsante impiegato
            @Override
            public void onClick(View v) {
                lblUser.setVisibility(View.VISIBLE);        //layout
                user.setVisibility(View.VISIBLE);
                lblPass.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                if(contI==0){           //se non ho mai selezionato il pulsante prima
                    contI++;
                    contA=0;
                    Pulisci();          //layout (riga 265)
                    btnAmm.setClickable(true);
                }
            }
        });

        btnAmm.setOnClickListener(new View.OnClickListener() {      //pulsante amministratore
            @Override
            public void onClick(View v) {
                lblUser.setVisibility(View.VISIBLE);        //layout
                user.setVisibility(View.VISIBLE);
                lblPass.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                check.setVisibility(View.VISIBLE);
                if(contA==0){           //se non ho mai selezionato il pulsante prima
                    contA++;
                    contI=0;
                    Pulisci();          //layout (riga 265)
                    btnImp.setClickable(true);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {      //pulsante aggiungi
            @Override
            public void onClick(View v) {
                if(contI==0){               //se sono un amministratore procedo
                    Intent intent = new Intent(User.this, MenuAddEditDelete.class);
                    intent.putExtra("add", "add");
                    intent.putExtra("max",capacita);
                    startActivity(intent);
                }
                else        //altrimenti stampo un errore
                    Toast.makeText(User.this,"Non disponi delle autorizzazioni necessarie!",Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {     //pulsante modifica
            @Override
            public void onClick(View v) {
                if(contI==0){               //se sono un amministratore procedo
                    Intent intent = new Intent(User.this, MenuAddEditDelete.class);
                    intent.putExtra("edit", "edit");
                    startActivity(intent);
                }
                else        //altrimenti stampo un errore
                    Toast.makeText(User.this,"Non disponi delle autorizzazioni necessarie!",Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {       //pulsante elimina
            @Override
            public void onClick(View v) {
                if(contI==0){           //se sono un amministratore procedo
                    Intent intent = new Intent(User.this, MenuAddEditDelete.class);
                    intent.putExtra("delete", "delete");
                    startActivity(intent);
                }
                else           //altrimenti stampo un errore
                    Toast.makeText(User.this,"Non disponi delle autorizzazioni necessarie!",Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {     //pulsante visualizza
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, MenuViewSearch.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {       //pulsante cerca
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User.this, MenuViewSearch.class);
                intent.putExtra("search", "search");
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {     //pulsante impostazioni
            @Override
            public void onClick(View v) {
                if(contI==0){                   //se sono un amministratore procedo
                    LAUNCH_SETTINGS_ACTIVITY=0;     //variabile per l'intent
                    Intent intent = new Intent(User.this, MenuSettings.class);
                    intent.putExtra("cap",capacita);
                    startActivityForResult(intent, LAUNCH_SETTINGS_ACTIVITY);
                }else           //altrimenti stampo un errore
                    Toast.makeText(User.this,"Non disponi delle autorizzazioni necessarie!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Assegnazione(){            //funzione per assegnare alle variabili i componenti
        btnImp= (Button) findViewById(R.id.btnImpiegato);
        btnAmm= (Button) findViewById(R.id.btnAmministratore);
        btnAdd= (ImageButton) findViewById(R.id.imgAdd);
        btnEdit= (ImageButton) findViewById(R.id.imgEdit);;
        btnDelete= (ImageButton) findViewById(R.id.imgDelete);;
        btnView= (ImageButton) findViewById(R.id.imgView);;
        btnSearch= (ImageButton) findViewById(R.id.imgSearch);;
        btnSettings= (ImageButton) findViewById(R.id.imgSettings);;
        user=(EditText) findViewById(R.id.txtUser);
        pass=(EditText) findViewById(R.id.txtPass);
        lblAdd=(TextView) findViewById(R.id.lblAdd);
        lblEdit=(TextView) findViewById(R.id.lblEdit);
        lblDelete=(TextView) findViewById(R.id.lblDelete);
        lblView=(TextView) findViewById(R.id.lblView);
        lblSearch=(TextView) findViewById(R.id.lblSearch);
        lblSettings=(TextView) findViewById(R.id.lblSettings);
        lblUser=(TextView) findViewById(R.id.lblUser);
        lblPass=(TextView) findViewById(R.id.lblPass);
        check=(ImageButton) findViewById(R.id.imgCheck);
    }

    private void Mostra(){          //funzione per mostrare label e textview
        btnAdd.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.VISIBLE);
        btnView.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        btnSettings.setVisibility(View.VISIBLE);
        lblAdd.setVisibility(View.VISIBLE);
        lblEdit.setVisibility(View.VISIBLE);
        lblDelete.setVisibility(View.VISIBLE);
        lblView.setVisibility(View.VISIBLE);
        lblSearch.setVisibility(View.VISIBLE);
        lblSettings.setVisibility(View.VISIBLE);
    }

    private void Pulisci(){     //funzione per nascondere label e textview
        btnAdd.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        btnView.setVisibility(View.INVISIBLE);
        btnSearch.setVisibility(View.INVISIBLE);
        btnSettings.setVisibility(View.INVISIBLE);
        lblAdd.setVisibility(View.INVISIBLE);
        lblEdit.setVisibility(View.INVISIBLE);
        lblDelete.setVisibility(View.INVISIBLE);
        lblView.setVisibility(View.INVISIBLE);
        lblSearch.setVisibility(View.INVISIBLE);
        lblSettings.setVisibility(View.INVISIBLE);
    }

    private void CreaAlert(){       //funzione per creare gli alertDialogue
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);         //AlertDialog per gestire se l'utente inserisce
        builder.setTitle("Attenzione");                                              //un valore errato
        builder.setMessage("Credenziali errate!");
        builder.setCancelable(false);
        builder.setNeutralButton("Chiudi", new
                DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder2 = new androidx.appcompat.app.AlertDialog.Builder(this);         //AlertDialog per informare l'utente di inserire
        builder2.setTitle("Credenziali");                                              //le credenziali per la prima volta
        builder2.setMessage("Benvenuto! Siccome è la prima volta che accedi, inserisci le credenziali per l'accesso, dopodichè imposta la capacità massima del magazzino.");
        builder2.setCancelable(false);
        builder2.setNeutralButton("Chiudi", new
                DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LAUNCH_ACTIVITY){
            if(resultCode== Activity.RESULT_OK) {                       //controllo per ottenre le nuove credenziali
                userImp = data.getStringExtra("user1");
                passImp = data.getStringExtra("pass1");
                userAmm = data.getStringExtra("user2");
                passAmm = data.getStringExtra("pass2");
                capacita = data.getIntExtra("cap",0);
            }
        }
        else if(requestCode==LAUNCH_SETTINGS_ACTIVITY){         //controllo per ottenere la nuova capacità massima
            if(requestCode== Activity.RESULT_OK){
                capacita = data.getIntExtra("cap",0);
                EliminaShared();            //funzione per eliminare i vecchi valori e inserire i nuovi (riga 348)
            }
        }
        Salva(userImp,passImp,userAmm,passAmm,capacita);    //funzione per salvare i nuovi valori nel sharedPreferences (riga 328)
        Recupera();     //funzione per recuperare i valori del sharedPreferences (riga 339)
    }

    private void Salva(String a, String b, String c, String d, int e){      //funzione per salvare le credenziali e la capacità max
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user1", a);
        editor.putString("pass1", b);
        editor.putString("user2", c);
        editor.putString("pass2", d);
        editor.putInt("cap", e);
        editor.apply();
    }

    private void Recupera(){            //funzione per recuperare i valori dallo sharedPreferences
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        userImp = sharedPref.getString("user1", null);
        passImp = sharedPref.getString("pass1", null);
        userAmm = sharedPref.getString("user2", null);
        passAmm = sharedPref.getString("pass2", null);
        capacita = sharedPref.getInt("cap", 0);
    }

    private void EliminaShared(){       //funzione per svuotare la sharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}