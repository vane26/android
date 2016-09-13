package com.google.sample.cloudvision;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.sample.cloudvision.BD.registroContract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Vane on 13/09/2016.
 */
public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
    public static SQLiteDatabase simpledb;

    private android.content.Context ctx;
    private final ProgressDialog dialog = new ProgressDialog(ctx);
    // can use UI thread here
    @Override
    protected void onPreExecute()
    {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    protected Boolean doInBackground(final String... args) {
        String currentDBPath = "/data/data/com.google.sample.cloudvision.BD/databases/registro_db";
        File dbFile=getDatabasePath("registro_db");

        System.out.println(dbFile);  // displays the data base path in your logcat
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/sdcard/registro_db");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "registro.csv");
        try {
            file.createNewFile();
            CsvWriter csvWrite = new CsvWriter(new FileWriter(file));

            Cursor curCSV = simpledb.rawQuery("select * from " + registroContract.registroEntry.table_name, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1),
                        curCSV.getString(2),curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            return true;
        }
        catch(SQLException sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            return false;
        }
        catch (IOException e)
        {
            Log.e("MainActivity", e.getMessage(), e);
            return false;
        }
    }
    public File getDatabasePath(String s) {
        return getDatabasePath(s);
    }
}



