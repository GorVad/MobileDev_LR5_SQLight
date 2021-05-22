package com.example.sqlight;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{
    private static final String TAG = MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    private MainActivity MainActivity;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        String telephone = "";
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null)
        {
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++)
            {
                if (isVersionM) msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                else msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                telephone = msgs[i].getOriginatingAddress();
                strMessage += "SMS from " + telephone;
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }

        DBHelper dbHelper;
        dbHelper = new DBHelper(MainActivity);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_MESSAGE, strMessage);
        contentValues.put(DBHelper.KEY_TELEPHONE, telephone);

        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
    }
}