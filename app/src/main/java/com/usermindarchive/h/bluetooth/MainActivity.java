package com.usermindarchive.h.bluetooth;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bluetooth)
    TextView bluetooth;
    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT=406;
    BroadcastReceiver receiver;
    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        ButterKnife.bind(this);
        broadcast();

        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        bluetoothStatus();
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

                }else{
//            to off the bluetooth
                }
            }
        });

        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }


    }
    public void bluetoothStatus(){
        if(mBluetoothAdapter.isEnabled())
            bluetooth.setText("Bluetooth ON");
        else
            bluetooth.setText("Bluetooth OFF");

    }

//    @OnClick(R.id.bluetooth)
//    public void setBluetooth(){
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }else{
////            to off the bluetooth
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_ENABLE_BT){
            if(mBluetoothAdapter.isEnabled())
            Toast.makeText(this,"Bluetooth is on",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"Bluetooth is off",Toast.LENGTH_LONG).show();
            bluetoothStatus();
        }


    }

    public void broadcast(){
        receiver=new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                String state= intent.getAction();
                if(state.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                    bluetoothStatus();
                }
                if(state.equals("android.intent.action.ACTION_POWER_CONNECTED")){
                    bluetooth.setText(bluetooth.getText()+"\n"+"Action."+intent.getAction());

                }
                if(state.equals("android.intent.action.ACTION_POWER_DISCONNECTED")){
                    bluetooth.setText(bluetooth.getText()+"\n"+"Action."+intent.getAction());


                }

            }
        };
        IntentFilter filter=new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(receiver,filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public void updateText(String data){
        bluetooth.setText(bluetooth.getText()+"\n"+"Action."+data);

    }


}
