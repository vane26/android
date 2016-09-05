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
    private SQLiteDatabase sqLiteDatabase;
    private static SQLiteDatabase.CursorFactory factory = null;
    String sqlUpdate = "ALTER TABLE personaContract.personaEntry ADD COLUMN run TEXT";


    public registroDbHelper(Context context) {
        super(context, DATA_BASE, factory, VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL(sqlUpdate);

        } else
            onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + registroContract.registroEntry.TABLE_NAME + " ("
                + registroContract.registroEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + registroContract.registroEntry.INDICE + " TEXT,"
                + registroContract.registroEntry.TEXTO + " TEXT,"
                + registroContract.registroEntry.CALIDAD + " TEXT,"
                + "UNIQUE (" + registroContract.registroEntry.INDICE + "))");
        Log.i(this.getClass().toString(), "Tabla persona creada");

    }


    public int insert(registro registro) {
        int id = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE", registro.getIndice());
            values.put("TEXTO", registro.getTexto());
            values.put("CALIDAD DE IMAGEN", registro.getCalidad());
            id = (int) sqLiteDatabase.insert(registroContract.registroEntry.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        return id;

    }

    public int agregar(String texto) {
        int id = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {
            ContentValues values = new ContentValues();
            registro registro = new registro(texto);
            values.put("TEXTO", registro.getTexto());
            id = (int) sqLiteDatabase.insert(registroContract.registroEntry.TABLE_NAME, null, values);
        }
        sqLiteDatabase.close();
        return id;

    }



    public int update(registro registro) {
        int filasAfectadas = 0;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (sqLiteDatabase != null) {
            ContentValues values = new ContentValues();
            values.put("INDICE", registro.getIndice());
            values.put("TEXTO", registro.getTexto());
            values.put("CALIDAD DE IMAGEN", registro.getCalidad());
            filasAfectadas = (int) sqLiteDatabase.update(registroContract.registroEntry.TABLE_NAME, values, "indice_cadena = ?", new String[]{String.valueOf(registro.getIndice())});
        }
        sqLiteDatabase.close();
        return filasAfectadas;
    }


    public int delete(String registroindice) {
        return getWritableDatabase().delete(
                registroContract.registroEntry.TABLE_NAME,
                registroContract.registroEntry.INDICE + " LIKE ?",
                new String[]{registroindice});
    }

}