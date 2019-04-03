package com.example.a5ktomkow;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int READ_PHONE_STATE_RESP = 1;
    private static final int SEND_SMS_RESP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void checkTelephoneInfo(View view) {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                showNoPermissionToShowTelephoneInfo();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_RESP);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            showTelephoneInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case READ_PHONE_STATE_RESP:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showTelephoneInfo();
                    return;
                }
                else
                {
                    showNoPermissionToShowTelephoneInfo();
                    return;
                }
            }
            case SEND_SMS_RESP:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    turnResponderOn();
                }
            }
        }
    }

    private void turnResponderOn()
    {
        SmsManager manager = SmsManager.getDefault();

        manager.
    }

    private void showTelephoneInfo() {
        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
            return;
        }

        String number = phoneMgr.getLine1Number();
        int phoneType = phoneMgr.getPhoneType();
        int callState = phoneMgr.getCallState();
        int networkType = phoneMgr.getNetworkType();
        String subscriberId = phoneMgr.getSubscriberId();
        String operator = phoneMgr.getNetworkOperator();

        StringBuilder builder = new StringBuilder();
        builder.append("number: " + number + "\n");
        builder.append("phoneType: " + phoneType + "\n");
        builder.append("callState: " + callState + "\n");
        builder.append("networkType: " + networkType + "\n");
        builder.append("subscriberId: " + subscriberId + "\n");
        builder.append("operator: " + operator + "\n");


        TextView numberView = findViewById(R.id.infoLabel);
        numberView.setText(builder.toString());
    }

    private void showNoPermissionToShowTelephoneInfo()
    {
        TextView numberView = findViewById(R.id.infoLabel);
        numberView.setText("I don't have permission to show information about your telephone");
    }

    public void activateResponder(View view)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        SEND_SMS_RESP);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            turnResponderOn();
        }
    }
}
