package com.davismiyashiro.expenses.model.localrepo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.*;
import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.ParticipantTable;
import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.TabTable;
import static com.davismiyashiro.expenses.model.localrepo.TabDbSchema.ExpenseTable;

/**
 * Class that takes care of the DBs DDL
 */
public class TabsDbHelper extends SQLiteOpenHelper {

    public static final int VERSION = 8;
    public static final String DATABASE_NAME = "tabsBase.db";

    public TabsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("onCreate");

        //Using Java UUID as String in the PK
        db.execSQL("create table " + TabTable.TABLE_NAME +
                "(" +
                TabTable.UUID + " TEXT PRIMARY KEY, " +
                TabTable.GROUPNAME + " TEXT" +
                ")"
        );

        String CREATE_PARTICIPANTS_TABLE = "CREATE TABLE " + ParticipantTable.TABLE_NAME +
                "(" +
                ParticipantTable.UUID + " TEXT PRIMARY KEY," +
                ParticipantTable.NAME + " TEXT," +
                ParticipantTable.EMAIL + " TEXT," +
                ParticipantTable.NUMBER + " INTEGER," +
                ParticipantTable.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + ParticipantTable.TAB_ID + ") " +
                "REFERENCES " + TabTable.TABLE_NAME + "(" + TabTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_PARTICIPANTS_TABLE);

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + ExpenseTable.TABLE_NAME +
                "(" +
                ExpenseTable.UUID + " TEXT PRIMARY KEY," +
                ExpenseTable.DESCRIPTION + " TEXT," +
                ExpenseTable.VALUE + " REAL," +
                ExpenseTable.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + ExpenseTable.TAB_ID + ") " +
                "REFERENCES " + TabTable.TABLE_NAME + "(" + TabTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_EXPENSES_TABLE);

        /**
         private String id;
         private String participantId;
         private String expenseId;
         private int numberParticipants;
         private double valueByParticipant;
         */
        String CREATE_SPLITS_TABLE = "CREATE TABLE " + SplitTable.TABLE_NAME +
                "(" +
                SplitTable.UUID + " TEXT PRIMARY KEY," +
                SplitTable.PARTICIPANT_ID + " TEXT," +
                SplitTable.EXPENSE_ID + " TEXT," +
                SplitTable.SPLIT_VAL + " REAL," +
                SplitTable.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + SplitTable.TAB_ID + ") " +
                "REFERENCES " + TabTable.TABLE_NAME + "(" + TabTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(" + SplitTable.PARTICIPANT_ID + ") " +
                "REFERENCES " + ParticipantTable.TABLE_NAME + "(" + ParticipantTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(" + SplitTable.EXPENSE_ID + ") " +
                "REFERENCES " + ExpenseTable.TABLE_NAME + "(" + ExpenseTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_SPLITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.d("onUpgrade");

        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TabTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ParticipantTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SplitTable.TABLE_NAME);
            onCreate(db);
        }
    }
}
