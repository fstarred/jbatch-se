package it.fstarred.jbatch.resources;

import org.jberet.cdi.JobScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

//@ApplicationScoped
public class Producer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @JobScoped
    @Produces
    public EntityManager produceEntityManager() {
        return Persistence.createEntityManagerFactory("h2-test").createEntityManager();
    }
}
