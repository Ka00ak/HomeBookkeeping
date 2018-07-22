package com.kaooak.android.homebookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.database.Singleton;
import com.kaooak.android.homebookkeeping.fragments.NewAccountFragment;
import com.kaooak.android.homebookkeeping.fragments.TransactionsListFragment;

public class NewAccountActivity extends AppCompatActivity{

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, NewAccountActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment);
        if (fragment == null) {
            fragment = NewAccountFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container_fragment, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_account:
                FragmentManager fragmentManager = getSupportFragmentManager();
                NewAccountFragment fragment = (NewAccountFragment) fragmentManager.findFragmentById(R.id.container_fragment);
                Bundle bundle = fragment.getData();

                String newAccountName = bundle.getString(NewAccountFragment.BUNDLE_NEW_ACCOUNT_NAME);
                int newAccountCurrency = bundle.getInt(NewAccountFragment.BUNDLE_NEW_ACCOUNT_CURRENCY);
                double newAccountStartValue = bundle.getDouble(NewAccountFragment.BUNDLE_NEW_ACCOUNT_START_VALUE);

                Account account = new Account(newAccountName, newAccountCurrency, newAccountStartValue, newAccountStartValue);
                Singleton.getInstance(this).insertAccount(account);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
