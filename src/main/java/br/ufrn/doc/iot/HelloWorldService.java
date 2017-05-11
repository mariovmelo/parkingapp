package br.ufrn.doc.iot;

import java.net.URISyntaxException;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;

//@Path("/hello")
public class HelloWorldService {
	
	private static final String connectionString = "HostName=EstacionamentoIoT.azure-devices.net;DeviceId=myFirstJavaDevice;SharedAccessKey=Ph/nSASaqUOBjbWu5i0rjQ==";
	private static final String deviceId = "myFirstJavaDevice";
	
	Random rand = new Random();
	DeviceClient client;
	
	private static HelloWorldService instance;
	
	private HelloWorldService() {
		try {
			client = new DeviceClient(connectionString, IotHubClientProtocol.MQTT);
			client.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HelloWorldService getInstance(){
		if(instance == null){
			instance = new HelloWorldService();
		}
		
		return instance;
	}
	
	@GET
	@Path("/nome/{name}")
	public Response getMsg(@PathParam("name") String name) {

		String output = "Welcome   : " + name;

		return Response.status(200).entity(output).build();

	}

	@GET
	@Path("/detectouCarro")
	public void detectar() throws Exception {

		double currentWindSpeed = 10+rand.nextDouble() * 4 - 2;
		DadosRFID dadosRFID = new DadosRFID();
		dadosRFID.tagId = "tagRFID"+currentWindSpeed;
		dadosRFID.detectouCarro = 1;
		
		String msgStr = dadosRFID.serialize();
		Message msg = new Message(msgStr);
		
		System.out.println("Sending: " + msgStr);

		Object lockobj = new Object();
		client.sendEventAsync(msg, null, lockobj);

		

	}
	
	private static class DadosRFID {
		public String tagId;
		public int detectouCarro;

		public String serialize() {
			Gson gson = new Gson();
			return gson.toJson(this);
		}
	}
}
