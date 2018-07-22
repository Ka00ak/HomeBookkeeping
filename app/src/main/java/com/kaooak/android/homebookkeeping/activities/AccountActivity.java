package com.kaooak.android.homebookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.fragments.AccountFragment;
import com.kaooak.android.homebookkeeping.fragments.AccountTransactionFragment;

public class AccountActivity extends AppCompatActivity implements AccountFragment.CallbackListener{

    private static final String EXTRA_UUID = "com.kaooak.android.homebookkeeping.activities.accountactivity.extra.uuid";

    public static Intent getIntent(Context context, String uuid) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_tabs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String uuid = intent.getStringExtra(EXTRA_UUID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_account = fragmentManager.findFragmentById(R.id.container_fragment_accounts);
        if (fragment_account == null) {
            fragment_account = AccountFragment.newInstance(uuid);
            fragmentManager.beginTransaction()
                    .add(R.id.container_fragment_accounts, fragment_account)
                    .commit();
        }

        Fragment fragment_transactions = fragmentManager.findFragmentById(R.id.container_fragment_transactions);
        if (fragment_transactions == null) {
            fragment_transactions = AccountTransactionFragment.newInstance(uuid);
            fragmentManager.beginTransaction()
                    .add(R.id.container_fragment_transactions, fragment_transactions)
                    .commit();
        }

        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag 1");
        tabSpec.setContent(R.id.container_fragment_accounts);
        tabSpec.setIndicator("Данные");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tag 2");
        tabSpec2.setContent(R.id.container_fragment_transactions);
        tabSpec2.setIndicator("Транзакции");
        tabHost.addTab(tabSpec2);

        tabHost.setCurrentTab(0);
    }

    @Override
    public void just() {
        finish();
    }
}
