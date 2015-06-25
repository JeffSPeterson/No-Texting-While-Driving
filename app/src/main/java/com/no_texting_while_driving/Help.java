package com.no_texting_while_driving;

import android.app.Activity;
import android.os.Bundle;
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

    }

}
