package com.jct.gilad.getdriver.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.jct.gilad.getdriver.model.database.NotifyDataChange;
import com.jct.gilad.getdriver.model.entities.Ride;

import java.util.Calendar;
import java.util.List;

public class MyService extends Service {
     @Override
        public void onCreate(){
        super.onCreate();
        Toast.makeText(getApplicationContext(), " Service Create", Toast.LENGTH_LONG);

    }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            BackendFactorySingleton.getBackend().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND,-10);
               // Date date = calendar.getTime();
                for (Ride ride : obj) {
                   // if (ride.getWhenLoadToFirebase().after(date)) {
                        Intent intent = new Intent(MyService.this, MyReceiver.class);
                        sendBroadcast(intent);
                   // }
                }
            }
            @Override
            public void onFailure(Exception exception) {
            }
        });
        return START_STICKY;
    }

        @Override
        public void onDestroy() {
        super.onDestroy();
        BackendFactorySingleton.getBackend().stopNotifyToRidesList();
        Toast.makeText(getApplicationContext(), " Service destroy", Toast.LENGTH_LONG);
    }

        @Override
        public IBinder onBind(Intent intent) {
        return null;
    }
    }
