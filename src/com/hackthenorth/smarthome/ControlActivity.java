package com.hackthenorth.smarthome;

import java.util.HashMap;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class ControlActivity extends FragmentActivity implements IWitListener {

	final static String SERVER_TOKEN = "2NSU3HWDHUXAHUHPYGRCZDK43NE2EFSX";
	final static String TAG = "ControlActivity";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Initialize Fragment
        Wit wit_fragment = (Wit) getSupportFragmentManager().findFragmentByTag("wit_fragment");
        if (wit_fragment != null) {
          wit_fragment.setAccessToken(SERVER_TOKEN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void witDidGraspIntent(String intent, HashMap<String, JsonElement> entities, String body, double confidence, Error error) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(entities);
        Log.i("Intent", jsonOutput);
;        /*
        ((TextView) findViewById(R.id.jsonView)).setText(Html.fromHtml("<span><b>Intent: " + intent + "<b></span><br/>") +
                jsonOutput + Html.fromHtml("<br/><span><b>Confidence: " + confidence + "<b></span>"));
    */
    }
}