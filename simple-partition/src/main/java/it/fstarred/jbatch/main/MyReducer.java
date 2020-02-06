package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.partition.PartitionReducer;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MyReducer implements PartitionReducer {

    @Inject
    Logger logger;

    @Override
    public void beginPartitionedStep() throws Exception {
        logger.info("beginPartitionedStep");
    }

    @Override
    public void beforePartitionedStepCompletion() throws Exception {
        logger.info("beforePartitionedStepCompletion");
    }

    @Override
    public void rollbackPartitionedStep() throws Exception {
        logger.info("rollbackPartitionedStep");
    }

    @Override
    public void afterPartitionedStepCompletion(PartitionStatus partitionStatus) throws Exception {
        logger.info("afterPartitionedStepCompletion");
    }
}
