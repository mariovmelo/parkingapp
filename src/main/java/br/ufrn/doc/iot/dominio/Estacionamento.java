package br.ufrn.doc.iot.dominio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufrn.doc.iot.mobile.AppServerParking;

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

	public Vaga getVaga(int numeroVaga) {
		for (Vaga v : vagasEstacionamento) {
			if (v.getNumero() == numeroVaga) {
				return v;
			}
		}
		return null;
	}

	public Vaga alocarVaga(String idMotorista,String token) throws Exception {
		try{
			for (Vaga v : vagasEstacionamento) {
				if (v.isDisponivel()) {
					v.setStatus(Vaga.RESERVADA);
					v.setOcupadoPor(idMotorista);
					v.setTokenMobile(token);
					AppServerParking.sendPushNotification(token, v);
					return v;
				}
			}
			return null;
		}finally{
			atualizarMobile();
		}
	}

	public Vaga ocuparVaga(int numeroVaga) {
		try{
			for (Vaga v : vagasEstacionamento) {
				if (v.getNumero() == numeroVaga){
					v.setStatus(Vaga.OCUPADA);
					return v;
				}
			}
			return null;	
		}finally{
			atualizarMobile();
		}

	}

	public Vaga desalocarVaga(int numeroVaga) {
		try{
			for (Vaga v : vagasEstacionamento) {
				if (v.getNumero() == numeroVaga){
					v.setStatus(Vaga.LIVRE);
					v.setOcupadoPor(null);
					return v;
				}
			}
			return null;
		}finally{
			atualizarMobile();
		}

	}

	private void atualizarMobile(){
		try {
			AppServerParking.sendPushNotificationBroadcast(vagasEstacionamento);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Vaga> getVagasEstacionamento() {
		return vagasEstacionamento;
	}



}
