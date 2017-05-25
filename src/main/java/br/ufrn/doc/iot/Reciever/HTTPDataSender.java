package br.ufrn.doc.iot.Reciever;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class HTTPDataSender {

	public void send() throws Exception{
		URL url = new URL("https://android.googleapis.com/gcm/googlenotification");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);

		// HTTP request header
		con.setRequestProperty("project_id", "appparking-e138f");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");
		con.connect();

		// HTTP request
		JSONObject data = new JSONObject();
		data.put("operation", "add");
		data.put("notification_key_name", "tag-123");
		data.put("registration_ids", new JSONArray(Arrays.asList("")));
		//data.put("id_token", idToken);

		OutputStream os = con.getOutputStream();
		os.write(data.toString().getBytes("UTF-8"));
		os.close();

		// Read the response into a string
		InputStream is = con.getInputStream();
		String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
		is.close();

		// Parse the JSON string and return the notification key
		JSONObject response = new JSONObject(responseString);
		response.getString("notification_key");
	}
}
