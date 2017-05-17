package br.ufrn.doc.iot.Reciever;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;

import com.google.gson.Gson;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.PartitionReceiver;
import com.microsoft.azure.servicebus.ConnectionStringBuilder;

import br.ufrn.doc.iot.Reciever.handler.DataHandler;
import br.ufrn.doc.iot.Reciever.handler.RFIDDataHandler;

public class DataReciever implements Runnable{
	
	private ServletContext context;
	private static Map<String,DataHandler> mapaHandler;
	
	static{
		mapaHandler = new HashMap<String,DataHandler>();
		mapaHandler.put("ANTENA_RFID_ENTRADA" , new RFIDDataHandler());
	}
	
	public DataReciever(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public void run() {
		try{
			final String namespaceName = "ihsuprodbyres006dednamespace";
			final String eventHubName = "iothub-ehub-ioteamhub-160683-17602d125e";
			final String sasKeyName = "iothubowner";
			final String sasKey = "bla0Kcuo5pj4SqV01sORWGdkkBEEPF41v2CnOw+Y47g=";
			ConnectionStringBuilder connStr = new ConnectionStringBuilder(namespaceName, eventHubName, sasKeyName, sasKey);
			
			System.out.println(connStr.toString());
			EventHubClient ehClient = EventHubClient.createFromConnectionStringSync(String.format(connStr.toString()));
			
			String partitionId = "0";
			
			Calendar atual = Calendar.getInstance();
			atual.set(Calendar.MILLISECOND, -10000);
			
			CompletableFuture<PartitionReceiver> receiver = ehClient.createReceiver("monitor" ,partitionId ,  atual.toInstant());
			
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
						
						JsonObject dados = Json.createReader(new ByteArrayInputStream(receivedEvent.getBytes())).readObject();
						JsonObject event = Json.createObjectBuilder().
		                        add("sequencia",receivedEvent.getSystemProperties().getSequenceNumber()).
		                        add("data", receivedEvent.getSystemProperties().getEnqueuedTime().toString()).
		                        add("texto",new String(receivedEvent.getBytes(), Charset.defaultCharset())).
		                        add("dados",dados)
		                        .build();
						
						DataHandler handler = mapaHandler.get(dados.getString("sensor"));
						
						if(handler != null)
							handler.handleData(context, event);
						
						String saida = gson.toJson(mensagem);
						
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
