package com.example.a5ktomkow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class OutgoingCallReceiver extends BroadcastReceiver
{
    private String blockedNumber;

    private static final String TAG = "callBlocker";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "calling...", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {
            String phoneNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "First if");

            if ((phoneNumber != null) && phoneNumber.contains(blockedNumber))
            {
                Log.i(TAG, "Second if");
                Toast.makeText(context,
                        "aborting call: " + phoneNumber,
                        Toast.LENGTH_LONG).show();

                TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

                if (telecomManager != null) {
                    boolean success = telecomManager.endCall();
                }
            }
        }
    }

    public String getBlockedNumber()
    {
        return blockedNumber;
    }

    public void setBlockedNumber(String blockedNumber)
    {
        this.blockedNumber = blockedNumber;
    }
}
