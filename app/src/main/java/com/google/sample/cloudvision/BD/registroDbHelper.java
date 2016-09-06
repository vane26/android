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
    private static int version = 1;
    private static String data_base= "registro_db";
    SQLiteDatabase db;
    registroDbHelper registroDbHelper;
    private static SQLiteDatabase.CursorFactory factory = null;



    public registroDbHelper(Context context) {
        super(context, data_base, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + registroContract.registroEntry.table_name + " ("
                + registroContract.registroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + registroContract.registroEntry.indice + " TEXT,"
                + registroContract.registroEntry.texto + " TEXT,"
                + registroContract.registroEntry.calidad + " TEXT,"
                + "UNIQUE (" + registroContract.registroEntry._ID + "))");
        Log.i(this.getClass().toString(), "Tabla persona creada");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + data_base);
        onCreate(db);
    }

//conexiones
    public void abrir(){
        Log.i("SQLite", "Se cierra conexion a la base de datos " + registroDbHelper.getDatabaseName());
        registroDbHelper.close();
    }


    public void cerrar(){
        Log.i("SQLite", "Se cierra conexion a la base de datos " + registroDbHelper.getDatabaseName());
        registroDbHelper.close();
    }



    //metodos
    public int insert(registro registro) {
        int id = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE", registro.getIndice());
            values.put("TEXTO", registro.getTexto());
            values.put("CALIDAD DE IMAGEN", registro.getCalidad());
            id = (int) db.insert(registroContract.registroEntry.table_name, null, values);
        }
        db.close();
        return id;

    }

    public int agregar(String texto) {
        int id = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            registro registro = new registro(texto);
            values.put("TEXTO", registro.getTexto());
            id = (int) db.insert(registroContract.registroEntry.table_name, null, values);
        }
        db.close();
        return id;

    }


    public int update(registro registro) {
        int filasAfectadas = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE", registro.getIndice());
            values.put("TEXTO", registro.getTexto());
            values.put("CALIDAD DE IMAGEN", registro.getCalidad());
            filasAfectadas = (int) db.update(registroContract.registroEntry.table_name, values, "indice_cadena = ?", new String[]{String.valueOf(registro.getIndice())});
        }
        db.close();
        return filasAfectadas;
    }


    public int delete(String registroindice) {
        return getWritableDatabase().delete(
                registroContract.registroEntry.table_name,
                registroContract.registroEntry.indice + " LIKE ?",
                new String[]{registroindice});
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


}