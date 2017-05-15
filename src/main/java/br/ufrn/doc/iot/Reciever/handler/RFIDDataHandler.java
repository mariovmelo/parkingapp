package br.ufrn.doc.iot.Reciever.handler;

import javax.json.JsonObject;
import javax.servlet.ServletContext;

import br.ufrn.doc.iot.dominio.Estacionamento;

public class RFIDDataHandler implements DataHandler{

	@Override
	public void handleData(ServletContext context, JsonObject mensagem) {
		
		Estacionamento estacionamento = (Estacionamento) context.getAttribute("estacionamento");
		estacionamento.alocarVaga(mensagem.getJsonObject("dados").getString("tagNumber"));
		context.setAttribute("estacionamento", estacionamento);
	}

}
