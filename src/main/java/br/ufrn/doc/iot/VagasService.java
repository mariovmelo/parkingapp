package br.ufrn.doc.iot;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.ufrn.doc.iot.dominio.Vaga;

@Path("/vagas")
public class VagasService {

	@GET
	@Path("/listar")
	@Produces("application/json")
	public List<Vaga> listar() {

		ArrayList<Vaga> vagas = new ArrayList<Vaga>();
				
		for(int i = 0; i < 20; i++){
			Vaga v = new Vaga();
			v.setNumero(i);
			v.setOcupado(false);
			vagas.add(v);
		}
		

		return vagas;

	}
}
