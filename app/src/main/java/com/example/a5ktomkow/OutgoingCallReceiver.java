package com.example.a5ktomkow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class OutgoingCallReceiver extends BroadcastReceiver
{
    private String blockedNumber;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "calling...", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {
            String phoneNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);

            if ((phoneNumber != null) && phoneNumber.contains(blockedNumber))
            {
                Toast.makeText(context,
                        "aborting call: +" + blockedNumber,
                        Toast.LENGTH_LONG).show();
                this.abortBroadcast();
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
