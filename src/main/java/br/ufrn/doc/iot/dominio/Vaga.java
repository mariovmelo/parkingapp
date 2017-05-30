package br.ufrn.doc.iot.dominio;

public class Vaga {

	public static final int LIVRE = 0;
	public static final int RESERVADA = 1;
	public static final int OCUPADA = 2;
	
	private String ocupadoPor;
	
	private int status;
	
	private int numero;
	
	private String tokenMobile;
	
	public Vaga(int numero){
		this.numero = numero;
	}
	
	public int getNumero() {
		return numero;
	}

	public String getOcupadoPor() {
		return ocupadoPor;
	}

	public void setOcupadoPor(String ocupadoPor) {
		this.ocupadoPor = ocupadoPor;
	}

	public int getStatus() {
		return status;
	}
	
	public String getStatusDesc(){
		if(status == LIVRE)
			return "Livre";
		else if(status == OCUPADA){
			return "Ocupada";
		}else{
			return "Reservada";
		}
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public boolean isDisponivel(){
		return status == LIVRE;
	}
	
	public boolean isReservado(){
		return status == RESERVADA;
	}
	
	public String getTokenMobile() {
		return tokenMobile;
	}

	public void setTokenMobile(String tokenMobile) {
		this.tokenMobile = tokenMobile;
	}
	
}
