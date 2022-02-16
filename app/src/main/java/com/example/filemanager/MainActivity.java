package com.example.filemanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.filemanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    BluetoothAdapter bluetoothAdapter;
    WifiManager wifiManager;

    private static final int REQUEST_CODE = 1;
    private String[] PERMISSIONS;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        PERMISSIONS = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH_SCAN,

                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(PERMISSIONS, REQUEST_CODE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                Toast.makeText(MainActivity.this, "Read external storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                // set ok button on alert dialog
                builder.setPositiveButton(R.string.dialog_ok, (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE));

                // set cancel on alert dialog
                builder.setNegativeButton(R.string.dialog_cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            if (grantResults.length > 0 &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            }

            if (grantResults.length > 0 &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Bluetooth permission granted", Toast.LENGTH_SHORT).show();
            }
            if (grantResults.length > 0 &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "ACCESS_COARSE_LOCATION permission granted", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                }
                if (!wifiManager.isWifiEnabled()){
                    Toast.makeText(getApplicationContext(), "Turning on wifi...", Toast.LENGTH_SHORT).show();
                    wifiManager.setWifiEnabled(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Wifi is already turned on", Toast.LENGTH_SHORT).show();
                }

            }
            if (grantResults.length > 0 &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE permission granted", Toast.LENGTH_SHORT).show();
            }
            if (grantResults.length > 0 &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "CHANGE_WIFI_STATE permission granted", Toast.LENGTH_SHORT).show();
            }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }
}