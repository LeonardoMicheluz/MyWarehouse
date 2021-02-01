package com.example.mywarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;

public class Credenziali extends AppCompatActivity {    //activity per settare le credenziali degli utenti

    private EditText userImp;
    private EditText passImp;
    private EditText userAmm;
    private EditText passAmm;
    private EditText capacita;
    private Button btnImposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credenziali);

        userImp = (EditText) findViewById(R.id.txtUserImp);
        userAmm = (EditText) findViewById(R.id.txtUserAmm);
        passImp = (EditText) findViewById(R.id.txtPassImp);
        passAmm = (EditText) findViewById(R.id.txtPassAmm);
        capacita = (EditText) findViewById(R.id.txtCapacita);
        btnImposta = (Button) findViewById(R.id.btnImposta);

        btnImposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //ritorno indietro i valori inseriti

                if(Controllo()){        //controllo dell'input
                    Intent intent = new Intent();
                    intent.putExtra("user1", userImp.getText().toString());
                    intent.putExtra("pass1", passImp.getText().toString());
                    intent.putExtra("user2", userAmm.getText().toString());
                    intent.putExtra("pass2", passAmm.getText().toString());
                    intent.putExtra("cap",Integer.parseInt(capacita.getText().toString()));
                    setResult(RESULT_OK, intent);
                    finish();
                }else
                    Toast.makeText(Credenziali.this,"Inserisci i valori corretti!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean Controllo(){        //funzione per controllare l'input
        if(userImp.getText().toString().isEmpty())
            return false;
        else if(userAmm.getText().toString().isEmpty())
            return false;
        else if(passAmm.getText().toString().isEmpty())
            return false;
        else if(passImp.getText().toString().isEmpty())
            return false;
        else if(capacita.getText().toString().isEmpty())
            return false;
        else if(Integer.parseInt(capacita.getText().toString())<=0)
            return false;
        else
            return true;
    }
}