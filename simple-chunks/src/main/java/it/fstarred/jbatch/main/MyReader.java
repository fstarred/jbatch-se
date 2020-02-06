package it.fstarred.jbatch.main;

import it.fstarred.jbatch.entity.Customer;
import org.slf4j.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

@Named
public class MyReader extends AbstractItemReader {

    @Inject
    Logger logger;

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Inject
    EntityManager entityManager;

    List<Customer> output;

    int index = 0;

    @Override
    public Object readItem() throws Exception {
        logger.info("readItem: {}", index);
        return index < (output.size()) ?
                output.get(index++) :
                null;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("open: {}", checkpoint);
        entityManager.getTransaction().begin();
        final CriteriaQuery<Customer> criteria = entityManager
                .getCriteriaBuilder()
                .createQuery(Customer.class);
        criteria.select(criteria.from(Customer.class));
        output = entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.info("checkpoint");
        if (index < output.size()) {
            entityManager.getTransaction().begin();
        }
        return index;
    }

    @Override
    public void close() throws Exception {
        logger.info("close");
        super.close();
    }
}
