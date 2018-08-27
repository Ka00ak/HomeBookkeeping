package com.kaooak.android.homebookkeeping.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.ValuesSingleton;
import com.kaooak.android.homebookkeeping.adapter.TransactionItemAdapter;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.JSON.GsonData;
import com.kaooak.android.homebookkeeping.data.RetrofitCB;
import com.kaooak.android.homebookkeeping.database.DbContract;

import java.math.BigDecimal;
import java.math.RoundingMode;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int LOADER_ACCOUNTS = 0;
    public static final int LOADER_TRANSACTIONS_ID = 1;
    public static final int LOADER_ACCOUNTS_ID = 2;

    private Spinner mSpinner;
    private long mAccountId = 1;
    private int mAccountCurrentValue = 0;
    private int mAccountCurrency = 1;

    private SimpleCursorAdapter mSimpleCursorAdapter;

    private TextView mTvAccountName;
    private TextView mTvAccountCurrentValue;

    private ListView mListView;
    private TransactionItemAdapter mTransactionItemAdapter;

    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        Log.d(TAG, "onCreate: ");


        Toolbar toolbar = findViewById(R.id.toolbar_accounts);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        mTvAccountName = findViewById(R.id.tv_account_name);
        mTvAccountCurrentValue = findViewById(R.id.tv_account_current_value);

        mSimpleCursorAdapter = new SimpleCursorAdapter (
                this,
                android.R.layout.simple_spinner_item,
                null,
                new String[] {
                        DbContract.AccountsTable.Columns.NAME
                },
                new int[] {
                        android.R.id.text1,
                },
                0);
        mSimpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner = toolbar.findViewById(R.id.spinner_accounts);
        mSpinner.setAdapter(mSimpleCursorAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAccountId = l;
                getSupportLoaderManager().restartLoader(LOADER_ACCOUNTS_ID, null, MainActivity.this);
                getSupportLoaderManager().restartLoader(LOADER_TRANSACTIONS_ID, null, MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mTransactionItemAdapter = new TransactionItemAdapter(this, null,0);

        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(mTransactionItemAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = ContentUris.withAppendedId(DbContract.TransactionsTable.CONTENT_URI, l);
                Intent intent = TransactionActivity.getIntent(MainActivity.this, uri, mAccountId, mAccountCurrentValue, mAccountCurrency);
                startActivity(intent);
            }
        });





        mFloatingActionButton = findViewById(R.id.btn_add_account);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TransactionActivity.getIntent(MainActivity.this, null, mAccountId, mAccountCurrentValue, mAccountCurrency);
                startActivity(intent);
//                Intent intent = TransactionActivity.getIntent(MainActivity.this, Uri.parse(""));
//                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ACCOUNTS, null ,this);
        getSupportLoaderManager().initLoader(LOADER_TRANSACTIONS_ID, null ,this);
        getSupportLoaderManager().initLoader(LOADER_ACCOUNTS_ID, null ,this);

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
        Log.d(TAG, "onCreateLoader: " + id);

        switch (id) {
            case LOADER_ACCOUNTS:
                return new CursorLoader(this, DbContract.AccountsTable.CONTENT_URI, null, null, null,null);
            case LOADER_TRANSACTIONS_ID:
                String selection = DbContract.TransactionsTable.Columns.ACCOUNT_ID + " = " + mAccountId;
                return new CursorLoader(this, DbContract.TransactionsTable.CONTENT_URI, null, selection, null,null);
//                return new CursorLoader(this, ContentUris.withAppendedId(DbContract.TransactionsTable.CONTENT_ACCOUNTS_URI, mAccountId), null, null, null, null);
            case LOADER_ACCOUNTS_ID:
                Uri newUri = ContentUris.withAppendedId(DbContract.AccountsTable.CONTENT_URI, mAccountId);
                return new CursorLoader(this, newUri, null, null, null,null);
            default:
                throw new IllegalArgumentException("Cannot create Loader unknown id");
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: " + loader.getId() + ", count - " + data.getCount());

        switch (loader.getId()) {
            case LOADER_ACCOUNTS:
                mSimpleCursorAdapter.swapCursor(data);
                break;
            case LOADER_TRANSACTIONS_ID:
                mTransactionItemAdapter.swapCursor(data);
                break;
            case LOADER_ACCOUNTS_ID:
                if (data.getCount() > 0) {
                    data.moveToFirst();

                    int startValue = data.getInt(data.getColumnIndex("startValue"));
                    BigDecimal startValueBd = new BigDecimal(String.valueOf(startValue / 100.0));
                    BigDecimal rubCurrentValueBd;

                    mAccountCurrency = data.getInt(data.getColumnIndex(DbContract.AccountsTable.Columns.CURRENCY));
                    String currencyStr;
                    switch (mAccountCurrency) {
                        case Currencies.CURRENCY_RUB:
                            currencyStr = " рублей ";
                            rubCurrentValueBd = startValueBd;
                            break;
                        case Currencies.CURRENCY_DOLLAR:
                            currencyStr = " долларов ";
                            rubCurrentValueBd = startValueBd.multiply(new BigDecimal("2.22"));
                            break;
                        case Currencies.CURRENCY_EURO:
                            currencyStr = " евро ";
                            rubCurrentValueBd = startValueBd.multiply(new BigDecimal("3.33"));
                            break;
                        default:
                            currencyStr = " рублей ";
                            rubCurrentValueBd = startValueBd;
                            break;
                    }

                    int plusValue = data.getInt(data.getColumnIndex("plusValue"));
                    BigDecimal plusValueBd = new BigDecimal(String.valueOf(plusValue / 10000.0));
                    int minusValue = data.getInt(data.getColumnIndex("minusValue"));
                    BigDecimal minusValueBd = new BigDecimal(String.valueOf(minusValue / 10000.0));

                    rubCurrentValueBd = rubCurrentValueBd.add(plusValueBd);
                    rubCurrentValueBd = rubCurrentValueBd.add(minusValueBd);

                    BigDecimal currentValue;
                    switch (mAccountCurrency) {
                        case Currencies.CURRENCY_RUB:
                            currencyStr = " рублей ";
                            currentValue = rubCurrentValueBd;
                            break;
                        case Currencies.CURRENCY_DOLLAR:
                            currencyStr = " долларов ";
                            currentValue = rubCurrentValueBd.divide(new BigDecimal("2.22"), RoundingMode.HALF_UP);
                            break;
                        case Currencies.CURRENCY_EURO:
                            currencyStr = " евро ";
                            currentValue = rubCurrentValueBd.divide(new BigDecimal("3.33"), RoundingMode.HALF_UP);
                            break;
                        default:
                            currencyStr = " рублей ";
                            currentValue = rubCurrentValueBd;
                            break;
                    }

                    mTvAccountCurrentValue.setText("Состояние счёта: " + currentValue + " " + currencyStr + "(" + rubCurrentValueBd + " рублей)");
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: " + loader.getId());

        switch (loader.getId()) {
            case LOADER_ACCOUNTS:
                mSimpleCursorAdapter.swapCursor(null);
                break;
            case LOADER_TRANSACTIONS_ID:
                mTransactionItemAdapter.swapCursor(null);
                break;
        }
    }


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

//                if (currentFragment == 0) {
//                    AccountsListFragment accountsListFragment = (AccountsListFragment) getSupportFragmentManager().findFragmentById(R.id.container_fragment);
//                    accountsListFragment.updateView();
//                }
            } else {
                Toast.makeText(getApplicationContext(), "Для работы с другими валютами необходимо подключение к интернету", Toast.LENGTH_LONG).show();
            }
        }

    }
}
