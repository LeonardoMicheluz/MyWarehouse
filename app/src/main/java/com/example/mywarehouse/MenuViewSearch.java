package com.example.mywarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuViewSearch extends AppCompatActivity {

    private Database db = new Database(this);;
    private ListView ls;
    private TextView lblcerca;
    private EditText cerca;
    private Button btnCerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_view_search);
        ls =(ListView) findViewById(R.id.lstMostra);
        lblcerca=(TextView) findViewById(R.id.lblCerca);
        cerca=(EditText) findViewById(R.id.txtCerca);
        btnCerca=(Button) findViewById(R.id.btnCerca);

        Intent i=getIntent();                               //riprendo i valori dall'activity precedente
        if(i.hasExtra("search")){
           Mostra();
        }

        ShowDatabase();     //funzione per mostrare il database

        btnCerca.setOnClickListener(new View.OnClickListener() {        //pulsante per cercare
            @Override
            public void onClick(View v) {
                Cursor cursor=db.Search((cerca.getText().toString()));
                if(cursor.getCount()!=0){
                    Stampa(cursor);
                    Toast.makeText(MenuViewSearch.this,"Oggetto trovato!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MenuViewSearch.this,"Oggetto inesistente!",Toast.LENGTH_SHORT).show();
                    ls.setAdapter(null);
                }
                cursor.close();
            }
        });
    }

    private void ShowDatabase(){                    //funzione per mostrare i record del database
        Cursor cursor = db.getInfo();       //riprendo tutti i record della tabella
        Stampa(cursor);         //chiamo la funzione sottostante
        cursor.close();
    }

    private void Stampa(Cursor cursor){     //funzione per stampare i record nella listview
        if(cursor.moveToFirst()){

            String[] info= new String[cursor.getCount()];
            int i=0;
            do{
                info[i] = "Codice:\t\t\t\t\t\t\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_ID))+"\n"+
                        "Descrizione:\t\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_DESCRIPTION))+"\n"+
                        "Categoria:\t\t\t\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_CATEGORY))+"\n"+
                        "Provenienza:\t\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_ORIGIN))+"\n"+
                        "Data di arrivo:\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_DATE)) +"\n"+
                        "Brand:\t\t\t\t\t\t\t\t" + cursor.getString(cursor.getColumnIndex(Database.KEY_BRAND));
                        i++;
            }while (cursor.moveToNext());

            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, info){

                //funzione per cambiare il colore del testo della listview
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);
                    return view;
                }
            };
            ls.setAdapter(adapter);     //metto l'arrayAdapter nella listview
            cursor.close();
        }
    }

    private void Mostra(){      //funzione per il layout
        btnCerca.setVisibility(View.VISIBLE);
        lblcerca.setVisibility(View.VISIBLE);
        cerca.setVisibility(View.VISIBLE);
    }
}