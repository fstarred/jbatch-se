package it.fstarred.jbatch.main;

import it.fstarred.jbatch.test.AJobTest;
import org.jberet.runtime.JobExecutionImpl;
import org.jberet.runtime.PartitionExecutionImpl;
import org.jberet.runtime.StepExecutionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SimplePartitionTest extends AJobTest {

    Logger logger = LoggerFactory.getLogger(SimplePartitionTest.class);

    @Test
    void chunks_partition() throws InterruptedException {

        final JobOperator jobOperator = BatchRuntime.getJobOperator();

        final int addend = 1;

        final Properties properties = new Properties();
        properties.setProperty("addend", String.valueOf(addend));

        long id = jobOperator.start("chunks-partition", properties);

        final JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(5, TimeUnit.MINUTES);
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getBatchStatus());

        final List<PartitionExecutionImpl> partitionExecutions = ((StepExecutionImpl) jobExecution.getStepExecutions().get(0)).getPartitionExecutions();

        partitionExecutions.forEach(partitionExecution -> {
            verifyMetric(Metric.MetricType.READ_COUNT, partitionExecution, 10);
            verifyMetric(Metric.MetricType.WRITE_COUNT, partitionExecution, 10);
            verifyMetric(Metric.MetricType.COMMIT_COUNT, partitionExecution, 3);
        });
    }
}
