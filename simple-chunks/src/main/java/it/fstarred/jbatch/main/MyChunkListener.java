package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.listener.AbstractChunkListener;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MyChunkListener extends AbstractChunkListener {

    @Inject
    Logger logger;

    @Inject
    private JobContext jobContext;

    @Inject
    private StepContext stepContext;

    @Inject
    @BatchProperty
    String stepExitStatus;


    @Override
    public void beforeChunk() throws Exception {
        logger.info("before chunk");
    }

    @Override
    public void onError(final Exception ex) throws Exception {
        logger.info("error chunk");
    }

    @Override
    public void afterChunk() throws Exception {
        logger.info("after chunk");
        stepContext.setExitStatus(stepExitStatus);
        logger.info("exit status: {}", stepContext.getExitStatus());
    }

}
