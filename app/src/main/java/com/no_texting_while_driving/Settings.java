package com.no_texting_while_driving;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends Activity {
    public static final String PREFS_NAME = "OnOffPrefs";

    BackgroundService backService;

    Button bSpeedometer;
    Button bTurnOnOff;
    Button bChangePassword;
    Button bSetMPH;
    Button bBack;

    String SpeedMPH;
    Spinner spin;

    Intent serviceIntent;
    Boolean lockUnlock = false;
    ImageView iVLock;
    TextView tVSpeedMPH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        iVLock = (ImageView) findViewById(R.id.imageView_lock);
        bTurnOnOff = (Button) findViewById(R.id.button_Turn_On_Off);
        bSpeedometer = (Button) findViewById(R.id.button_Speedometer);
        bChangePassword = (Button) findViewById(R.id.button_ChangePass);
        bSetMPH = (Button) findViewById(R.id.button_SetMPH);
        bBack = (Button) findViewById(R.id.button_BackToMain);
        tVSpeedMPH = (TextView) findViewById(R.id.textView_SpeedMPH);

        serviceIntent = new Intent();
        serviceIntent.setAction("com.no_texting_while_driving.BackgroundService");

        final SharedPreferences settingPrefs = getSharedPreferences(PREFS_NAME, 0);

        bTurnOnOff.setText(settingPrefs.getString("TURN_ON_OFF", "Turn On"));

        SpeedMPH = settingPrefs.getString("MPH", "10");
        int MPH = java.lang.Integer.parseInt(SpeedMPH);
        tVSpeedMPH.setText(MPH + "MPH");


        lockUnlock = (settingPrefs.getBoolean("ON_OFF", false));
        if (lockUnlock == true) {
            iVLock.setImageResource(R.drawable.lock);
        } else {
            iVLock.setImageResource(R.drawable.unlock);
        }

        bTurnOnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (bTurnOnOff.getText().toString().equals("Turn On")) {

                    SharedPreferences.Editor editor = settingPrefs.edit();

                    editor.putString("TURN_ON_OFF", "Turn Off");
                    editor.putBoolean("ON_OFF", true);
                    editor.commit();

                    bTurnOnOff.setText(settingPrefs.getString("TURN_ON_OFF", "Turn Off"));
                    iVLock.setImageResource(R.drawable.lock);

                    startService(serviceIntent);

                } else {
                    SharedPreferences.Editor editor = settingPrefs.edit();

                    editor.putString("TURN_ON_OFF", "Turn On");
                    editor.putBoolean("ON_OFF", false);
                    editor.commit();

                    bTurnOnOff.setText(settingPrefs.getString("TURN_ON_OFF", "Turn On"));
                    iVLock.setImageResource(R.drawable.unlock);

                    stopService(serviceIntent);

                }//end else
            }
        });

        bSpeedometer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.no_texting_while_driving.SPEEDOMETER"));
            }
        });

        bChangePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.no_texting_while_driving.REGISTER"));
            }
        });

        bSetMPH.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Settings.this);
                dialog.setContentView(R.layout.alert_spinner);

                spin = (Spinner) dialog.findViewById(R.id.mph_dropdown);
                Button cancel = (Button) dialog.findViewById(R.id.button_SpinnerCancel);
                Button save = (Button) dialog.findViewById(R.id.button_SpinnerSave);

                dialog.setTitle("Set MPH to Disable Text");

                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = settingPrefs.edit();

                        editor.putString("MPH", (String) spin.getSelectedItem());
                        editor.commit();

                        SpeedMPH = settingPrefs.getString("MPH", "10");
                        int MPH = java.lang.Integer.parseInt(SpeedMPH);
                        tVSpeedMPH.setText(MPH + "MPH");

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

}
