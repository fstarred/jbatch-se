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
import javax.batch.runtime.Metric;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ChunksTest extends AJobTest {

    Logger logger = LoggerFactory.getLogger(ChunksTest.class);

    @BeforeAll
    static void beforeAll() {
        Locale.setDefault(new Locale("en", "US"));
    }

    @Test
    void chunks() throws InterruptedException {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        long id = jobOperator.start("simple-chunks", null);

        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

        verifyMetric(Metric.MetricType.COMMIT_COUNT, jobExecution.getStepExecutions().get(0), 4);
        verifyMetric(Metric.MetricType.READ_SKIP_COUNT, jobExecution.getStepExecutions().get(0), 0);
    }

    @Test
    void chunks_fails() throws InterruptedException {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        final int itemNumError = 15;

        final Properties properties = new Properties();
        properties.setProperty("throwerror", Boolean.TRUE.toString());
        properties.setProperty("itemerror", String.valueOf(itemNumError));

        long id = jobOperator.start("chunks-fails", properties);

        logger.info("job started: {}", id);

        JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.FAILED, jobExecution.getBatchStatus());

        verifyMetric(Metric.MetricType.READ_COUNT, jobExecution.getStepExecutions().get(0), itemNumError + 1);
        verifyMetric(Metric.MetricType.COMMIT_COUNT, jobExecution.getStepExecutions().get(0), 1);
        verifyMetric(Metric.MetricType.ROLLBACK_COUNT, jobExecution.getStepExecutions().get(0), 1);
        verifyMetric(Metric.MetricType.WRITE_COUNT, jobExecution.getStepExecutions().get(0), 10);

        properties.setProperty("throwerror", Boolean.FALSE.toString());

        final long newId = jobOperator.restart(id, properties);

        logger.info("job restarted: {} with new id: {}", id, newId);

        final JobExecutionImpl newJobExecution = (JobExecutionImpl) jobOperator.getJobExecution(newId);
        newJobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.COMPLETED, newJobExecution.getBatchStatus());

        verifyMetric(Metric.MetricType.READ_COUNT, newJobExecution.getStepExecutions().get(0), 20);
        verifyMetric(Metric.MetricType.COMMIT_COUNT, newJobExecution.getStepExecutions().get(0), 2 + 1); // last commit is reserved for items % chunk item count
        verifyMetric(Metric.MetricType.ROLLBACK_COUNT, newJobExecution.getStepExecutions().get(0), 0);
        verifyMetric(Metric.MetricType.WRITE_COUNT, newJobExecution.getStepExecutions().get(0), 20);

    }
}
