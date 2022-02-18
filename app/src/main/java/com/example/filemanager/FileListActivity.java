package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.filemanager.databinding.ActivityFileListBinding;

import java.util.Objects;

public class FileListActivity extends AppCompatActivity {
    private static final String TAG = "myApp";
    ActivityFileListBinding binding;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFileListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.grantPermissinBtn.setVisibility(View.INVISIBLE);
        // To hide the top bar in app
        Objects.requireNonNull(getSupportActionBar()).hide();
        String path = getIntent().getStringExtra("path");
        Log.d(TAG, "onCreate: " + path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isPermissionGranted;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isPermissionGranted = Environment.isExternalStorageManager();
            if (!isPermissionGranted) {
                // permission is not granted
                binding.grantPermissinBtn.setVisibility(View.VISIBLE);
                binding.grantPermissinBtn.setOnClickListener(view -> new AlertDialog.Builder(FileListActivity.this)
                        .setTitle("Allow all file access")
                        .setMessage("To use this app we need storage permission, so we strongly suggest" +
                                " you to grant this permission\n\n" +
                                "Grant permission?")
                        .setPositiveButton("OK", (dialogInterface, i) -> takePermission())
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(this, "Please give the permission", Toast.LENGTH_SHORT).show())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show());
            } else {
                // permission is granted
                binding.grantPermissinBtn.setVisibility(View.INVISIBLE);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                binding.grantPermissinBtn.setVisibility(View.VISIBLE);
                binding.grantPermissinBtn.setOnClickListener(view -> new AlertDialog.Builder(FileListActivity.this)
                        .setTitle("Allow all file access")
                        .setMessage("To use this app we need storage permission, so we strongly suggest" +
                                " you to grant this permission\n\n" +
                                "Grant permission?")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(FileListActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE))
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> Toast.makeText(this, "Please give the permission", Toast.LENGTH_SHORT).show())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show());
            } else
                binding.grantPermissinBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void takePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }
        }
    }
}