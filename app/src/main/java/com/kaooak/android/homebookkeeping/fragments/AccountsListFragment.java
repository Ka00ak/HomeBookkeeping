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
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.activities.AccountActivity;
import com.kaooak.android.homebookkeeping.activities.NewAccountActivity;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.database.Singleton;

import java.util.List;

public class AccountsListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;

    public AccountsListFragment() {
    }

    public static AccountsListFragment newInstance() {
        AccountsListFragment fragment = new AccountsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setAdapter(new AccountsAdapter(Singleton.getInstance(getActivity()).selectAccounts()));

        mFloatingActionButton = (FloatingActionButton)  view.findViewById(R.id.button_add_account);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = NewAccountActivity.getIntent(getActivity());
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    public void updateView() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }


    private class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

        private List<Account> mAccounts;

        public AccountsAdapter(List<Account> items) {
            mAccounts = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.account_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Account account = mAccounts.get(position);
            holder.bind(account);
        }

        public void setAccounts(List<Account> accounts) {
            mAccounts = accounts;
        }
        public List<Account> getAccounts() {
            return mAccounts;
        }
        @Override
        public int getItemCount() {
            return mAccounts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements MenuItem.OnMenuItemClickListener{
            private View mView;
            private TextView mTextName;
            private TextView mTextValue;
            private TextView mTextValueRub;

            private Account mAccount;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextName = (TextView) view.findViewById(R.id.text_name);
                mTextValue = (TextView) view.findViewById(R.id.text_value);
                mTextValueRub = (TextView) view.findViewById(R.id.text_value_rub);
            }

            public void bind(Account account) {
                mAccount = account;
                double value = mAccount.getValue();

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = AccountActivity.getIntent(getActivity(), mAccount.getUUID());
                        startActivity(intent);
                    }
                });
                mView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        MenuInflater menuInflater = new MenuInflater(view.getContext());
                        menuInflater.inflate(R.menu.context_menu_account, contextMenu);
                        MenuItem item = contextMenu.findItem(R.id.delete_item);
                        item.setOnMenuItemClickListener(ViewHolder.this);
                    }
                });

                mTextName.setText(mAccount.getName());
                switch (mAccount.getCurrency()) {
                    case Currencies.CURRENCY_RUB:
                        mTextValue.setText(String.format("%.2f",value) + " рублей");
                        mTextValueRub.setText("");
                        break;
                    case Currencies.CURRENCY_DOLLAR:
//                        BigDecimal decimal = new BigDecimal(mValueUSD);
//                        BigDecimal decimal1= new BigDecimal(value);
//                        BigDecimal result = decimal.multiply(decimal1);
//                        BigDecimal sto = new BigDecimal(100);
//                        int i = result.multiply(sto).intValue();

                        mTextValue.setText(String.format("%.2f",value) + " долларов");
                        mTextValueRub.setText(String.format("%.2f", value * ValuesSingleton.getmValueUSD()) + " рублей");
                        break;
                    case Currencies.CURRENCY_EURO:
                        mTextValue.setText(String.format("%.2f",value) + " евро");
                        mTextValueRub.setText(String.format("%.2f", value * ValuesSingleton.getmValueEUR()) + " рублей");
                        break;
                }
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_item:
                        int index = mRecyclerView.getChildLayoutPosition(itemView);
                        Account account = ((AccountsAdapter) mRecyclerView.getAdapter()).getAccounts().get(index);
                        Singleton.getInstance(getActivity()).deleteAccount(account);

                        ((AccountsAdapter) mRecyclerView.getAdapter()).setAccounts(Singleton.getInstance(getActivity()).selectAccounts());
                        ((AccountsAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
                        return true;
                    default:
                        return true;
                }
            }
        }
    }
}
