package com.kaooak.android.homebookkeeping.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.kaooak.android.homebookkeeping.R;

public class NewAccountFragment extends Fragment{

    public static final String BUNDLE_NEW_ACCOUNT_NAME = "newAccountName";
    public static final String BUNDLE_NEW_ACCOUNT_CURRENCY = "newAccountCurrency";
    public static final String BUNDLE_NEW_ACCOUNT_START_VALUE = "newAccountStartValue";

    private EditText mEtNewAccountName;
    private Spinner mSpinnerNewAccountCurrency;
    private EditText mEtNewAccountStartValue;

    public static NewAccountFragment newInstance() {
        NewAccountFragment fragment = new NewAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_account, container, false);

        mEtNewAccountName = view.findViewById(R.id.et_new_account_name);
        mSpinnerNewAccountCurrency = view.findViewById(R.id.spinner_new_account_currency);
        mEtNewAccountStartValue = view.findViewById(R.id.et_new_account_start_value);

        return view;
    }

    public Bundle getData() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_NEW_ACCOUNT_NAME, mEtNewAccountName.getText().toString());
        bundle.putInt(BUNDLE_NEW_ACCOUNT_CURRENCY, mSpinnerNewAccountCurrency.getSelectedItemPosition());
        bundle.putDouble(BUNDLE_NEW_ACCOUNT_START_VALUE, Double.valueOf(mEtNewAccountStartValue.getText().toString()));

        return bundle;
    }
}
