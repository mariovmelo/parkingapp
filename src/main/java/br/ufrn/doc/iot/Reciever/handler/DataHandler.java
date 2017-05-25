package br.ufrn.doc.iot.Reciever.handler;

import javax.json.JsonObject;
import javax.servlet.ServletContext;

public interface DataHandler {
	
	public void handleData(ServletContext context, JsonObject mensagem) throws Exception;

}
