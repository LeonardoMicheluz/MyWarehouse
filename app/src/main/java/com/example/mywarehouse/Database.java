package com.example.mywarehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {        //database di oggetti

    protected static final String DATABASE_NAME = "Storage";
    protected static final String KEY_ID = "id";
    protected static final String KEY_DESCRIPTION = "description";
    protected static final String KEY_CATEGORY = "category";
    protected static final String KEY_ORIGIN = "origin";
    protected static final String KEY_DATE = "date";
    protected static final String KEY_BRAND = "brand";
    protected static final String TAB_NAME = "items";
    protected static final int VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {       //tabella di oggetti
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TAB_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_ORIGIN + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_BRAND + " TEXT)";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TAB_NAME);
        this.onCreate(db);
    }

    public void Add(String des, String cat, String pro, String data, String brand) {    //aggiungi un oggetto
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DESCRIPTION, des);
        contentValues.put(KEY_CATEGORY, cat);
        contentValues.put(KEY_ORIGIN, pro);
        contentValues.put(KEY_DATE, data);
        contentValues.put(KEY_BRAND, brand);
        sqLiteDatabase.insert(TAB_NAME,null, contentValues);
        sqLiteDatabase.close();
    }

    public Cursor getInfo() {       //ritorna tutti i record della tabella
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TAB_NAME + ";";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public void Modify(int cod,String des, String cat, String pro, String dat, String bran){    //modifica un oggetto dato il codice
        SQLiteDatabase db = this.getWritableDatabase();

        if(!des.isEmpty())
            db.execSQL("UPDATE "+TAB_NAME+" SET description = "+"'"+des+"' "+ "WHERE id = "+"'"+cod+"'");
        if(!cat.isEmpty())
            db.execSQL("UPDATE "+TAB_NAME+" SET category = "+"'"+cat+"' "+ "WHERE id = "+"'"+cod+"'");
        if(!pro.isEmpty())
            db.execSQL("UPDATE "+TAB_NAME+" SET origin = "+"'"+pro+"' "+ "WHERE id = "+"'"+cod+"'");
        if(!dat.isEmpty())
            db.execSQL("UPDATE "+TAB_NAME+" SET date = "+"'"+dat+"' "+ "WHERE id = "+"'"+cod+"'");
        if(!bran.isEmpty())
            db.execSQL("UPDATE "+TAB_NAME+" SET brand = "+"'"+bran+"' "+ "WHERE id = "+"'"+cod+"'");
    }

    public void Delete(int cod){        //elimina un oggetto
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TAB_NAME+" WHERE id= "+"'"+cod+"'");
    }

    public Cursor Search(int cod){      //cerca un oggetto dato il codice
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TAB_NAME + " WHERE id = "+"'"+cod+"';";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public Cursor Search(String des){       //cerca un oggetto data la descrizione
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TAB_NAME + " WHERE description = "+"'"+des+"';";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public void DeleteAll() {       //elimina tutti i record della tabella
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TAB_NAME);
    }

    public int Count(){         //ritorna il numero di record nella tabella
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TAB_NAME);
        db.close();
        int x= (int) count;
        return x;
    }
}