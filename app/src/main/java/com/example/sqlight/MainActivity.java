package com.example.sqlight;


import android.app.PendingIntent;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnRead, btnSend;
    EditText etMessage, etTelephone;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        etMessage = (EditText) findViewById(R.id.etMessage);
        etTelephone = (EditText) findViewById(R.id.etTelephone);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String message = etMessage.getText().toString();
        String telephone = etTelephone.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DBHelper.KEY_MESSAGE, message);
                contentValues.put(DBHelper.KEY_TELEPHONE, telephone);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                break;

            case R.id.btnRead:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int messageIndex = cursor.getColumnIndex(DBHelper.KEY_MESSAGE);
                    int telephoneIndex = cursor.getColumnIndex(DBHelper.KEY_TELEPHONE);
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", message = " + cursor.getString(messageIndex) +
                                ", telephone = " + cursor.getString(telephoneIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
                break;

            case R.id.btnSend:
                EditText editText = (EditText) findViewById(R.id.etMessage);
                String destinationAddress = editText.getText().toString();
                EditText smsEditText = (EditText) findViewById(R.id.etMessage);
                String smsMessage = smsEditText.getText().toString();
                String scAddress = null;
                PendingIntent sentIntent = null, deliveryIntent = null;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage
                        (destinationAddress, scAddress, smsMessage,
                                sentIntent, deliveryIntent);;
                break;
        }
        dbHelper.close();
    }
}