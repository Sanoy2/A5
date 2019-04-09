package com.example.a5ktomkow;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private static final int READ_PHONE_STATE_RESP = 1;
    private static final int SEND_SMS_RESP = 2;
    private static final int RECEIVE_SMS_RESP = 3;
    private static final int READ_SMS_RESP = 4;
    private static final int PROCESS_OUTGOING_CALLS = 5;

    SmsReceiver smsReceiver;
    OutgoingCallReceiver blockerReceiver;

    IntentFilter smsAppearedFilter;
    IntentFilter outgoingCallFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsReceiver = new SmsReceiver();
        blockerReceiver = new OutgoingCallReceiver();

        smsAppearedFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        outgoingCallFilter = new IntentFilter("android.provider.Telephony.ACTION_PHONE_STATE_CHANGED");
    }

    public void checkResponderSwitch(View view)
    {
        checkResponderSwitch();
    }

    public void checkBlockerSwitch(View view)
    {
        checkBlockerSwitch();
    }

    public void turnBlockerOff(View view)
    {
        turnBlockerOff();
    }


    public void checkTelephoneInfo(View view)
    {
        if (checkIfPermissionGranted(Manifest.permission.READ_PHONE_STATE))
        {
            showTelephoneInfo();
        } else
        {
            askForPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_RESP);
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
                } else
                {
                    showNoPermissionToShowTelephoneInfo();
                    return;
                }
            }

            case PROCESS_OUTGOING_CALLS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("perm", "outgoing call process accepted");
                    blockerSwitchOn();
                }
                return;

            case SEND_SMS_RESP:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("perm", "send sms accepted");
                    if (!checkIfResponderCanBeRun())
                    {
                        askForPermissionsForSmsResponder();
                        return;
                    } else
                    {
                        smsSwitchOn();
                        return;
                    }
                }
            }
            case RECEIVE_SMS_RESP:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("perm", "receive sms accepted");
                    if (!checkIfResponderCanBeRun())
                    {
                        askForPermissionsForSmsResponder();
                        return;
                    } else
                    {
                        smsSwitchOn();
                        return;
                    }
                }
            }
            case READ_SMS_RESP:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.i("perm", "read sms accepted");
                    if (!checkIfResponderCanBeRun())
                    {
                        askForPermissionsForSmsResponder();
                        return;
                    } else
                    {
                        smsSwitchOn();
                        return;
                    }
                }
            }
        }
    }

    private void turnResponderOn()
    {
        if (checkIfResponderCanBeRun())
        {
            registerReceiver(smsReceiver, smsAppearedFilter);
        } else
        {
            Switch smsSwitch = (Switch) findViewById(R.id.smsSwitch);
            smsSwitch.setChecked(false);
            askForPermissionsForSmsResponder();
        }
    }

    private void askForPermissionsForSmsResponder()
    {
        if (!checkIfPermissionGranted(Manifest.permission.RECEIVE_SMS))
        {
            askForPermission(Manifest.permission.RECEIVE_SMS, RECEIVE_SMS_RESP);
            return;
        }

        if (!checkIfPermissionGranted(Manifest.permission.READ_SMS))
        {
            askForPermission(Manifest.permission.READ_SMS, READ_SMS_RESP);
            return;
        }

        if (!checkIfPermissionGranted(Manifest.permission.SEND_SMS))
        {
            askForPermission(Manifest.permission.SEND_SMS, SEND_SMS_RESP);
            return;
        }
    }

    private void turnBlockerOn()
    {
        if (checkIfPermissionGranted(Manifest.permission.PROCESS_OUTGOING_CALLS))
        {
            blockerReceiver.setBlockedNumber(getNumberToBlock());
            Toast.makeText(getApplicationContext(), "blocking: " + getNumberToBlock(), Toast.LENGTH_SHORT).show();
            registerReceiver(blockerReceiver, outgoingCallFilter);
        } else
        {
            askForPermission(Manifest.permission.PROCESS_OUTGOING_CALLS, PROCESS_OUTGOING_CALLS);
        }

    }

    private String getNumberToBlock()
    {
        EditText numberToBlockEditText = findViewById(R.id.numberToBlock);
        return numberToBlockEditText.getText().toString();
    }

    private void turnResponderOff()
    {
        try
        {
            unregisterReceiver(smsReceiver);
        } catch (Exception e)
        {

        }
    }

    private void turnBlockerOff()
    {
        try
        {
            unregisterReceiver(blockerReceiver);
            blockerSwitchOff();
        } catch (Exception e)
        {

        }
    }

    private void showTelephoneInfo()
    {
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

    private boolean checkIfResponderCanBeRun()
    {
        return checkIfPermissionGranted(Manifest.permission.SEND_SMS)
                && checkIfPermissionGranted(Manifest.permission.RECEIVE_SMS)
                && checkIfPermissionGranted(Manifest.permission.READ_SMS);
    }


    private void smsSwitchOn()
    {
        Switch smsSwitch = (Switch) findViewById(R.id.smsSwitch);
        smsSwitch.setChecked(true);
        checkResponderSwitch();
    }

    private void blockerSwitchOn()
    {
        Switch blockerSwitch = (Switch) findViewById(R.id.blockCallSwitch);
        blockerSwitch.setChecked(true);
        checkBlockerSwitch();
    }

    private void blockerSwitchOff()
    {
        Switch blockerSwitch = (Switch) findViewById(R.id.blockCallSwitch);
        blockerSwitch.setChecked(false);
    }

    private void checkResponderSwitch()
    {
        Switch smsSwitch = (Switch) findViewById(R.id.smsSwitch);

        Boolean switchState = smsSwitch.isChecked();

        try
        {
            if (switchState)
            {
                turnResponderOn();
            } else
            {
                turnResponderOff();
            }
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBlockerSwitch()
    {
        Switch smsSwitch = (Switch) findViewById(R.id.blockCallSwitch);

        Boolean switchState = smsSwitch.isChecked();

        try
        {
            if (switchState)
            {
                turnBlockerOn();
            } else
            {
                turnBlockerOff();
            }
        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showNoPermissionToShowTelephoneInfo()
    {
        TextView numberView = findViewById(R.id.infoLabel);
        numberView.setText("I don't have permission to show information about your telephone");
    }

    private boolean checkIfPermissionGranted(String permission)
    {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermission(String permission, int responseCode)
    {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, responseCode);
    }
}
