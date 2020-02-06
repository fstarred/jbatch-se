package it.fstarred.jbatch.main;

import it.fstarred.jbatch.entity.Customer;
import org.slf4j.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class FlowWriter extends AbstractItemWriter {

    @Inject
    Logger logger;

    @Inject
    StepContext stepContext;

    @Inject
    JobContext jobContext;

    @Inject
    EntityManager entityManager;

    @Override
    public void writeItems(List<Object> list) throws Exception {
        entityManager.getTransaction().commit();
        @SuppressWarnings("unchecked")
        ArrayList<Customer> processed = Optional.ofNullable(jobContext.getTransientUserData()).map(ArrayList.class::cast).orElse(new ArrayList<Customer>());
        processed.addAll(list.stream().map(Customer.class::cast).collect(Collectors.toList()));
        jobContext.setTransientUserData(processed);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return super.checkpointInfo();
    }
}
