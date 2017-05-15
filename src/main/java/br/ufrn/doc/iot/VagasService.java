package br.ufrn.doc.iot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

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
		
		Collections.sort(estacionamento.getVagasEstacionamento(), new Comparator<Vaga>() {
			@Override
			public int compare(Vaga v1, Vaga v2) {
				return new Integer(v1.getNumero()).compareTo(new Integer(v2.getNumero()));
			}
		});
		
		return estacionamento.getVagasEstacionamento();

	}
}
