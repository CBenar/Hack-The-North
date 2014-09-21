package com.hackthenorth.smarthome;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

@SuppressLint("ShowToast")
public class ControlActivity extends FragmentActivity implements IWitListener {

	final static String SERVER_TOKEN = "2NSU3HWDHUXAHUHPYGRCZDK43NE2EFSX";
	final static String TAG = "ControlActivity";
	
	private Integer darkness = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        // Initialize Fragment
        Wit wit_fragment = (Wit) getSupportFragmentManager().findFragmentByTag("wit_fragment");
        if (wit_fragment != null) {
          wit_fragment.setAccessToken(SERVER_TOKEN);
        }
    }
	
	@Override
	protected void onStart() {
		super.onStart();

		ImageButton button_light_status = (ImageButton) this.findViewById(R.id.button_light_status);
		ImageButton button_light_on = (ImageButton) this.findViewById(R.id.imageButton2);
		ImageButton button_light_off = (ImageButton) this.findViewById(R.id.imageButton3);
		
		ImageButton button_temp_status = (ImageButton) this.findViewById(R.id.ImageButton07);
		final EditText text_set_threshold = (EditText) this.findViewById(R.id.editText1);
		
		button_light_status.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GetLightTask().execute();
			}
		});
		
		button_light_on.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SetLightTask().execute("on");
			}
		});
		
		button_light_off.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SetLightTask().execute("off");
			}
		});
		
		button_temp_status.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GetTempTask().execute();
			}
		});
		
		text_set_threshold.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                		&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) 
                {
                	new SetLightThresholdTask().execute(text_set_threshold.getText().toString());
                    return true;
                } 

            return false;
            }
		});
	}

    @Override
    public void witDidGraspIntent(String intent, HashMap<String, JsonElement> entities, String body, double confidence, Error error) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(entities);
        Log.e("", jsonOutput);
        
        if (intent.equals("light_on")) {
        	new SetLightTask().execute("on");
        } else if (intent.equals("light_off")) {
			new SetLightTask().execute("off");
        } else if (intent.equals("get_light")) {
			new GetLightTask().execute();
        } else if (intent.equals("auto_mode")) {
        	if (ControlActivity.this.darkness == null) {
        		new GetTempTask().execute();
        	}
        	new SetLightThresholdTask().execute(ControlActivity.this.darkness.toString());
        }
    }
    
    class GetLightTask extends AsyncTask<Void, Void, Integer> {

    	final static String full_url = SparkAPI.host + SparkAPI.prefix + "/getLED";
    	final static String query = "access_token=" + SparkAPI.access_token + "&args=";
    	
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				JSONObject json = new JSONObject(SparkAPI.doPost(full_url, query));
				return json.getInt("return_value");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (result.equals(1)) {
				Toast.makeText(ControlActivity.this, "The light is ON!", Toast.LENGTH_SHORT).show();
			} else if (result.equals(0)) {
				Toast.makeText(ControlActivity.this, "The light is OFF!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ControlActivity.this, "Something wrong...", Toast.LENGTH_SHORT).show();
			}
		}
    	
    };
    
    class SetLightTask extends AsyncTask<String, Void, Void> {

    	final static String full_url = SparkAPI.host + SparkAPI.prefix + "/setLED";
    	
		@Override
		protected Void doInBackground(String... params) {
			try {
				String query = "access_token=" + SparkAPI.access_token + "&args=" + params[0];
				SparkAPI.doPost(full_url, query);
				return null;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
    };
    
    class SetLightThresholdTask extends AsyncTask<String, Void, Void> {

    	final static String full_url = SparkAPI.host + SparkAPI.prefix + "/setTh";
    	
		@Override
		protected Void doInBackground(String... params) {
			try {
				String query = "access_token=" + SparkAPI.access_token + "&args=" + params[0];
				SparkAPI.doPost(full_url, query);
				return null;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
    };
    
    class GetTempTask extends AsyncTask<Void, Void, Integer> {

    	final static String full_url = SparkAPI.host + SparkAPI.prefix + "/getSensor";
    	
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				String query = "access_token=" + SparkAPI.access_token + "&args=";
				JSONObject json = new JSONObject(SparkAPI.doPost(full_url, query));
				return json.getInt("return_value");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			if (result > 0) {
				ControlActivity.this.darkness = result;
				Toast.makeText(ControlActivity.this, "The darkness is " + result.toString(), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ControlActivity.this, "Something wrong...", Toast.LENGTH_SHORT).show();
			}
		}
    };
}