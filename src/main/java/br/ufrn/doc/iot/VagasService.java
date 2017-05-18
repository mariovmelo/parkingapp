package br.ufrn.doc.iot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		return Response.ok().build();
	}
}
