package com.kaooak.android.homebookkeeping.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.JSON.GsonData;
import com.kaooak.android.homebookkeeping.data.RetrofitCB;
import com.kaooak.android.homebookkeeping.fragments.AccountsListFragment;
import com.kaooak.android.homebookkeeping.fragments.TransactionsListFragment;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private int currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                mDrawerLayout.closeDrawers();

                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.menu_drawer_item_accounts:
                        setTitle("Счета");

                        fragmentManager.beginTransaction()
                                .replace(R.id.container_fragment, AccountsListFragment.newInstance())
                                .commit();
                        return true;
                    case R.id.menu_drawer_item_transactions:
                        setTitle("Транзакции");

                        fragmentManager.beginTransaction()
                                .replace(R.id.container_fragment, TransactionsListFragment.newInstance())
                                .commit();
                        return true;
                    default:
                        return false;
                }
            }
        });

        new RetrofitAsyncTask().execute("");

        currentFragment = 0;
        setTitle("Счета");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, AccountsListFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//
//            }
//        }
//    }

    private class RetrofitAsyncTask extends AsyncTask<String, Void, GsonData> {

        @Override
        protected GsonData doInBackground(String... strings) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.cbr-xml-daily.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitCB retrofitCB = retrofit.create(RetrofitCB.class);

            Call<GsonData> cur = retrofitCB.getData();

            try {
                GsonData gsonData = cur.execute().body();
                return gsonData;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected synchronized void onPostExecute(GsonData gsonData) {
            super.onPostExecute(gsonData);

            if (gsonData != null) {
                double valueUSD = gsonData.getValute().getUSD().getValue();
                double valueEUR = gsonData.getValute().getEUR().getValue();

                ValuesSingleton.setmValueRUB(1);
                ValuesSingleton.setmValueUSD(valueUSD);
                ValuesSingleton.setmValueEUR(valueEUR);

                if (currentFragment == 0) {
                    AccountsListFragment accountsListFragment = (AccountsListFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment);
                    accountsListFragment.updateView();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Для работы с другими валютами необходимо подключение к интернету", Toast.LENGTH_LONG).show();
            }
        }

    }
}
