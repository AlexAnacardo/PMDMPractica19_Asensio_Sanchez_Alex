package es.ies.claudiomoyano.dam2.pmdm.practica19_asensio_sanchez_alex;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;


public class ServicioBateria extends Service {

    //Cambiar este valor por el numero de telefono al que queramos que se envien los sms, no dejo el mio por obvias razones
    private static final String PHONE_NUMBER = "123456789";

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            int batteryPercent = (int) ((level / (float) scale) * 100);

            if (batteryPercent <= 15) {
                Toast.makeText(context,
                        "Batería baja: " + batteryPercent + "%",
                        Toast.LENGTH_LONG).show();

                sendSMS("Batería baja (" + batteryPercent + "%)");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendSMS(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
