package it.fstarred.jbatch.main;

import it.fstarred.jbatch.test.AJobTest;
import org.jberet.runtime.JobExecutionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class FlowTest extends AJobTest {

    Logger logger = LoggerFactory.getLogger(FlowTest.class);

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(new Locale("en", "US"));
    }

    @Test
    void flow() throws InterruptedException {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        long id = jobOperator.start("flow", null);

        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

        Properties properties = new Properties();
        properties.setProperty("year", "1990");

        long newid = jobOperator.start("flow", properties);

        final JobExecutionImpl newJobExecution = (JobExecutionImpl) jobOperator.getJobExecution(newid);
        newJobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.FAILED, newJobExecution.getBatchStatus());

//        verifyMetric(Metric.MetricType.COMMIT_COUNT, jobExecution.getStepExecutions().get(0), 4);
//        verifyMetric(Metric.MetricType.READ_SKIP_COUNT, jobExecution.getStepExecutions().get(0), 0);
    }
}
