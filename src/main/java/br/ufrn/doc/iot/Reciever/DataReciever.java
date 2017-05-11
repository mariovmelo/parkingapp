package br.ufrn.doc.iot.Reciever;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.PartitionReceiver;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;

public class DataReciever implements Runnable{

	//private static final String connectionString = "HostName=EstacionamentoIoT.azure-devices.net;DeviceId=myFirstJavaDevice;SharedAccessKey=Ph/nSASaqUOBjbWu5i0rjQ==";
	private static final String connectionString = "HostName=EstacionamentoIoT.azure-devices.net;DeviceId=%s;SharedAccessKey=%s";
	private static final String deviceId = "myFirstJavaDevice";
	
	protected String deviceId;
	protected String deviceKey;
	
	@Override
	public void run() {
		try{
			final String namespaceName = "ihsuprodbyres021dednamespace";
			final String eventHubName = "iothub-ehub-estacionam-147236-3497bd514c";
			final String sasKeyName = "iothubowner";
			final String sasKey = "w+tIMayUXe1x2tsSv5OOtnEf/Wt/ZvLb0tjGPDtiDzU=";
			ConnectionStringBuilder connStr = new ConnectionStringBuilder(namespaceName, eventHubName, sasKeyName, sasKey);
			
			System.out.println(connStr.toString());
			
			EventHubClient ehClient = EventHubClient.createFromConnectionStringSync(String.format(connStr.toString());
			
			
			String partitionId = "0";
			
			Calendar atual = Calendar.getInstance();
			atual.set(Calendar.MILLISECOND, -10000);
			
			CompletableFuture<PartitionReceiver> receiver = ehClient.createReceiver("monitor" ,partitionId ,  atual.toInstant());
//			PartitionReceiver receiver = ehClient.createReceiverSync(
//					"monitor", 
//					partitionId, 
//					PartitionReceiver.START_OF_STREAM,
//					false);

			//receiver.setReceiveTimeout(Duration.ofSeconds(20));
			
			Gson gson = new Gson();
			
			System.out.println("Iniciando Leitura...");
			while(true){
				int batchSize = 0;
				Iterable<EventData> receivedEvents = receiver.get().receiveSync(100);
				if (receivedEvents != null)
				{
					for(EventData receivedEvent: receivedEvents)
					{	
						Messagem mensagem = new Messagem();
						
						mensagem.setSequencia(receivedEvent.getSystemProperties().getSequenceNumber());
						mensagem.setData(receivedEvent.getSystemProperties().getEnqueuedTime());
						mensagem.setTexto(new String(receivedEvent.getBytes(), Charset.defaultCharset()));
						
						JsonObject event = Json.createObjectBuilder().
		                        add("sequencia",receivedEvent.getSystemProperties().getSequenceNumber()).
		                        add("data", receivedEvent.getSystemProperties().getEnqueuedTime().toString()).
		                        add("texto",new String(receivedEvent.getBytes(), Charset.defaultCharset()))
		                        .build();
						
						String saida = gson.toJson(mensagem);
						//sessao.getBasicRemote().sendText(saida);
						//sessao.getBasicRemote().sendObject(event);
						
						System.out.println(String.format("Message: %s", new String(receivedEvent.getBytes(), Charset.defaultCharset())));
						System.out.println(String.format("Offset: %s, SeqNo: %s, EnqueueTime: %s", 
								receivedEvent.getSystemProperties().getOffset(), 
								receivedEvent.getSystemProperties().getSequenceNumber(), 
								receivedEvent.getSystemProperties().getEnqueuedTime()));
						System.out.println(String.format("ReceivedBatch Size: %s", batchSize));
						batchSize++;
					}
				}

				

			}
		}catch(Exception e){
			e.printStackTrace();
		}


	}

	private class Messagem{
		private String texto;
		private long sequencia;
		private Instant data;
		
		public String getTexto() {
			return texto;
		}
		public void setTexto(String texto) {
			this.texto = texto;
		}
		public long getSequencia() {
			return sequencia;
		}
		public void setSequencia(long sequencia) {
			this.sequencia = sequencia;
		}
		public Instant getData() {
			return data;
		}
		public void setData(Instant data) {
			this.data = data;
		}
		
		
	}
//	public static void main(String[] args) {
//		new DataReciever().run();
//	}
}
