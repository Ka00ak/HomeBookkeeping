package com.kaooak.android.homebookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.Singleton;
import com.kaooak.android.homebookkeeping.fragments.NewTransactionFragment;

public class NewTransactionActivity extends AppCompatActivity {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, NewTransactionActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_account = fragmentManager.findFragmentById(R.id.container_fragment);
        if (fragment_account == null) {
            fragment_account = NewTransactionFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container_fragment, fragment_account)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_transaction:
                FragmentManager fragmentManager = getSupportFragmentManager();
                NewTransactionFragment fragment = (NewTransactionFragment) fragmentManager.findFragmentById(R.id.container_fragment);
                Transaction transaction = fragment.getData();
                Singleton.getInstance(this).insertTransaction(transaction);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
