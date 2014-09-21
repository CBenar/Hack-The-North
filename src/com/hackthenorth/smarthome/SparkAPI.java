package com.hackthenorth.smarthome;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SparkAPI {
	final static String host = "https://api.spark.io";
	final static String device_id = "55ff6f065075555310351487";
	final static String prefix = "/v1/devices/" + device_id;
	final static String access_token = "4ac441581be804b12310228bc9250b1f9fdd114c";
		
	public static String doPost(String full_url, String query) 
			throws IOException {
		URL url  = new URL(full_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(query);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String aux = "";
		
		while ((aux = in.readLine()) != null) {
			sb.append(aux);
		}
		return sb.toString();
	}
}