package com.kaooak.android.homebookkeeping.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.kaooak.android.homebookkeeping.R;

import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class DateFragment extends DialogFragment {

    private static final String ARG_MILLIS = "millis";

    private static final String EXTRA_MILLIS= "com.kaooak.android.homebookkeeping.datefragment.extra_millis";

    DatePicker mDatePicker;

    OnCallbacksListener mOnCallbacksListener;

    public static final DateFragment newInstance(long millis) {
        Bundle args = new Bundle();
        args.putLong(ARG_MILLIS, millis);

        DateFragment dateFragment = new DateFragment();
        dateFragment.setArguments(args);
        return dateFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnCallbacksListener = (OnCallbacksListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCallbacksListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long millis = getArguments().getLong(ARG_MILLIS);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        mDatePicker = (DatePicker) LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date, null);
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);

        return new AlertDialog.Builder(getActivity())
                .setView(mDatePicker)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);

                        long millis = calendar.getTime().getTime();
                        mOnCallbacksListener.onDatePicked(millis);
                    }
                })
                .create();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnCallbacksListener = null;
    }

    public interface OnCallbacksListener {
        void onDatePicked(long millis);
    }

}
