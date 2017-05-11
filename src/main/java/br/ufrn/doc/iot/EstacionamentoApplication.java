package br.ufrn.doc.iot;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class EstacionamentoApplication extends Application{

	@Override
	public Set<Class<?>> getClasses() {
	    final Set<Class<?>> classes = new HashSet<Class<?>>();
	    // register root resource
	    classes.add(HelloWorldService.class);
	    return classes;
	}
}
