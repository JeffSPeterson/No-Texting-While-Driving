package com.no_texting_while_driving;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    public static final String PREFS_NAME = "RegistrationPrefs";

    Button bSettings;
    Button bRegister;
    Button bHelp;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bSettings = (Button) findViewById(R.id.button_Settings);
        bRegister = (Button) findViewById(R.id.button_Register);
        bHelp = (Button) findViewById(R.id.button_Help);
        final SharedPreferences settingPrefs = getSharedPreferences(PREFS_NAME, 0);

        if (settingPrefs.getBoolean("REGISTERED", false) == false) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Would you like to Register this device?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent("com.no_texting_while_driving.REGISTER"));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }//end if
        else {
            //
            //			Dialog dialog = new Dialog(this);
            //			dialog.setContentView(R.layout.yourLayoutId);
            //			dialog.show();
        }//end else


        bSettings.setOnClickListener(new View.OnClickListener() {
            EditText passwordET;
            Button cancel;
            Button ok;

            @Override
            public void onClick(View v) {

                if (settingPrefs.getBoolean("REGISTERED", false) == true) {

                    final String passwordRegistered = settingPrefs.getString("PASSWORD", "");
                    final Dialog dialog = new Dialog(MainActivity.this);

                    dialog.setContentView(R.layout.password_dialog);

                    passwordET = (EditText) dialog.findViewById(R.id.password_editText);
                    cancel = (Button) dialog.findViewById(R.id.button_PasswordCancel);
                    ok = (Button) dialog.findViewById(R.id.button_PasswordOK);

                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (passwordET.getText().toString().equals(passwordRegistered)) {

                                startActivity(new Intent("com.no_texting_while_driving.SETTINGS"));
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();

                }//end if
                else {
                    startActivity(new Intent("com.no_texting_while_driving.SETTINGS"));
                }//end else
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            EditText passwordET;
            Button cancel;
            Button ok;

            @Override
            public void onClick(View v) {

                if (settingPrefs.getBoolean("REGISTERED", false) == true) {

                    final String passwordRegistered = settingPrefs.getString("PASSWORD", "");

                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.password_dialog);

                    passwordET = (EditText) dialog.findViewById(R.id.password_editText);
                    cancel = (Button) dialog.findViewById(R.id.button_PasswordCancel);
                    ok = (Button) dialog.findViewById(R.id.button_PasswordOK);

                    cancel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (passwordET.getText().toString().equals(passwordRegistered)) {

                                startActivity(new Intent("com.no_texting_while_driving.REGISTER"));
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });

                    dialog.show();

                }//end if
                else {
                    startActivity(new Intent("com.no_texting_while_driving.REGISTER"));
                }//end else
            }
        });

        bHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.no_texting_while_driving.HELP"));
            }
        });
    }

}