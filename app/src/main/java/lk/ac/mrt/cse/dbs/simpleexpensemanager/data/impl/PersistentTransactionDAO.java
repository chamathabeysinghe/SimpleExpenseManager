
/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDBContract.TransactionTable;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an database implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO implements TransactionDAO,Serializable {

    private transient SQLiteDatabase database;
    public PersistentTransactionDAO(SQLiteDatabase database) {
        this.database=database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {


        String sql="INSERT INTO "+ TransactionTable.TABLE_NAME+"("+ TransactionTable.COLUMN_NAME_ACCOUNT_NO+","+ TransactionTable.COLUMN_NAME_TRANSACTION_TYPE+","+
                TransactionTable.COLUMN_NAME_DATE+","+ TransactionTable.COLUMN_NAME_AMOUNT+") VALUES(?,?,?,?);";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.bindString(1,accountNo);
        statement.bindString(2,expenseType.name());
        statement.bindString(3,date.toString());
        statement.bindDouble(4,amount);

        statement.executeInsert();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        String sql="SELECT * FROM "+ TransactionTable.TABLE_NAME;
        Cursor cursor=database.rawQuery(sql,null);
        List<Transaction> transactionList=new ArrayList<>();
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_ACCOUNT_NO));
            double amount=cursor.getDouble(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_AMOUNT));
            String type=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_TRANSACTION_TYPE));

            String date=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_DATE));
            Transaction transaction=new Transaction(new Date(date),accountNo, ExpenseType.valueOf(type),amount);
            transactionList.add(transaction);
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {


        String sql="SELECT * FROM "+ TransactionTable.TABLE_NAME+" LIMIT "+limit;
        Cursor cursor=database.rawQuery(sql,null);
        List<Transaction> transactionList=new ArrayList<>();
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_ACCOUNT_NO));
            double amount=cursor.getDouble(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_AMOUNT));
            String type=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_TRANSACTION_TYPE));

            String date=cursor.getString(cursor.getColumnIndex(TransactionTable.COLUMN_NAME_DATE));
            Transaction transaction=new Transaction(new Date(date),accountNo, ExpenseType.valueOf(type),amount);
            transactionList.add(transaction);
        }

        return  transactionList;
    }

}
