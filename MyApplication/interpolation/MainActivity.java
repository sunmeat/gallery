package com.sunmeat.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button b1, b2, b3, b4, b5, b6, b7, b8;

    Button but;

    Animation accelerate, anticipate, anticipateOvershoot, bounce, cycle, decelerate, linear, overshoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        but = (Button) findViewById(R.id.button);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        b7 = (Button) findViewById(R.id.button7);
        b8 = (Button) findViewById(R.id.button8);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);

        accelerate = AnimationUtils.loadAnimation(this, R.anim.accelerate);
        anticipate = AnimationUtils.loadAnimation(this, R.anim.anticipate);
        anticipateOvershoot = AnimationUtils.loadAnimation(this, R.anim.anticipate_overshoot);
        bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        cycle = AnimationUtils.loadAnimation(this, R.anim.cycle);
        decelerate = AnimationUtils.loadAnimation(this, R.anim.decelerate);
        linear = AnimationUtils.loadAnimation(this, R.anim.linear);
        overshoot = AnimationUtils.loadAnimation(this, R.anim.overshoot);
    }

    @Override
    public void onClick(View view) {
        Button b = (Button) view;
        int id = b.getId();
        if (id == R.id.button1)
            but.startAnimation(accelerate);
        else if (id == R.id.button2)
            but.startAnimation(anticipate);
        else if (id == R.id.button3)
            but.startAnimation(anticipateOvershoot);
        else if (id == R.id.button4)
            but.startAnimation(bounce);
        else if (id == R.id.button5)
            but.startAnimation(cycle);
        else if (id == R.id.button6)
            but.startAnimation(decelerate);
        else if (id == R.id.button7)
            but.startAnimation(linear);
        else if (id == R.id.button8)
            but.startAnimation(overshoot);
    }
}
