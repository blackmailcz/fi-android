package uco_448237.movio.pv526.fi.muni.cz.ukol3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BlackMail on 3.1.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    // DB
    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "movies.db";

    public DBHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MovieProviderWrapper.MovieEntityContants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieProviderWrapper.MovieEntityContants.TABLE_NAME);
        onCreate(db);
    }

}
