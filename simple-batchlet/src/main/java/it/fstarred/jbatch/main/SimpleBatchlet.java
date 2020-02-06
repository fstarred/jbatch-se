package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SimpleBatchlet extends AbstractBatchlet {

    @Inject
    @BatchProperty(name = "foo")
    String foo;

    @Inject
    Logger logger;

    @Inject
    StepContext stepContext;

    @Override
    public String process() throws Exception {
        final String say = stepContext.getProperties().getProperty("say");
        logger.info("just say: {}, {}", say, foo);
        return BatchStatus.COMPLETED.toString();
    }
}
