package com.no_texting_while_driving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Help extends Activity {

    Button bBack;
    Button bAbout;
    Button bHowTo;
    Button bSpeedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        bBack = (Button) findViewById(R.id.button_BackToMain);
        bAbout = (Button) findViewById(R.id.button_About);
        bHowTo = (Button) findViewById(R.id.button_HowTo);
        bSpeedometer = (Button) findViewById(R.id.button_SpeedometerHelp);

        bSpeedometer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.no_texting_while_driving.SPEEDOMETER"));
            }
        });

        bBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.no_texting_while_driving.ABOUT"));
            }
        });

        bHowTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.no_texting_while_driving.HOWTO"));
            }
        });

    }

}
