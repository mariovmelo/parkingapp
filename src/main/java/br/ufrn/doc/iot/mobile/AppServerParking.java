package br.ufrn.doc.iot.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.JsonObject;

import br.ufrn.doc.iot.dominio.Vaga;


public class AppServerParking {

	public final static String AUTH_KEY_FCM = "AIzaSyAEfYPTvJOZDsmddOC6t28MGqKKW7AuExY";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	public static String sendPushNotification(String deviceToken,Vaga vaga)
			throws IOException {
		String result = "";
		URL url = new URL(API_URL_FCM);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
		conn.setRequestProperty("Content-Type", "application/json");

		//dispositivo que vai enviar
		JsonObject json = new JsonObject();
		json.addProperty("to", deviceToken.trim());


		JsonObject info = new JsonObject();
		info.addProperty("title", "App Parking Mensagem Teste"); 
		//logica para enviar a vaga aonde o cliente podera estacionar
		info.addProperty("body", "Estacione na vaga "+vaga.getNumero()); 
		json.add("notification", info);

		try {
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			result = "Sucesso";
		} catch (Exception e) {
			e.printStackTrace();
			result = "Falha";
		}
		System.out.println("GCM Notification is sent successfully");

		return result;

	}


	public static String sendPushNotificationBroadcast(List<Vaga> vagas) throws IOException{
		String topic = "/topics/vagas";
		String result = "";
	    URL url = new URL(API_URL_FCM);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setUseCaches(false);
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
	    conn.setRequestProperty("Content-Type", "application/json");

	    
	    JsonObject json = new JsonObject();
	    json.addProperty("to", topic);
	    
	    //Enviar panorama com o mapa do estacionamento  
	    JsonObject info = new JsonObject();
	    for(Vaga vaga: vagas){
	    	info.addProperty("vaga"+vaga.getNumero(), vaga.getStatusDesc());
	    }
	    
	    json.add("data", info);
	    
	    try {
	        OutputStreamWriter wr = new OutputStreamWriter(
	                conn.getOutputStream());
	        wr.write(json.toString());
	        wr.flush();

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	                (conn.getInputStream())));

	        String output;
	        System.out.println("Output from Server .... \n");
	        while ((output = br.readLine()) != null) {
	            System.out.println(output);
	        }
	        result = "Sucesso";
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = "Falha";
	    }
	    System.out.println("GCM Notification is sent successfully");

	    return result;
	}
}
