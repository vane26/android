package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vane on 31/08/2016.
 */
public class registroDbHelper extends SQLiteOpenHelper {
    private static int VERSION = 1;
    private static String DATA_BASE = "registro_db";
    SQLiteDatabase db;
    registroDbHelper registroDbHelper;
    private static SQLiteDatabase.CursorFactory factory = null;



    public registroDbHelper(Context context) {
        super(context, DATA_BASE, factory, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + registroContract.registroEntry.TABLE_NAME + " ("
                + registroContract.registroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + registroContract.registroEntry.INDICE + " TEXT,"
                + registroContract.registroEntry.TEXTO + " TEXT,"
                + registroContract.registroEntry.CALIDAD + " TEXT,"
                + "UNIQUE (" + registroContract.registroEntry.INDICE + "))");
        Log.i(this.getClass().toString(), "Tabla persona creada");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DATA_BASE);
        onCreate(db);
    }


    public void abrir(){
        Log.i("SQLite", "Se cierra conexion a la base de datos " + registroDbHelper.getDatabaseName());
        db = registroDbHelper.getWritableDatabase();
    }


    public void cerrar(){
        Log.i("SQLite", "Se cierra conexion a la base de datos " + registroDbHelper.getDatabaseName());
        registroDbHelper.close();
    }


    public int insert(registro registro) {
        int id = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE", registro.getIndice());
            values.put("TEXTO", registro.getTexto());
            values.put("CALIDAD DE IMAGEN", registro.getCalidad());
            id = (int) db.insert(registroContract.registroEntry.TABLE_NAME, null, values);
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
            id = (int) db.insert(registroContract.registroEntry.TABLE_NAME, null, values);
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
            filasAfectadas = (int) db.update(registroContract.registroEntry.TABLE_NAME, values, "indice_cadena = ?", new String[]{String.valueOf(registro.getIndice())});
        }
        db.close();
        return filasAfectadas;
    }


    public int delete(String registroindice) {
        return getWritableDatabase().delete(
                registroContract.registroEntry.TABLE_NAME,
                registroContract.registroEntry.INDICE + " LIKE ?",
                new String[]{registroindice});
    }







}