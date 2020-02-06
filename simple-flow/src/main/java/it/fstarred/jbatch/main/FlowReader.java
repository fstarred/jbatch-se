package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named
public class FlowReader extends AbstractItemReader {

    @Inject
    Logger logger;

    @Inject
    StepContext stepContext;

    @Inject
    JobContext jobContext;

    @SuppressWarnings("rawtypes")
    List data;

    int index = 0;

    @Inject
    EntityManager entityManager;

    @Override
    public Object readItem() throws Exception {
        return (index < data.size()) ? data.get(index++) : null;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        this.data = Optional.of(jobContext.getTransientUserData()).map(List.class::cast).orElseThrow(OperationNotSupportedException::new);
        jobContext.setTransientUserData(null);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        entityManager.getTransaction().begin();
        return index;
    }
}
