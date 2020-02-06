package it.fstarred.jbatch.main;

import it.fstarred.jbatch.entity.Customer;
import org.slf4j.Logger;

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MyProcessor implements ItemProcessor {

    @Inject
    Logger logger;

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public Object processItem(Object o) throws Exception {
        logger.info("processItem");
        Customer item = (Customer)o;
        item.setSurname(item.getSurname() + " Unit Test");
        return o;
    }
}
