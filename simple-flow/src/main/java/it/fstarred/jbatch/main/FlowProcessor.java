package it.fstarred.jbatch.main;

import it.fstarred.jbatch.entity.Customer;
import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoField;

@Named
public class FlowProcessor implements ItemProcessor {

    @Inject
    Logger logger;

    @Inject
    @BatchProperty(name = "year")
    Integer year;

    @Override
    public Object processItem(Object o) throws Exception {
        final Customer customer = (Customer) o;
        final int birthYear = Instant.ofEpochMilli(customer.getBirth().getTime()).atZone(ZoneId.systemDefault()).get(ChronoField.YEAR);
        return (birthYear > year) ?
                customer :
                null;
    }
}
