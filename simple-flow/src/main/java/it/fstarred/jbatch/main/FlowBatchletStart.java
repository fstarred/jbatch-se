package it.fstarred.jbatch.main;

import it.fstarred.jbatch.entity.Customer;
import org.slf4j.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Named
public class FlowBatchletStart extends AbstractBatchlet {

    @Inject
    Logger logger;

    @Inject
    EntityManager entityManager;

    @Inject
    StepContext stepContext;

    @Inject
    JobContext jobContext;

    @Override
    public String process() throws Exception {
        entityManager.getTransaction().begin();
        final CriteriaQuery<Customer> criteria = entityManager
                .getCriteriaBuilder()
                .createQuery(Customer.class);
        criteria.select(criteria.from(Customer.class));
        final List<Customer> output = entityManager.createQuery(criteria).getResultList();

        jobContext.setTransientUserData(output);

        return "processed";
    }
}
