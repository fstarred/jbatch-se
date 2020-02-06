package it.fstarred.jbatch.main;

import it.fstarred.jbatch.test.AJobTest;
import org.jberet.runtime.JobExecutionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.util.concurrent.TimeUnit;

public class SimpleBatchletTest extends AJobTest {

    Logger logger = LoggerFactory.getLogger(SimpleBatchletTest.class);

    @Test
    void batchlet() throws InterruptedException {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        long id = jobOperator.start("simple-batchlet", null);

        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());
    }


}
