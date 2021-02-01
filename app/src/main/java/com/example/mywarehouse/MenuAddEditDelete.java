package com.example.mywarehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MenuAddEditDelete extends AppCompatActivity {

    private Database db= new Database(this);
    private String comando="";      //variabile per capire quale azione eseguire tra aggiunta, modifica e elimina
    private int quantita;           //quantità massima del magazzino
    private double risultato=0;     //variabile per controllare la quantità di oggetti nel magazzino e stampare i toast

    private EditText txtCod;
    private EditText txtDes;
    private EditText txtCat;
    private EditText txtProv;
    private EditText txtData;
    private EditText txtBrand;
    private TextView lblCod;
    private TextView lblDes;
    private TextView lblCat;
    private TextView lblProv;
    private TextView lblData;
    private TextView lblBrand;
    private Button btnComando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add_edit_delete);

        txtCod=(EditText) findViewById(R.id.txt0);
        txtDes=(EditText) findViewById(R.id.txt1);
        txtCat=(EditText) findViewById(R.id.txt2);
        txtProv=(EditText) findViewById(R.id.txt3);
        txtData=(EditText) findViewById(R.id.txt4);
        txtBrand=(EditText) findViewById(R.id.txt5);
        lblCod=(TextView) findViewById(R.id.lbl0);
        lblDes=(TextView) findViewById(R.id.lbl1);
        lblCat=(TextView) findViewById(R.id.lbl2);
        lblProv=(TextView) findViewById(R.id.lbl3);
        lblData=(TextView) findViewById(R.id.lbl4);
        lblBrand=(TextView) findViewById(R.id.lbl5);
        btnComando=(Button) findViewById(R.id.btnComando);

        Intent i=getIntent();                               //riprendo i valori dall'activity precedente

        if(i.hasExtra("max")){
            quantita=i.getIntExtra("max",0);
        }
        if(i.hasExtra("add")){
            comando=i.getStringExtra("add");
            btnComando.setText("Aggiungi");
            lblCod.setText("Inserire la quantità:");
            risultato= (db.Count()*1.0/quantita*100);       //% del magazzino occupato

            if(risultato>=80 && risultato<100)      //controllo per i toast
                Toast.makeText(MenuAddEditDelete.this,"Il magazzino è quasi pieno!", Toast.LENGTH_SHORT).show();
            else if(risultato==100)
                Toast.makeText(MenuAddEditDelete.this,"Il magazzino è pieno!", Toast.LENGTH_SHORT).show();
        }
        if(i.hasExtra("edit")){
            comando=i.getStringExtra("edit");
            btnComando.setText("Modifica");
            lblCod.setText("Inserire il codice:");
        }
        if(i.hasExtra("delete")){
            comando=i.getStringExtra("delete");
            btnComando.setText("Elimina");
            lblCod.setText("Inserire il codice:");
            Nascondi();
        }

        btnComando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comando.toString().equals("add")){       //se bisogna aggiungere faccio controlli e aggiungo n elementi
                    if(Controllo()){
                        if((Integer.parseInt(txtCod.getText().toString()))+db.Count()>quantita)
                            Toast.makeText(MenuAddEditDelete.this,"Il magazzino è pieno!", Toast.LENGTH_SHORT).show();
                        else {
                            for(int i=0; i<Integer.parseInt(txtCod.getText().toString()); i++)
                                db.Add(txtDes.getText().toString(),txtCat.getText().toString(),txtProv.getText().toString(),txtData.getText().toString(),txtBrand.getText().toString());
                            if(Integer.parseInt(txtCod.getText().toString())==1)
                                Toast.makeText(MenuAddEditDelete.this,"1 oggetto aggiunto!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MenuAddEditDelete.this,"" +
                                        Integer.parseInt(txtCod.getText().toString()) + " oggetti aggiunti!", Toast.LENGTH_SHORT).show();
                        }
                    }else
                        Toast.makeText(MenuAddEditDelete.this,"è sorto un problema. Per piacere, riprova", Toast.LENGTH_SHORT).show();
                    }
                else if(comando.toString().equals("edit")){     //se bisogna modificare faccio controlli e modifico un elemento
                    if(!txtCod.getText().toString().isEmpty()){
                        if(db.Search(Integer.parseInt(txtCod.getText().toString())).getCount()!=0){
                            db.Modify(Integer.parseInt(txtCod.getText().toString()), txtDes.getText().toString(),txtCat.getText().toString(),txtProv.getText().toString(),txtData.getText().toString(),txtBrand.getText().toString());
                            Toast.makeText(MenuAddEditDelete.this,"oggetto modificato!", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(MenuAddEditDelete.this,"codice inesistente!",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MenuAddEditDelete.this,"è sorto un problema. Per piacere, riprova", Toast.LENGTH_SHORT).show();
                }
                else if(comando.toString().equals("delete")){    //se bisogna modificare faccio controlli e elimino un elemento
                    if(Controllo()){
                        if(db.Search(Integer.parseInt(txtCod.getText().toString())).getCount()!=0){
                            db.Delete(Integer.parseInt(txtCod.getText().toString()));
                            Toast.makeText(MenuAddEditDelete.this,"oggetto eliminato!", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(MenuAddEditDelete.this,"codice inesistente!",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(MenuAddEditDelete.this,"è sorto un problema. Per piacere, riprova", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Nascondi(){        //funzione per nascondere label e textview
        txtDes.setVisibility(View.INVISIBLE);
        lblDes.setVisibility(View.INVISIBLE);
        txtCat.setVisibility(View.INVISIBLE);
        lblCat.setVisibility(View.INVISIBLE);
        txtProv.setVisibility(View.INVISIBLE);
        lblProv.setVisibility(View.INVISIBLE);
        txtData.setVisibility(View.INVISIBLE);
        lblData.setVisibility(View.INVISIBLE);
        txtBrand.setVisibility(View.INVISIBLE);
        lblBrand.setVisibility(View.INVISIBLE);
    }

    private boolean Controllo(){        //funzione per controllare gli input
        if(txtDes.getVisibility()==View.VISIBLE &&txtDes.getText().toString().isEmpty())
            return false;
        else if(txtCat.getVisibility()==View.VISIBLE &&txtCat.getText().toString().isEmpty())
            return false;
        else if(txtProv.getVisibility()==View.VISIBLE &&txtProv.getText().toString().isEmpty())
            return false;
        else if(txtData.getVisibility()==View.VISIBLE &&txtData.getText().toString().isEmpty())
            return false;
        else if(txtBrand.getVisibility()==View.VISIBLE &&txtBrand.getText().toString().isEmpty())
            return false;
        else if(txtCod.getVisibility()==View.VISIBLE && txtCod.getText().toString().isEmpty())
            return false;
        else if(Integer.parseInt(txtCod.getText().toString())<=0)
            return false;
        else
            return true;
    }
}
