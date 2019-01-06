/*
 * MIT License
 *
 * Copyright (c) 2019 Davis Miyashiro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.davismiyashiro.expenses.model.localrepo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import timber.log.Timber

import com.davismiyashiro.expenses.model.localrepo.TabDbSchema.* // ktlint-disable no-wildcard-imports
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

        // Using Java UUID as String in the PK
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
