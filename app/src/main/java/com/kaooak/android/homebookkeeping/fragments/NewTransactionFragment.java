package com.kaooak.android.homebookkeeping.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.Singleton;

import java.util.ArrayList;
import java.util.Date;

public class NewTransactionFragment extends Fragment {

    private RadioGroup mRgroupTransactionType;
    private Button mBtnTransactionDate;
    private Spinner mSpinnerTransactionAccount;
    private EditText mEtTransactionValue;
    private Spinner mSpinnerTransactionCurrency;
    private EditText mEtTransactionComment;

    public static NewTransactionFragment newInstance() {
        NewTransactionFragment fragment = new NewTransactionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_transaction, container, false);

//        mRgroupTransactionType = view.findViewById(R.id.rgroup_transaction_type);

        mBtnTransactionDate = view.findViewById(R.id.btn_transaction_date);
        mBtnTransactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayList<Account> list = Singleton.getInstance(getActivity()).selectAccounts();
        ArrayAdapter<Account> adp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
//        mSpinnerTransactionAccount = view.findViewById(R.id.spinner_transaction_account_one);
        mSpinnerTransactionAccount.setAdapter(adp);

        mEtTransactionValue = view.findViewById(R.id.et_transaction_value);
        mSpinnerTransactionCurrency = view.findViewById(R.id.spinner_transaction_currency);
        mEtTransactionComment = view.findViewById(R.id.et_transaction_comment);

        return view;
    }

    public Transaction getData() {

        int transactionType = TransactionTypes.TYPE_PLUS;
//        switch (mRgroupTransactionType.getCheckedRadioButtonId()) {
//            case R.id.rbtn_in:
//                transactionType = TransactionTypes.TYPE_PLUS;
//                break;
//            case R.id.rbtn_out:
//                transactionType = TransactionTypes.TYPE_MINUS;
//                break;
//            default:
//                transactionType = TransactionTypes.TYPE_PLUS;
//                break;
//        }

        long date = new Date().getTime();

        String account_uuid = ((Account) mSpinnerTransactionAccount.getSelectedItem()).getUUID();

        double value = Double.valueOf(mEtTransactionValue.getText().toString());

        int currency = mSpinnerTransactionCurrency.getSelectedItemPosition();

        double rate;
        switch (currency) {
            case Currencies.CURRENCY_RUB:
                rate = ValuesSingleton.getmValueRUB();
                break;
            case Currencies.CURRENCY_DOLLAR:
                rate = ValuesSingleton.getmValueUSD();
                break;
            case Currencies.CURRENCY_EURO:
                rate = ValuesSingleton.getmValueEUR();
                break;
            default:
                rate = ValuesSingleton.getmValueRUB();
                break;
        }

        String comment = mEtTransactionComment.getText().toString();

        Transaction transaction = new Transaction(transactionType, date, account_uuid, account_uuid, value, currency, rate,  comment);
        return transaction;
    }
}
