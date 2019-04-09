package com.example.a5ktomkow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "onReceive called");
//        Toast.makeText(context, "New text message appeared", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            String number = "";
            String text = "";

            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                number = smsMessage.getDisplayOriginatingAddress();
                text += smsMessage.getMessageBody();
                Log.i(TAG, "number: " + number);

                String newText = tryIncrementGivenValueAsText(text);
                sendSms(number, newText);
            }
        }
    }

    private String tryIncrementGivenValueAsText(String text)
    {
        try
        {
            int number = Integer.parseInt(text);
            return Integer.toString(++number);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    private void sendSms(String number, String text)
    {
        if (text.isEmpty() || text == null)
            text = "Sorry, I don't understand your message. Send me an integer number";

        SmsManager.getDefault().sendTextMessage(number, null, text, null, null);
    }

}