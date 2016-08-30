package com.google.sample.cloudvision.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vane on 30/08/2016.
 */
public class personaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "persona.bd";
    String sqlUpdate = "ALTER TABLE personaContract.personaEntry ADD COLUMN run TEXT";



    public personaDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > oldVersion){
            db.execSQL(sqlUpdate);
        }
          }

     @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + personaContract.personaEntry.TABLE_NAME + " ("
                + personaContract.personaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + personaContract.personaEntry.RUN + " TEXT NOT NULL,"
                + personaContract.personaEntry.APELLIDOS + " TEXT NOT NULL,"
                + personaContract.personaEntry.NOMBRES + " TEXT NOT NULL,"
                + "UNIQUE (" + personaContract.personaEntry.RUN + "))");

     }

    public int agregar(String run, String apellidos, String nombres){
        int id = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if(sqLiteDatabase!=null){
            ContentValues values = new ContentValues();
            values.put("RUN", run);
            values.put("APELLIDOS", apellidos);
            values.put("NOMBRES", nombres);
            id = (int) sqLiteDatabase.insert(personaContract.personaEntry.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        return id;

    }


    public int actualizar(String run, String apellidos, String nombres){
        int filasAfectadas = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if(sqLiteDatabase!=null){
            ContentValues values = new ContentValues();
            values.put("RUN", run);
            values.put("APELLIDOS", apellidos);
            values.put("NOMBRES", nombres);
            filasAfectadas = (int) sqLiteDatabase.update(personaContract.personaEntry.TABLE_NAME, values, "run_usuario = ?", new String[]{String.valueOf(run)});
        }
        sqLiteDatabase.close();
        return filasAfectadas;
    }


    public int deletepersona(String personaid) {
        return getWritableDatabase().delete(
                personaContract.personaEntry.TABLE_NAME,
                personaContract.personaEntry.RUN + " LIKE ?",
                new String[]{personaid});
    }



}
