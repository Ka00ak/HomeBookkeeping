package com.kaooak.android.homebookkeeping.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.ValuesRecounter;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.database.Singleton;

public class AccountFragment extends Fragment {

    private static final String ARGUMENT_UUID = "uuid";

    private CallbackListener mCallbackListener;

    private EditText mEtAccountName;
    private Spinner mSpinnerAccountCurrenncy;
    private EditText mEtAccountStartValue;

    private Account mAccount;

    public interface CallbackListener {
        void just();
    }

    public static AccountFragment newInstance(String uuid) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_UUID, uuid);

        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle args = getArguments();
        String uuid = args.getString(ARGUMENT_UUID);

        mAccount = Singleton.getInstance(getActivity()).selectAccount(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mEtAccountName = view.findViewById(R.id.et_account_name);
        mEtAccountName.setText(mAccount.getName());
        mEtAccountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAccount.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSpinnerAccountCurrenncy = view.findViewById(R.id.spinner_account_currency);
        mSpinnerAccountCurrenncy.setSelection(mAccount.getCurrency());
        mSpinnerAccountCurrenncy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int currentCurrency = mAccount.getCurrency();
                double currentValue = ValuesSingleton.getmValueRUB();
                switch (currentCurrency) {
                    case Currencies.CURRENCY_RUB:
                        currentValue = ValuesSingleton.getmValueRUB();
                        break;
                    case Currencies.CURRENCY_DOLLAR:
                        currentValue = ValuesSingleton.getmValueUSD();
                        break;
                    case Currencies.CURRENCY_EURO:
                        currentValue = ValuesSingleton.getmValueEUR();
                        break;
                }

                int newCurrency = i;
                double newValue = ValuesSingleton.getmValueRUB();
                switch (newCurrency) {
                    case Currencies.CURRENCY_RUB:
                        newValue = ValuesSingleton.getmValueRUB();
                        break;
                    case Currencies.CURRENCY_DOLLAR:
                        newValue = ValuesSingleton.getmValueUSD();
                        break;
                    case Currencies.CURRENCY_EURO:
                        newValue = ValuesSingleton.getmValueEUR();
                        break;
                }

                double result = ValuesRecounter.recount(mAccount.getValue(), currentValue, newValue);

                mAccount.setValue(result);
                mAccount.setCurrency(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEtAccountStartValue = view.findViewById(R.id.et_account_start_value);
        mEtAccountStartValue.setText(String.valueOf(mAccount.getStartValue()));
        mEtAccountStartValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAccount.setStartValue(Double.valueOf(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountFragment.CallbackListener)
            mCallbackListener = (AccountFragment.CallbackListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save:
                Singleton.getInstance(getActivity()).updateAccount(mAccount);

                if (mCallbackListener != null)
                    mCallbackListener.just();

                return true;
            default:
                return false;
        }
    }
}
