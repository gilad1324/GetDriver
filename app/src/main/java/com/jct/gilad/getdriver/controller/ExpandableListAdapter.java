package com.jct.gilad.getdriver.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.jct.gilad.getdriver.R;
import com.jct.gilad.getdriver.model.backend.BackendFactorySingleton;
import com.jct.gilad.getdriver.model.backend.CurrentLocation;
import com.jct.gilad.getdriver.model.database.FireBase_DbManager;
import com.jct.gilad.getdriver.model.entities.Ride;
import com.jct.gilad.getdriver.model.entities.Status1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jct.gilad.getdriver.model.backend.CurrentLocation.getPlace;
import static com.jct.gilad.getdriver.model.backend.CurrentLocation.locationA;

class ExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context context;
    String driverID;
    Filter distanceFilter;
    private List<Ride> rideList;
    private List<Ride> orginRideList;
    CurrentLocation location;
    private LayoutInflater inflater;


    public ExpandableListAdapter(Context context, List<Ride> rideList, String driverID) {
        this.context = context;
        this.rideList = rideList;
        this.driverID = driverID;
        this.orginRideList = rideList;
        location = new CurrentLocation(context);
        location.getLocation(context);
    }


    @Override
    public int getGroupCount() {
        return rideList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ride ride = (Ride) getGroup(groupPosition);
        // Check if an existing view is being reused, otherwise inflate the view
        ExpandableListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ExpandableListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.availble_group_layout, null);
            viewHolder.destination = (TextView) convertView.findViewById(R.id.destination);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ExpandableListAdapter.ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.destination.setText(getPlace(ride.getDestLocation()));
        float distance = (ride.getSourceLocation().distanceTo(locationA));
        distance /= 100;
        int temp = (int) (distance);
        distance = (float) (temp) / 10;
        if (locationA.getLatitude() != 0 || locationA.getLongitude() != 0)
            viewHolder.distance.setText(String.valueOf(distance) + context.getString(R.string.KM));
        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Ride ride = (Ride) getChild(groupPosition, childPosition);
        final ExpandableListAdapter.ViewHolder2 viewHolder2;
        if (convertView == null) {
            viewHolder2 = new ExpandableListAdapter.ViewHolder2();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.ride_item, null);
            viewHolder2.full_name = (TextView) convertView.findViewById(R.id.nameInput);
            viewHolder2.source = (TextView) convertView.findViewById(R.id.sourceInput);
            viewHolder2.callButton = (Button) convertView.findViewById(R.id.buttonCall);
            viewHolder2.emailButton = (Button) convertView.findViewById(R.id.buttonEmail);
            viewHolder2.messageButton = (Button) convertView.findViewById(R.id.buttonMessage);
            viewHolder2.startButton = (Button) convertView.findViewById(R.id.buttonTakeDrive);
            convertView.setTag(viewHolder2);
        } else {
            viewHolder2 = (ViewHolder2) convertView.getTag();
        }
        viewHolder2.source.setText(getPlace(ride.getSourceLocation()));
        viewHolder2.full_name.setText(ride.getClientName());
        viewHolder2.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String uri = "waze://?ll=" + ride.getDestLocation().getLatitude() + ", " + ride.getDestLocation().getLongitude() + "&navigate=yes";
//                context.startActivity(new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse(uri)));
                try {
                    if(BackendFactorySingleton.getBackend().RideCanBeProgress(ride))
                        ride.setStatus(Status1.INPROGRESS);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = Calendar.getInstance().getTime();
                ride.setStartTime(date);
                ride.setDriverID(driverID);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        return BackendFactorySingleton.getBackend().updateRide(ride, new FireBase_DbManager.Action<String>() {
                            @Override
                            public void onSuccess(String obj) {
                                Toast.makeText(context, R.string.update, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Exception exception) {

                            }

                            @Override
                            public void onProgress(String status, double percent) {

                            }
                        });
                    }
                }.execute();
                Toast.makeText(context, R.string.pass_progress, Toast.LENGTH_LONG).show();
//                String smsText = context.getString(R.string.Taxi_on_the_way);
//                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + ride.getPhone()));
//                smsIntent.putExtra("sms_body", smsText);
//                context.startActivity(smsIntent);
            }
        });
        viewHolder2.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "smsto", ride.getClientPhoneNumber(), null));
                context.startActivity(smsIntent);
            }
        });
        viewHolder2.emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", ride.getClientEmail(), null));
                context.startActivity(Intent.createChooser(emailIntent, "choose an email client"));
            }
        });
        viewHolder2.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ride.getClientPhoneNumber()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder2 {
        TextView full_name;
        TextView source;
        Button callButton;
        Button messageButton;
        Button emailButton;
        Button startButton;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        if (distanceFilter == null)
            distanceFilter = new DistanceFilter();
        return distanceFilter;
    }

    public class ViewHolder {
        TextView destination;
        TextView distance;
    }

    public void resetData() {
        rideList = orginRideList;
    }

    private class DistanceFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if ((constraint == null) || (constraint.length() == 0)) {
                // No filter implemented we return all the list
                results.values = orginRideList;
                results.count = orginRideList.size();
            } else if (!Character.isDigit(constraint.charAt(constraint.length() - 1))) {
                results.values = rideList;
                results.count = rideList.size();
            } else {
                // We perform filtering operation
                List<Ride> nRideList = new ArrayList<Ride>();
                for (Ride ride : orginRideList) {
                    float distance = (ride.getSourceLocation().distanceTo(locationA));
                    distance /= 100;
                    int temp = (int) (distance);
                    distance = (float) (temp) / 10;
                    if (distance <= Float.valueOf(constraint.toString()))
                        nRideList.add(ride);
                }
                results.values = nRideList;
                results.count = nRideList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rideList = (List<Ride>) results.values;
            notifyDataSetChanged();
        }
    }
}
