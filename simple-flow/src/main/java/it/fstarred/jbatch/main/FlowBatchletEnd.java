package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class FlowBatchletEnd extends AbstractBatchlet {

    @Inject
    StepContext stepContext;

    @Inject
    JobContext jobContext;

    @Inject
    Logger logger;

    static final String FAIL = "st-fail";

    @Override
    public String process() throws Exception {
        @SuppressWarnings("rawtypes") final List list = Optional.of(jobContext.getTransientUserData()).map(List.class::cast).orElseThrow(UnsupportedOperationException::new);
        logger.info("list size: {}", list.size());
        return list.size() > 7 ?
                "completed" :
                FAIL;
    }
}
