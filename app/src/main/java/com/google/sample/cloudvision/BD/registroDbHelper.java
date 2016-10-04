package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Vane on 31/08/2016.
 */

public class registroDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "";
    public static int version = 2;
    public static String db_path = "/data/data/com.google.sample.cloudvision.BD/databases/";
    public static String data_base = "registro_db.csv";



    private String INICIO_PROCESO = "Comienza el proceso";
    private String FIN_PROCESO = "Finaliza el proceso tardo: %s:%s:%s";
    private String FORMATO_DOS_DIGITOS = "00";

    public Date fechaInicio;

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

    public int update(String texto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        registro registro = new registro(texto);
        values.put("TEXTO ", registro.getTexto());
        return db.update(registroContract.registroEntry.table_name, values, "indice_cadena = ?", new String[]{String.valueOf(registro.getTexto())});
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



    public void comenzarProceso() {
        System.out.println(INICIO_PROCESO);
        this.fechaInicio = new Date();
    }



    public void finalizaProceso(){

        Date fechaFin = new Date();
        Long tiempoTranscurrido = fechaFin.getTime() - fechaInicio.getTime();

        Long diffSeconds = tiempoTranscurrido / 1000 % 60;
        Long diffMinutes = tiempoTranscurrido / (60 * 1000) % 60;
        Long diffHours = tiempoTranscurrido / (60 * 60 * 1000) % 24;

        DecimalFormat df = new DecimalFormat(FORMATO_DOS_DIGITOS);
        System.out.println(String.format(FIN_PROCESO, df.format(diffHours), df.format(diffMinutes), df.format(diffSeconds)));
    }


    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = db_path + data_base;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(data_base);
        // Path to the just created empty db
        String outFileName = db_path + data_base;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }







}








