package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Vane on 31/08/2016.
 */

public class registroDbHelper extends SQLiteOpenHelper {
    public static int version = 2;
    public static String db_path = "/data/data/com.google.sample.cloudvision.BD/databases/";
    public static String data_base = "registro_db";
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
        db.close();
    }


    public void cerrar() {
        Log.i("SQLite ", "Se cierra conexion a la base de datos " + db.getDatabaseName());
        db.close();
    }


    //metodos insert, update, query
    public int insert(registro registro) {
        int id = 0;
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE ", registro.getIndice());
            values.put("TEXTO ", registro.getTexto());
            values.put("CALIDAD ", registro.getCalidad());
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
            values.put("TEXTO ", registro.getTexto());
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
            values.put("INDICE ", registro.getIndice());
            values.put("TEXTO ", registro.getTexto());
            values.put("CALIDAD ", registro.getCalidad());
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


    public void backupdDatabase(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String packageName  = "com.google.sample.cloudvision.BD";
            String sourceDBName = "registro_db";
            String targetDBName = "registro";
            if (sd.canWrite()) {
                Date now = new Date();
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String backupDBPath = targetDBName + dateFormat.format(now) + ".db";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                Log.i("backup","backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup","sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            Log.i("Backup", e.toString());
        }
    }



}