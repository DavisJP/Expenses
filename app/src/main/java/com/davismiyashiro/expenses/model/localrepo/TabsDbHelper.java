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
        db.execSQL("create table " + TabTable.NAME +
                "(" +
                TabTable.Cols.UUID + " TEXT PRIMARY KEY, " +
                TabTable.Cols.GROUPNAME + " TEXT" +
                ")"
        );

        String CREATE_PARTICIPANTS_TABLE = "CREATE TABLE " + ParticipantTable.NAME +
                "(" +
                ParticipantTable.Cols.UUID + " TEXT PRIMARY KEY," +
                ParticipantTable.Cols.NAME + " TEXT," +
                ParticipantTable.Cols.EMAIL + " TEXT," +
                ParticipantTable.Cols.NUMBER + " INTEGER," +
                ParticipantTable.Cols.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + ParticipantTable.Cols.TAB_ID + ") " +
                "REFERENCES " + TabTable.NAME + "(" + TabTable.Cols.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_PARTICIPANTS_TABLE);

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + ExpenseTable.NAME +
                "(" +
                ExpenseTable.Cols.UUID + " TEXT PRIMARY KEY," +
                ExpenseTable.Cols.DESCRIPTION + " TEXT," +
                ExpenseTable.Cols.VALUE + " REAL," +
                ExpenseTable.Cols.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + ExpenseTable.Cols.TAB_ID + ") " +
                "REFERENCES " + TabTable.NAME + "(" + TabTable.Cols.UUID + ") " +
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
        String CREATE_SPLITS_TABLE = "CREATE TABLE " + SplitTable.NAME +
                "(" +
                SplitTable.Cols.UUID + " TEXT PRIMARY KEY," +
                SplitTable.Cols.PARTICIPANT_ID + " TEXT," +
                SplitTable.Cols.EXPENSE_ID + " TEXT," +
                SplitTable.Cols.SPLIT_VAL + " REAL," +
                SplitTable.Cols.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + SplitTable.Cols.TAB_ID + ") " +
                "REFERENCES " + TabTable.NAME + "(" + TabTable.Cols.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(" + SplitTable.Cols.PARTICIPANT_ID + ") " +
                "REFERENCES " + ParticipantTable.NAME + "(" + ParticipantTable.Cols.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(" + SplitTable.Cols.EXPENSE_ID + ") " +
                "REFERENCES " + ExpenseTable.NAME + "(" + ExpenseTable.Cols.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_SPLITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.d("onUpgrade");

        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TabTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ParticipantTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SplitTable.NAME);
            onCreate(db);
        }
    }
}
