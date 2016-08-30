package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Vane on 30/08/2016.
 */
public class personaDbHelper extends SQLiteOpenHelper {
    private static int VERSION = 1;
    private static String DATA_BASE = "persona_db";
    private SQLiteDatabase db;
    private static SQLiteDatabase.CursorFactory factory = null;
     String sqlUpdate = "ALTER TABLE personaContract.personaEntry ADD COLUMN run TEXT";



    public personaDbHelper(Context context){
        super(context, DATA_BASE, factory, VERSION);
    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL(sqlUpdate);

        }
        else
            onCreate(db);
          }

     @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + personaContract.personaEntry.TABLE_NAME + " ("
                + personaContract.personaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + personaContract.personaEntry.RUN + " TEXT NOT NULL,"
                + personaContract.personaEntry.APELLIDOS + " TEXT NOT NULL,"
                + personaContract.personaEntry.NOMBRES + " TEXT NOT NULL,"
                + "UNIQUE (" + personaContract.personaEntry.RUN + "))");
         Log.i(this.getClass().toString(), "Tabla persona creada");

     }


    public int agregar(persona persona){
        int id = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if(sqLiteDatabase!=null){
            ContentValues values = new ContentValues();
            values.put("RUN", persona.getRun());
            values.put("APELLIDOS", persona.getApellidos());
            values.put("NOMBRES", persona.getNombres());
            id = (int) sqLiteDatabase.insert(personaContract.personaEntry.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        return id;

    }


    public int actualizar(persona persona){
        int filasAfectadas = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if(sqLiteDatabase!=null){
            ContentValues values = new ContentValues();
            values.put("RUN", persona.getRun());
            values.put("APELLIDOS",persona.getApellidos());
            values.put("NOMBRES", persona.getNombres());
            filasAfectadas = (int) sqLiteDatabase.update(personaContract.personaEntry.TABLE_NAME, values, "run_persona = ?", new String[]{String.valueOf(persona.getRun())});
        }
        sqLiteDatabase.close();
        return filasAfectadas;
    }


    public int borrar(String personaid) {
        return getWritableDatabase().delete(
                personaContract.personaEntry.TABLE_NAME,
                personaContract.personaEntry.RUN + " LIKE ?",
                new String[]{personaid});
    }


   }
