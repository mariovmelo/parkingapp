package br.ufrn.doc.iot.Reciever.handler;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;

import br.ufrn.doc.iot.Reciever.DataSender;
import br.ufrn.doc.iot.dominio.Estacionamento;
import br.ufrn.doc.iot.dominio.Vaga;

public class PresenceDataHandler implements DataHandler{

	@Override
	public void handleData(ServletContext context, JsonObject mensagem) {
		
		Estacionamento estacionamento = (Estacionamento) context.getAttribute("estacionamento");
		Vaga vaga = null;
		
		if(mensagem.getJsonObject("dados").getInt("status") == Vaga.OCUPADA )
			vaga = estacionamento.ocuparVaga(mensagem.getJsonObject("dados").getInt("numeroVaga"));
		else
			vaga = estacionamento.desalocarVaga(mensagem.getJsonObject("dados").getInt("numeroVaga"));
		
		context.setAttribute("estacionamento", estacionamento);
		
		DataSender dataSender = (DataSender) context.getAttribute("dataSender");
		
		Map<String, Object> mapa = new HashMap<String,Object>();
		mapa.put("deviceId", "rfidentrada");
		mapa.put("message", Json.createObjectBuilder().add("status", vaga.getStatus())
				.add("numero", vaga.getNumero()).add("sensor", "ledVaga").build());
		dataSender.addMensagem(mapa);
	}

}
