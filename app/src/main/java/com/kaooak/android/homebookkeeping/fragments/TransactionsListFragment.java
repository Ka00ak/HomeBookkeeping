package com.kaooak.android.homebookkeeping.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.activities.NewTransactionActivity;
import com.kaooak.android.homebookkeeping.activities.TransactionActivity;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.Singleton;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TransactionsListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    public TransactionsListFragment() {
    }

    public static TransactionsListFragment newInstance() {
        TransactionsListFragment fragment = new TransactionsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(new TransactionsAdapter(Singleton.getInstance(getActivity()).selectTransactions()));

        mFloatingActionButton = (FloatingActionButton)  view.findViewById(R.id.button_add_transaction);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Transaction transaction = new Transaction(TransactionTypes.TYPE_PLUS, new Date(), "", Currencies.CURRENCY_RUB, 0, 0);
//                Singleton.getInstance(getActivity()).insertAccount(account);
                Intent intent = NewTransactionActivity.getIntent(getActivity());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }


    public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

        private List<Transaction> mTransactions;

        public TransactionsAdapter(List<Transaction> items) {
            mTransactions = items;
        }

        @Override
        public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.transaction_item, parent, false);
            return new TransactionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TransactionViewHolder holder, int position) {
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



        public class TransactionViewHolder extends RecyclerView.ViewHolder implements MenuItem.OnMenuItemClickListener{
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

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = TransactionActivity.getIntent(mView.getContext(), mTransaction.getUUID());
                        mView.getContext().startActivity(intent);
                    }
                });
                mView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        MenuInflater menuInflater = new MenuInflater(view.getContext());
                        menuInflater.inflate(R.menu.context_menu_transaction, contextMenu);
                        MenuItem item = contextMenu.findItem(R.id.delete_item);
                        item.setOnMenuItemClickListener(TransactionsListFragment.TransactionsAdapter.TransactionViewHolder.this);
                    }
                });

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
                        valueStr = valueStr + String.format("%.2f", value) + " долларов (" + String.format("%.2f", value * mTransaction.getRate()) + ") рублей";
                        break;
                    case Currencies.CURRENCY_EURO:
                        valueStr = valueStr + String.format("%.2f", value) + " евро (" + String.format("%.2f", value * mTransaction.getRate()) + ") рублей";
                        break;
                }
                mTvValue.setText(valueStr);

                mTvComment.setText(mTransaction.getComment());
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_item:
                        int index = mRecyclerView.getChildLayoutPosition(itemView);
                        Transaction transaction = ((TransactionsAdapter) mRecyclerView.getAdapter()).getTransactions().get(index);
                        Singleton.getInstance(getActivity()).deleteTransaction(transaction);

                        ((TransactionsAdapter) mRecyclerView.getAdapter()).setTransactions(Singleton.getInstance(getActivity()).selectTransactions());
                        ((TransactionsAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
                        return true;
                    default:
                        return true;
                }
            }
        }
    }
}
