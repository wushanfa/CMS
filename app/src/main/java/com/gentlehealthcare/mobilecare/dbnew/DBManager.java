package com.gentlehealthcare.mobilecare.dbnew;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.gentlehealthcare.mobilecare.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zyy on 2016/4/8.
 */
public class DBManager {

    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "mobilecare.db";
    public static final String PACKAGE_NAME = "com.gentlehealthcare.nursemobile";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
            PACKAGE_NAME;
    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbFilePath) {

        InputStream is = null;
        FileOutputStream fos = null;
        File file = new File(dbFilePath);

        if (!file.exists()) {
            try {
                is = this.context.getResources().openRawResource(R.raw.mobilecare);
                fos = new FileOutputStream(dbFilePath);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
            } catch (FileNotFoundException e) {
                Log.e("Database", "File not found");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Database", "IO exception");
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (database == null) {
            database = SQLiteDatabase.openOrCreateDatabase(dbFilePath, null);
        }
        return database;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void closeDatabase() {
        this.database.close();
    }
}
