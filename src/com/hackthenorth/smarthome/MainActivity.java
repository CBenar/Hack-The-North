package com.hackthenorth.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 500){
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent go = new Intent(MainActivity.this, ControlActivity.class);
				startActivity(go);
            }
        };
        logoTimer.start();
    }
 }
