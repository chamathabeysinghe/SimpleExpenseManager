package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.provider.BaseColumns;

/**
 * Created by Chamath on 11/18/2016.
 */

/**
 * Database details
 */
public final class ExpenseManagerDBContract {

    private ExpenseManagerDBContract(){}

    public static class AccountTable implements BaseColumns{
        public static final String TABLE_NAME = "accounts";
        public static final String COLUMN_NAME_ACCOUNT_NO="account_no";
        public static final String COLUMN_NAME_BANK="bank";
        public static final String COLUMN_NAME_ACCOUNT_HOLDER="holder";
        public static final String COLUMN_NAME_INITIAL_BALANCE="balance";
    }

    public static class TransactionTable implements BaseColumns{
        public static final String TABLE_NAME = "transactions";
        public static final String COLUMN_NAME_ACCOUNT_NO="account_no";
        public static final String COLUMN_NAME_TRANSACTION_TYPE="type";
        public static final String COLUMN_NAME_AMOUNT="amount";
        public static final String COLUMN_NAME_DATE="date";
    }


}
