package com.upiita.witcomcheck;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by oscar on 21/10/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String COORDINATE_TABLE_NAME = "Registro";
    private static final String COORDINATE_TABLE_CREATE =
            "CREATE TABLE `Registro` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`codigo` TEXT," +
                    "`tipo` TEXT," +
                    "`fecha` TEXT," +
                    "`hora` TEXT" +
                    ");";
    DataBaseHelper (Context context) {
        super(context, COORDINATE_TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(COORDINATE_TABLE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
