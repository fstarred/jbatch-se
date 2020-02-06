package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.Decider;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.StepExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named
public class FlowDecider implements Decider {

    @Inject
    StepContext stepContext;

    @Inject
    JobContext jobContext;

    @Inject
    Logger logger;

    static final String NEXT = "next";
    static final String FAIL = "st-fail";

    @Override
    public String decide(StepExecution[] stepExecutions) throws Exception {
        final StepExecution stepExecution = stepExecutions[0];
        final BatchStatus batchStatus = stepExecution.getBatchStatus();
        logger.info("status: {}", batchStatus);
        @SuppressWarnings("rawtypes")
        final List list = Optional.of(jobContext.getTransientUserData()).map(List.class::cast).orElseThrow(UnsupportedOperationException::new);
        return list.size() > 5 ?
                NEXT :
                FAIL;
    }
}
