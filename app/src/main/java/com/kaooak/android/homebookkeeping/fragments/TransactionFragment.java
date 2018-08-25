package com.kaooak.android.homebookkeeping.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.ValuesRecounter;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.Singleton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionFragment extends Fragment {

    private static final String ARGUMENT_UUID = "uuid";

    private Transaction mTransaction;

    private RadioGroup mRgroupTransactionType;
    private Button mBtnTransactionDate;
    private Spinner mSpinnerTransactionAccount;
    private EditText mEtTransactionValue;
    private Spinner mSpinnerTransactionCurrenncy;
    private EditText mEtTransactionComment;

    public static TransactionFragment newInstance(String uuid) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_UUID, uuid);

        TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        String uuid = args.getString(ARGUMENT_UUID);

        mTransaction = Singleton.getInstance(getActivity()).selectTransaction(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_transaction, container, false);

//        mRgroupTransactionType = view.findViewById(R.id.rgroup_transaction_type);
//        switch (mTransaction.getType()) {
//            case TransactionTypes.TYPE_PLUS:
//                mRgroupTransactionType.check(R.id.rbtn_in);
//                break;
//            case TransactionTypes.TYPE_MINUS:
//                mRgroupTransactionType.check(R.id.rbtn_out);
//                break;
//        }

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

        int index = -1;
        for (int i=0; i < list.size(); i++)
            if (mTransaction.getAccountOneUUID().equals(list.get(i).getUUID()))
                index = i;
        mSpinnerTransactionAccount.setSelection(index);

        mEtTransactionValue = view.findViewById(R.id.et_transaction_value);
        mEtTransactionValue.setText(String.valueOf(mTransaction.getValue()));

        mSpinnerTransactionCurrenncy = view.findViewById(R.id.spinner_transaction_currency);
        mSpinnerTransactionCurrenncy.setSelection(mTransaction.getCurrency());

        mEtTransactionComment = view.findViewById(R.id.et_transaction_comment);
        mEtTransactionComment.setText(String.valueOf(mTransaction.getComment()));

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

        int currency = mSpinnerTransactionCurrenncy.getSelectedItemPosition();

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

        mTransaction.setType(transactionType);
        mTransaction.setDate(date);
        mTransaction.setAccountOneUUID(account_uuid);
        mTransaction.setAccountTwoUUID(account_uuid);
        mTransaction.setValue(value);
        mTransaction.setCurrency(currency);
        mTransaction.setRate(rate);
        mTransaction.setComment(comment);
        return mTransaction;
    }
}
