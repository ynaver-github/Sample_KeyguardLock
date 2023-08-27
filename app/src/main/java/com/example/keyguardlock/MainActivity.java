package com.example.keyguardlock;

import static com.example.keyguardlock.AppLog.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int ADMIN_REQUEST_CODE = 100;
    public static final int REQUEST_CODE_FOR_CREDENTIAL = 100;
    public static final int REQUEST_CODE_FOR_CREDENTIAL_MWO = 101;

    private KeyguardManager.KeyguardLock keyguardLock;
    private KeyguardManager keyguardManager;

    private Long finishtimeed = Long.valueOf(1000);
    private Long presstime = Long.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1;
        btn1 = findViewById(R.id.btn_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.d(TAG, "btn1 onClick()-Start ");
                keygaurdLock();
                // Enable the keyguard lock
                enableKeyguardLock();
                // Trigger the lock screen
                AppLog.d(TAG, "requestDismissKeyguard() show");
//                keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                setShowWhenLocked(false);
                setTurnScreenOn(false);
//                keyguardManager.requestDismissKeyguard(MainActivity.this, null);
                showAuthenticationScreen();
            }
        });

        // Get the KeyguardManager instance
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

        // Get the KeyguardLock instance
        keyguardLock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE);

        Boolean isSecure = keyguardManager.isKeyguardSecure();
        Boolean isLock = keyguardManager.isKeyguardLocked();

        // Check if the keyguard is secure or already locked
        AppLog.d(TAG, "isSecure " + isSecure + " isLock " + isLock);
        if  (isSecure || isLock) {
            AppLog.d(TAG, "onCreate() already locked");
            // Keyguard is secure or already locked
            // No action required
        } else {
            AppLog.d(TAG, "onCreate() request to lock it");
            // Keyguard is not secure, request to lock it
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setShowWhenLocked(true);
                setTurnScreenOn(true);
            } else {
                // For older versions, use the deprecated flags
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            }
            // Trigger the lock screen
            AppLog.d(TAG, "requestDismissKeyguard() show");
            keyguardManager.requestDismissKeyguard(this, null);
        }
    }

    private void keygaurdLock() {
        AppLog.d(TAG, "keygaurdLock() request to lock it");
        // Get the KeyguardManager instance
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        // Keyguard is not secure, request to lock it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            // For older versions, use the deprecated flags
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        // Trigger the lock screen
        keyguardManager.requestDismissKeyguard(MainActivity.this, null);
    }


    private void enableKeyguardLock() {
        AppLog.d(TAG, "enableKeyguardLock() request to lock it");
        // Check if the keyguard lock is already enabled
//        if (!isKeyguardEnabled()) {
            // Enable the keyguard lock
            keyguardLock.reenableKeyguard();
//            keyguardLock.disableKeyguard();

            // Turn the screen on
            turnScreenOn();
//        }
    }

//    private boolean isKeyguardEnabled() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            return keyguardLock.isDeviceSecure();
//        } else {
//            return keyguardLock.isKeyguardLocked();
//        }
//    }

    private void turnScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void showAuthenticationScreen() {
        // Create the Confirm Credentials screen. You can customize the title and description. Or
        // we will provide a generic one for you if you leave it null
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null);
        if (intent != null) {
            AppLog.d(TAG, "showAuthenticationScreen() request to lock it");
            AppLog.d(TAG, "intent: " + intent);
            startActivityForResult(intent, REQUEST_CODE_FOR_CREDENTIAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.d(TAG, "onActivityResult()-Start " + requestCode + " " + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        AppLog.d(TAG, "onActivityResult()-data " + data.getExtras());
        String dismissStatus = "";
        if (data.getExtras() != null) {
            dismissStatus = data.getExtras().getString("dismissStatus");
            AppLog.d(TAG, "dismissStatus: " + dismissStatus);
        }
        if (requestCode == REQUEST_CODE_FOR_CREDENTIAL) {
            if (dismissStatus.equals("Success")){
                AppLog.d(TAG, "Start checkOvenRegisteredAndGoToOven()");
            }
        } else if (requestCode == REQUEST_CODE_FOR_CREDENTIAL_MWO) {
            if (dismissStatus.equals("Success")) {

            }
        }

    }

    @Override
    protected void onStart() {
        AppLog.d(TAG, "onStart() ");
        super.onStart();

    }

    @Override
    protected void onResume() {
        AppLog.d(TAG, "onResume() ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        AppLog.d(TAG, "onPause() ");
        super.onPause();

    }

    @Override
    protected void onStop() {
        AppLog.d(TAG, "onStop() ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppLog.d(TAG, "onDestroy() ");
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        AppLog.d(TAG, "onBackPressed() ");
        Long tempTime = System.currentTimeMillis();
        Long intervalTime = tempTime - presstime;
        if (0 <= intervalTime && finishtimeed >= intervalTime) {
            finish();
        } else {
            presstime = tempTime;
            Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }
}
