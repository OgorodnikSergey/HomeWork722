package ru.ogorodnik.homework722;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public EditText phone, smsText;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCall = (Button) findViewById(R.id.btnCall);
        Button btnSentSMS = (Button) findViewById(R.id.btnSentSMS);
        phone = (EditText) findViewById(R.id.phone);
        smsText = (EditText) findViewById(R.id.smsText);

//Звоним
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callByNumber ();
            }
        });
//отправляем СМС
        btnSentSMS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendSMS();
            }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
// Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Разрешение получено, осуществляем звонок
                    sendSMS();
                } else {
// Разрешение не дано. Закрываем приложение
                    finish();
                }
            }
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
// Проверяем результат запроса на право позвонить
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// Разрешение получено, осуществляем звонок
                    callByNumber();
                } else {
// Разрешение не дано. Закрываем приложение
                    finish();
                }
            }
        }
    }

    private void sendSMS (){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не получено
            // Делаем запрос на добавление разрешения отправки SMS
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Разрешение уже получено
            if (!phone.getText().toString().equals("") && !smsText.getText().toString().equals("") ) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone.getText().toString()));
                smsIntent.putExtra("sms_body", smsText.getText().toString());
                startActivity(smsIntent);
                //если оставить smsManager, то не видно что происходит
                //SmsManager smgr = SmsManager.getDefault();
                //smgr.sendTextMessage(phone.getText().toString(),null,smsText.getText().toString(),null,null);
                Toast.makeText(MainActivity.this, "SMS отправляется", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MainActivity.this, "Не введен номер телефона или текст SMS", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void callByNumber(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не получено
            // Делаем запрос на добавление разрешения звонка
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Разрешение уже получено
            if (!phone.getText().toString().equals("")) {
                Uri uri = Uri.parse("tel:"+phone.getText().toString());
                Intent dialIntent = new Intent(Intent.ACTION_CALL, uri);
                // Звоним
                startActivity(dialIntent);
            } else {
                Toast.makeText(MainActivity.this, "Введите номер телефона", Toast.LENGTH_LONG).show();
            }
        }
    }


}
