package br.ufrn.doc.iot.Reciever;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.json.JsonObject;

import com.microsoft.azure.sdk.iot.service.DeliveryAcknowledgement;
import com.microsoft.azure.sdk.iot.service.FeedbackBatch;
import com.microsoft.azure.sdk.iot.service.FeedbackReceiver;
import com.microsoft.azure.sdk.iot.service.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.Message;
import com.microsoft.azure.sdk.iot.service.ServiceClient;

public class DataSender implements Runnable{
	
	private static final String connectionString = "HostName=ioteamhub.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=bla0Kcuo5pj4SqV01sORWGdkkBEEPF41v2CnOw+Y47g=";
	private static final IotHubServiceClientProtocol protocol = IotHubServiceClientProtocol.AMQPS;
	private ServiceClient serviceClient;
	
	private Queue<Map<String,Object>> fila;
	
	public DataSender() {
		fila = new LinkedBlockingQueue<Map<String,Object>>();
	}
	
	@Override
	public void run() {
		try {
			serviceClient = ServiceClient.createFromConnectionString(connectionString, protocol);
			if (serviceClient != null) {
				serviceClient.open();
				
				while(true){
					if(!fila.isEmpty()){
						Map<String,Object> msg = fila.poll();
						
						FeedbackReceiver feedbackReceiver = serviceClient
								.getFeedbackReceiver((String) msg.get("deviceId"));
						
						feedbackReceiver.open();
						
						String message = ((JsonObject) msg.get("message")).toString();
						
						System.out.println("Enviando para device: "+message);
						
						Message messageToSend = new Message(message);
						messageToSend.setDeliveryAcknowledgement(DeliveryAcknowledgement.Full);
						
						serviceClient.send((String) msg.get("deviceId"), messageToSend);
						
						FeedbackBatch feedbackBatch = feedbackReceiver.receive(10000);
						if (feedbackBatch != null) {
							System.out.println("Message feedback received, feedback time: "
									+ feedbackBatch.getEnqueuedTimeUtc().toString());
						}

						feedbackReceiver.close();
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				serviceClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void addMensagem(Map<String,Object> msg){
		fila.offer(msg);
	}
	
}
