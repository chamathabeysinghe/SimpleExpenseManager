package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDBContract.AccountTable;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDBContract.TransactionTable;
/**
 * Created by Chamath on 11/18/2016.
 */

/**
 * Database initialization
 */
public class ExpenseManagerDBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " VARCHAR";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String DATE_TYPE=" DATE";

    private static final String COMMA_SEP = ",";


    
    private static final String SQL_CREATE_ACCOUNT =
            "CREATE TABLE " + AccountTable.TABLE_NAME + " (" +
                    AccountTable.COLUMN_NAME_ACCOUNT_NO + TEXT_TYPE+" PRIMARY KEY "+COMMA_SEP +
                    AccountTable.COLUMN_NAME_BANK + TEXT_TYPE + COMMA_SEP +
                    AccountTable.COLUMN_NAME_ACCOUNT_HOLDER + TEXT_TYPE + COMMA_SEP+
                    AccountTable.COLUMN_NAME_INITIAL_BALANCE+ DOUBLE_TYPE+" )";

    private static final String SQL_DELETE_ACCOUNT_TABLE =
            "DROP TABLE IF EXISTS " + AccountTable.TABLE_NAME;


    private static final String SQL_CREATE_ENTRIES_TRANSACTION =
            "CREATE TABLE " + TransactionTable.TABLE_NAME + " (" +
                    TransactionTable._ID + " INTEGER PRIMARY KEY "+COMMA_SEP +
                    TransactionTable.COLUMN_NAME_ACCOUNT_NO + TEXT_TYPE + COMMA_SEP +
                    TransactionTable.COLUMN_NAME_DATE + DATE_TYPE + COMMA_SEP+
                    TransactionTable.COLUMN_NAME_TRANSACTION_TYPE + TEXT_TYPE +COMMA_SEP+
                    TransactionTable.COLUMN_NAME_AMOUNT+ DOUBLE_TYPE+COMMA_SEP+
                    " FOREIGN KEY("+ TransactionTable.COLUMN_NAME_ACCOUNT_NO+") REFERENCES "+ AccountTable.TABLE_NAME+"("+ AccountTable.COLUMN_NAME_ACCOUNT_NO+")"+
                    " );";

    private static final String SQL_DELETE_ENTRIES_TRANSACTION =
            "DROP TABLE IF EXISTS " + TransactionTable.TABLE_NAME;



    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "140007P.db";

    public ExpenseManagerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACCOUNT);
        db.execSQL(SQL_CREATE_ENTRIES_TRANSACTION);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ACCOUNT_TABLE);
        db.execSQL(SQL_DELETE_ENTRIES_TRANSACTION);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }
}
