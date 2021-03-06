package br.ufrn.doc.iot.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.ufrn.doc.iot.Reciever.DataReciever;
import br.ufrn.doc.iot.Reciever.DataSender;
import br.ufrn.doc.iot.dominio.Estacionamento;

@WebListener
public class AppServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		
		Estacionamento estacionamento = new Estacionamento(3);
		ctx.setAttribute("estacionamento", estacionamento);
		
		new Thread(new DataReciever(ctx)).start();
		
		DataSender dataSender = new DataSender();
		new Thread(dataSender).start();
		
		ctx.setAttribute("dataSender", dataSender);
		
		Map<String, String> mapaTags = new HashMap<String,String>();
		ctx.setAttribute("dadosUsuarios", mapaTags);
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
