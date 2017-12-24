package com.logistics.sj.appv1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by AJ on 03-05-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private  static  final  String DATABASE_FILE_NAME="truckDB1.db";
    private static  final Integer DATABASE_VERSION =1;

    public DBHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("tagg","oncreate");

        db.execSQL("DROP TABLE IF EXISTS "+Contract.TableColumns.TRUCK_TABLE);

        final String CREATE_TABLE_QUERY="CREATE TABLE "+
                Contract.TableColumns.TRUCK_TABLE+" ("+
                Contract.TableColumns._ID +" INTEGER,"+
                Contract.TableColumns.TRUCK_NUMBER+" TEXT PRIMARY KEY NOT NULL,"+
                Contract.TableColumns.TRUCK_OWNER_NAME+" TEXT ,"+
                Contract.TableColumns.TRUCK_OWNER_NUMBER_ARRAY+" TEXT ,"+
                Contract.TableColumns.ACCOUNT_NUMBER_ARRAY+" TEXT);";

       try {
           db.execSQL(CREATE_TABLE_QUERY);
       }catch (Exception e)
       {
           Log.i("tagg","create table exception");
       }
        Log.i("tagg","create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("tagg","onUpgarde");

        try {
            db.execSQL("DROP TABLE IF EXISTS "+Contract.TableColumns.TRUCK_TABLE);
            onCreate(db);
        }catch (Exception e)
        {
            Log.i("tagg","create table");
        }
    }
}
