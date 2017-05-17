package br.ufrn.doc.iot.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Estacionamento {
	
	private int numeroDeVagas;
	private ArrayList<Vaga> vagasEstacionamento;
	
	public Estacionamento(int numeroDeVagas) {
		this.numeroDeVagas = numeroDeVagas;
		
		vagasEstacionamento = new ArrayList<Vaga>();
		for (int i = 1; i <= numeroDeVagas; i++) {
			vagasEstacionamento.add(new Vaga(i));
		}
		
		Collections.sort(getVagasEstacionamento(), new Comparator<Vaga>() {
			@Override
			public int compare(Vaga v1, Vaga v2) {
				return new Integer(v1.getNumero()).compareTo(new Integer(v2.getNumero()));
			}
		});
	}

	public int getNumeroDeVagas() {
		return numeroDeVagas;
	}
	
	public Vaga alocarVaga(String idMotorista) {
		for (Vaga v : vagasEstacionamento) {
			if (v.isDisponivel()) {
				v.setStatus(Vaga.RESERVADA);
				v.setOcupadoPor(idMotorista);
				return v;
			}
		}
		return null;
	}
	
	public void ocuparVaga(int numeroVaga) {
		for (Vaga v : vagasEstacionamento) {
			if (v.getNumero() == numeroVaga){
				v.setStatus(Vaga.OCUPADA);
			}
		}
	}
	
	public void desalocarVaga(int numeroVaga) {
		for (Vaga v : vagasEstacionamento) {
			if (v.getNumero() == numeroVaga){
				v.setStatus(Vaga.LIVRE);
				v.setOcupadoPor(null);
			}
		}
	}

	public ArrayList<Vaga> getVagasEstacionamento() {
		return vagasEstacionamento;
	}
	
	
	
}
