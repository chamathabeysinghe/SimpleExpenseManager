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
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDBContract.AccountTable;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is a database implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO implements AccountDAO,Serializable {

    private transient SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase database) {
        this.database=database;
    }
    @Override
    public List<String> getAccountNumbersList() {
        String sql="SELECT "+ AccountTable.COLUMN_NAME_ACCOUNT_NO+" FROM "+ AccountTable.TABLE_NAME;
        Cursor cursor=database.rawQuery(sql,null);
        List<String> accountNumberList=new ArrayList<>();
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_ACCOUNT_NO));

            accountNumberList.add(accountNo);
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        String sql="SELECT * FROM "+ AccountTable.TABLE_NAME;
        Cursor cursor=database.rawQuery(sql,null);
        List<Account> accountList=new ArrayList<>();
        while(cursor.moveToNext()){
            String accountNo=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_ACCOUNT_NO));
            String bank=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_BANK));
            String accountHolder=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_ACCOUNT_HOLDER));
            double balance=cursor.getDouble(cursor.getColumnIndex(AccountTable.COLUMN_NAME_INITIAL_BALANCE));
            Account account=new Account(accountNo,bank,accountHolder,balance);
            accountList.add(account);
        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String sql="SELECT * FROM "+ AccountTable.TABLE_NAME+" WHERE "+ AccountTable.COLUMN_NAME_ACCOUNT_NO+" = '"+accountNo+"';";
        Cursor cursor=database.rawQuery(sql,null);
        if(cursor.getCount()==0){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else{
            cursor.moveToFirst();
            String bank=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_BANK));
            String accountHolder=cursor.getString(cursor.getColumnIndex(AccountTable.COLUMN_NAME_ACCOUNT_HOLDER));
            double balance=cursor.getDouble(cursor.getColumnIndex(AccountTable.COLUMN_NAME_INITIAL_BALANCE));
            Account account=new Account(accountNo,bank,accountHolder,balance);
            return  account;
        }

    }

    @Override
    public void addAccount(Account account) {
        String sql="INSERT INTO "+ AccountTable.TABLE_NAME+"("+ AccountTable.COLUMN_NAME_ACCOUNT_NO+","+ AccountTable.COLUMN_NAME_BANK+","+
                AccountTable.COLUMN_NAME_ACCOUNT_HOLDER+","+ AccountTable.COLUMN_NAME_INITIAL_BALANCE+") VALUES(?,?,?,?);";
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindString(1,account.getAccountNo());
        statement.bindString(2,account.getBankName());
        statement.bindString(3,account.getAccountHolderName());
        statement.bindDouble(4,account.getBalance());
        try{
            statement.executeInsert();
        }
        catch (SQLiteConstraintException ex){
            Log.e("Error","Integrity error occurred");
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql="DELETE FROM "+ AccountTable.TABLE_NAME+" WHERE "+ AccountTable.COLUMN_NAME_ACCOUNT_NO+" = ?;";
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindString(1,accountNo);
        statement.executeUpdateDelete();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account=getAccount(accountNo);


        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        String sql="UPDATE "+ AccountTable.TABLE_NAME+" SET "+ AccountTable.COLUMN_NAME_INITIAL_BALANCE+" = ? WHERE "+
              AccountTable.COLUMN_NAME_ACCOUNT_NO+" = ? ;"  ;
        SQLiteStatement statement=database.compileStatement(sql); //avoid sql injection
        statement.bindDouble(1,account.getBalance());
        statement.bindString(2,accountNo);
        statement.executeUpdateDelete();
    }
}
