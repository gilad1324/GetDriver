package com.jct.gilad.getdriver.controller;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.ArrayList;

public class HistoryListViewAdapter extends BaseAdapter {
    Context context;

    public HistoryListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return HistoryFragment.FinishRides.size();
    }

    @Override
    public Object getItem(int position) {
        return HistoryFragment.FinishRides.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final Ride ride = HistoryFragment.FinishRides.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_item, null, true);
            viewHolder.endDrive = (TextView) convertView.findViewById(R.id.endDriveInput);
            viewHolder.payment = (TextView) convertView.findViewById(R.id.paymentInput);
            viewHolder.AddContacts = (FloatingActionButton) convertView.findViewById(R.id.AddContacts);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.endDrive.setText(ride.getEndTime().toString());
        double payment = ((ride.getEndTime().getTime() - ride.getStartTime().getTime()) / 60000.0 * 2);
        int temp = (int) (payment*100.0);
        payment = temp;
        payment /= 100.0;
        viewHolder.payment.setText(String.valueOf(payment) + context.getString(R.string.ils));

        viewHolder.AddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                addContactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, ride.getClientEmail());
                addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, ride.getClientPhoneNumber());
                addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, ride.getClientName());
                context.startActivity(addContactIntent);
            }
        });
        return convertView;
    }

    private class ViewHolder {

        protected FloatingActionButton AddContacts;
        private TextView endDrive, payment;

    }
}
