package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vane on 31/08/2016.
 */

public class registroDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "";
    public static int version = 2;
    public static String db_path = "/data/data/com.google.sample.cloudvision.BD/databases/";
    public static String data_base = "registro_db.csv";


    // SQLiteDatabase db;
    registroDbHelper db;
    private final Context myContext;
    private static SQLiteDatabase.CursorFactory factory = null;

    String sqlCreate = "CREATE TABLE registro (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "indice TEXT, texto TEXT, calidad TEXT)";
    String sqlUpdate = "ALTER TABLE registro ADD COLUMN indice TEXT";


    public registroDbHelper(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
        this.myContext = context;
        //SQLiteDatabase.openOrCreateDatabase("/mnt/sdcard/" + nombre, null);

    }


    @Override
    public void onCreate(SQLiteDatabase db) { // codigo para crear base de datos
        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }
        db.execSQL(sqlCreate);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //codigo en caso de querer crear mas campos en nuestra base de datos
        if (newVersion > oldVersion) {
            db.execSQL(sqlUpdate);
        }
    }

    //conexiones
    public void abrir() {
        Log.i("SQLite ", "Se abre conexion a la base de datos " + db.getWritableDatabase());

    }


    public void close() {
        if (db != null)
            Log.i("SQLite ", "Se cierra conexion a la base de datos ");
        db.close();
    }


    //metodos insert, update, query
    public void insert(registro registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE ", registro.getIndice());
            values.put("TEXTO ", registro.getTexto());
            values.put("CALIDAD ", registro.getCalidad());
            db.insert(registroContract.registroEntry.table_name, null, values);
        }
        db.close();
    }

    public void agregar(String texto) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            registro registro = new registro(texto);
            values.put("TEXTO ", registro.getTexto());
            db.insert(registroContract.registroEntry.table_name, null, values);
        }
        db.close();


    }



    public int update(registro registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("INDICE ", registro.getIndice());
        values.put("TEXTO ", registro.getTexto());
        values.put("CALIDAD ", registro.getCalidad());
        return db.update(registroContract.registroEntry.table_name, values, "indice_cadena = ?", new String[]{String.valueOf(registro.getIndice())});
        }





    public void delete(registro registro) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(registroContract.registroEntry.table_name, registro.getIndice() + " = ?", new String[]{String.valueOf(registro.getIndice())});
    }


    public List<registro> ListadoGeneral() {
        List<registro> listado = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + registroContract.registroEntry.table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                registro registro = new registro();
                registro.setIndice(cursor.getString(0));
                registro.setTexto(cursor.getString(1));
                registro.setCalidad(cursor.getString(2));
                listado.add(registro);

            } while (cursor.moveToNext());
        }
        return listado;
    }


    public List<registro> ListadoGeneraluno() {
        List<registro> listado = new ArrayList<>();
        String selectQuery = "SELECT TEXTO FROM " + registroContract.registroEntry.table_name;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                registro registro = new registro();
                registro.setTexto(cursor.getString(0));
                listado.add(registro);

            } while (cursor.moveToNext());
        }
        return listado;
    }


    public int getRegistro(){
        String countQuery = "SELECT * FROM " + registroContract.registroEntry.table_name;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }



}








