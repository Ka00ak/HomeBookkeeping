package com.kaooak.android.homebookkeeping.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.database.DbContract;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class TransactionItemAdapter extends CursorAdapter {


    public TransactionItemAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //дата
        long millis = cursor.getLong(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.DATE));
        String dateStr = DateFormat.getDateFormat(context).format(new Date(millis));

        TextView tvTransactionDate = view.findViewById(R.id.tv_date);
        tvTransactionDate.setText(dateStr);

        //значение
        int value = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.VALUE));
        int currency = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY));
        int currencyValue = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY_VALUE));


        BigDecimal valueBd = new BigDecimal(String.valueOf(value / 100.0));
        BigDecimal currencyValueBd = new BigDecimal(String.valueOf(currencyValue / 100.0));

        BigDecimal currencyValueRubBd = valueBd.multiply(currencyValueBd).setScale(2, RoundingMode.HALF_EVEN);

        String valueStr;
        switch (currency) {
            case Currencies.CURRENCY_RUB:
                valueStr = valueBd.setScale(2, RoundingMode.HALF_EVEN) + " рублей ";
                break;
            case Currencies.CURRENCY_DOLLAR:
                valueStr = valueBd.setScale(2, RoundingMode.HALF_EVEN) + " долларов (" + currencyValueRubBd + " рублей)";
                break;
            case Currencies.CURRENCY_EURO:
                valueStr = valueBd.setScale(2, RoundingMode.HALF_EVEN) + " евро (" + currencyValueRubBd + " рублей)";
                break;
            default:
                valueStr = valueBd.setScale(2, RoundingMode.HALF_EVEN) + " рублей ";
                break;
        }

        TextView tvTransactionValue = view.findViewById(R.id.tv_value);
        tvTransactionValue.setText(valueStr);

        //комментарий
        String comment = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.COMMENT));

        TextView tvTransactionComment = view.findViewById(R.id.tv_comment);
        tvTransactionComment.setText(comment);
    }
}
