package com.kaooak.android.homebookkeeping.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.Toast;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.data.JSON.GsonData;
import com.kaooak.android.homebookkeeping.data.RetrofitCB;
import com.kaooak.android.homebookkeeping.database.DbContract;
import com.kaooak.android.homebookkeeping.fragments.AccountsListFragment;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private int currentFragment;

    private Spinner mSpinner;
//    private long mAccountId;

    private SimpleCursorAdapter mSimpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_accounts);

        Log.d(TAG, "onCreate: ");

        getSupportLoaderManager().initLoader(0, null ,this);

        Toolbar toolbar = findViewById(R.id.toolbar_accounts);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        mSimpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null, new String[] { "name"}, new int[] { android.R.id.text1}, 0);
        mSimpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner = toolbar.findViewById(R.id.spinner_accounts);
        mSpinner.setAdapter(mSimpleCursorAdapter);

//        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                mAccountId = l;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


//        mDrawerLayout = findViewById(R.id.drawer_layout);
//        mNavigationView = findViewById(R.id.navigation_view);
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
//
//                mDrawerLayout.closeDrawers();
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                switch (item.getItemId()) {
//                    case R.id.menu_drawer_item_accounts:
//                        setTitle("Счета");
//
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.container_fragment, AccountsListFragment.newInstance())
//                                .commit();
//                        return true;
//                    case R.id.menu_drawer_item_transactions:
//                        setTitle("Транзакции");
//
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.container_fragment, TransactionsListFragment.newInstance())
//                                .commit();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });

//        new RetrofitAsyncTask().execute("");
//
//        currentFragment = 0;
//        setTitle("Счета");
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container_fragment, AccountsListFragment.newInstance())
//                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_accounts_new_account:
                Intent intent = AccountActivity.getIntent(this);
                intent.setData(null);
                startActivity(intent);
                return true;
            case R.id.menu_accounts_edit_account:
                Intent intent2 = AccountActivity.getIntent(this);
                intent2.setData(ContentUris.withAppendedId(DbContract.AccountsTable.CONTENT_URI, mSpinner.getSelectedItemId()));
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(this, DbContract.AccountsTable.CONTENT_URI, null, null, null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");
        mSimpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mSimpleCursorAdapter.swapCursor(null);
    }
    //

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
