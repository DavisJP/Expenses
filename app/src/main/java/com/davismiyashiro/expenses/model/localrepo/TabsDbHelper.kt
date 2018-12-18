package com.davismiyashiro.expenses.model.localrepo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import timber.log.Timber

import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.*
import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.ParticipantTable
import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.TabTable
import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.ExpenseTable

/**
 * Class that takes care of the DBs DDL
 */
class TabsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        Timber.d("onCreate")

        //Using Java UUID as String in the PK
        db.execSQL("create table " + TabTable.TABLE_NAME +
                "(" +
                TabTable.UUID + " TEXT PRIMARY KEY, " +
                TabTable.GROUPNAME + " TEXT" +
                ")"
        )

        val CREATE_PARTICIPANTS_TABLE = """CREATE TABLE ${ParticipantTable.TABLE_NAME}
                (
                ${ParticipantTable.UUID} TEXT PRIMARY KEY,
                ${ParticipantTable.NAME} TEXT,
                ${ParticipantTable.EMAIL} TEXT,
                ${ParticipantTable.NUMBER} INTEGER,
                ${ParticipantTable.TAB_ID} TEXT,
                FOREIGN KEY( ${ParticipantTable.TAB_ID} )
                REFERENCES ${TabTable.TABLE_NAME} ( ${TabTable.UUID})
                ON UPDATE CASCADE ON DELETE CASCADE
                )"""

        db.execSQL(CREATE_PARTICIPANTS_TABLE)

        val CREATE_EXPENSES_TABLE = "CREATE TABLE " + ExpenseTable.TABLE_NAME +
                "(" +
                ExpenseTable.UUID + " TEXT PRIMARY KEY," +
                ExpenseTable.DESCRIPTION + " TEXT," +
                ExpenseTable.VALUE + " REAL," +
                ExpenseTable.TAB_ID + " TEXT, " +
                "FOREIGN KEY(" + ExpenseTable.TAB_ID + ") " +
                "REFERENCES " + TabTable.TABLE_NAME + "(" + TabTable.UUID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ")"

        db.execSQL(CREATE_EXPENSES_TABLE)

        /**
         * private String id;
         * private String participantId;
         * private String expenseId;
         * private int numberParticipants;
         * private double valueByParticipant;
         */
        val CREATE_SPLITS_TABLE = "CREATE TABLE " + SplitTable.TABLE_NAME +
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
                ")"

        db.execSQL(CREATE_SPLITS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Timber.d("onUpgrade")

        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TabTable.TABLE_NAME)
            db.execSQL("DROP TABLE IF EXISTS " + ParticipantTable.TABLE_NAME)
            db.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.TABLE_NAME)
            db.execSQL("DROP TABLE IF EXISTS " + SplitTable.TABLE_NAME)
            onCreate(db)
        }
    }

    companion object {
        val VERSION = 8
        val DATABASE_NAME = "tabsBase.db"
    }
}
