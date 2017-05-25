package br.ufrn.doc.iot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.ufrn.doc.iot.Reciever.handler.RFIDDataHandler;
import br.ufrn.doc.iot.dominio.Estacionamento;
import br.ufrn.doc.iot.dominio.Vaga;

@Path("/vagas")
public class VagasService {
	
	@Context
	private ServletContext context;
	
	@GET
	@Path("/listar")
	@Produces("application/json")
	public List<Vaga> listar() {
		
		Estacionamento estacionamento = (Estacionamento) context.getAttribute("estacionamento");
		
		return estacionamento.getVagasEstacionamento();

	}
	
	@POST
	@Path("/associarDevice")
	@Consumes("application/json")
	public Response associarClienteTagRFID(@FormParam("token")String tokenFCM,@FormParam("tag") String tagRFID){
		
		if(tokenFCM == null || tagRFID == null){
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		
		Map<String, String> mapaTags = (Map<String, String>) context.getAttribute("dadosUsuarios");
		
		if(mapaTags == null){
			mapaTags = new HashMap<String,String>();
		}
		
		mapaTags.put(tagRFID, tokenFCM);
	
		context.setAttribute("dadosUsuario", mapaTags);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/detectarCarro")
	@Consumes("application/json")
	public Response receberTag(@FormParam("tag") String tagRFID) throws Exception{
		
		if(tagRFID == null){
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		
		JsonObject event = Json.createObjectBuilder().
                add("sequencia","1").
                add("data", new Date().toString()).
                add("texto",new String("texto")).
                add("dados",Json.createObjectBuilder().add("tagNumber", tagRFID).build())
                .build();
		
		new RFIDDataHandler().handleData(context, event);
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/listarAssociacoes")
	@Produces("application/json")
	public Map<String, String> listaClientes(){
		return (Map<String, String>) context.getAttribute("dadosUsuario");
	}
}
