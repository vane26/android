package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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





    private boolean compruebadb(){
        boolean checkdb = false;
        String patch = registroDbHelper.db_path + registroDbHelper.data_base;
        File ficherodb = new File(patch);
        checkdb = ficherodb.exists();
        return checkdb;
    }


    public void crearbd(){
        boolean existe = compruebadb();
        if(existe){

        }else {
            this.getWritableDatabase();
            copiabd();
        }
    }

    public void copiabd(){
        try {
            //String currentDBPath = "/data/data/com.google.sample.cloudvision.BD/databases/registro_db";
            InputStream in = myContext.getAssets().open(registroDbHelper.data_base);
           // String ruta = registroDbHelper.db_path + registroDbHelper.data_base;
            String ruta = "/sdcard/registro.db";
            OutputStream salida = new FileOutputStream(ruta);

            byte[]buffer = new byte[1024];
            int tam;
            while ((tam = in.read(buffer)) > 0){
                salida.write(buffer, 0, tam);
            }
            salida.flush();
            salida.close();
            in.close();
        }catch (Exception e){

        }
    }
}