package com.kaooak.android.homebookkeeping.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.Singleton;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AccountTransactionFragment extends Fragment {

    private static final String ARGUMENT_UUID = "argumentUuid";

    private RecyclerView mRecyclerView;

    private Account mAccount;

    public static AccountTransactionFragment newInstance(String uuid) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_UUID, uuid);

        AccountTransactionFragment fragment = new AccountTransactionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public AccountTransactionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        String accountUuid = bundle.getString(ARGUMENT_UUID);
        mAccount = Singleton.getInstance(getActivity()).selectAccount(accountUuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_transactions, container, false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new AccountTransactionFragment.TransactionsAdapter(Singleton.getInstance(getActivity()).selectTransactions(mAccount)));

        return mRecyclerView;
    }


    public class TransactionsAdapter extends RecyclerView.Adapter<AccountTransactionFragment.TransactionsAdapter.TransactionViewHolder> {

        private List<Transaction> mTransactions;

        public TransactionsAdapter(List<Transaction> items) {
            mTransactions = items;
        }

        @Override
        public AccountTransactionFragment.TransactionsAdapter.TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.transaction_item, parent, false);
            return new AccountTransactionFragment.TransactionsAdapter.TransactionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AccountTransactionFragment.TransactionsAdapter.TransactionViewHolder holder, int position) {
            Transaction transaction = mTransactions.get(position);
            holder.bind(transaction);
        }

        public void setTransactions(List<Transaction> transactions) {
            mTransactions = transactions;
        }
        public List<Transaction> getTransactions() {
            return mTransactions;
        }
        @Override
        public int getItemCount() {
            return mTransactions.size();
        }


        public class TransactionViewHolder extends RecyclerView.ViewHolder {
            private View mView;
            private TextView mTvDate;
            private TextView mTvValue;
            private TextView mTvComment;

            private Transaction mTransaction;

            public TransactionViewHolder(View view) {
                super(view);
                mView = view;
                mTvDate = (TextView) view.findViewById(R.id.tv_date);
                mTvValue = (TextView) view.findViewById(R.id.tv_value);
                mTvComment = (TextView) view.findViewById(R.id.tv_comment);
            }

            public void bind(Transaction transaction) {
                mTransaction = transaction;

                //
                DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
                mTvDate.setText(dateFormat.format(new Date(mTransaction.getDate())));

                double value = mTransaction.getValue();
                String valueStr = "";
                switch (mTransaction.getType()) {
                    case TransactionTypes.TYPE_PLUS:
                        valueStr = "+";
                        break;
                    case TransactionTypes.TYPE_MINUS:
                        valueStr = "-";
                        break;
                }
                switch (mTransaction.getCurrency()) {
                    case Currencies.CURRENCY_RUB:
                        valueStr = valueStr + String.format("%.2f", value) + " рублей";
                        break;
                    case Currencies.CURRENCY_DOLLAR:
                        valueStr = valueStr + String.format("%.2f", value) + " долларов (" + String.format("%.2f", value * ValuesSingleton.getmValueUSD()) + ") рублей";
                        break;
                    case Currencies.CURRENCY_EURO:
                        valueStr = valueStr + String.format("%.2f", value) + " евро (" + String.format("%.2f", value * ValuesSingleton.getmValueEUR()) + ") рублей";
                        break;
                }
                mTvValue.setText(valueStr);

                mTvComment.setText(mTransaction.getComment());
            }

        }
    }
}
