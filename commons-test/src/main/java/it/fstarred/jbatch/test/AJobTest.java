package it.fstarred.jbatch.test;

import org.jberet.runtime.PartitionExecutionImpl;
import org.junit.jupiter.api.Assertions;

import javax.batch.runtime.Metric;
import javax.batch.runtime.StepExecution;

@SuppressWarnings("squid:S2187")
public class AJobTest {

    protected void verifyMetric(final Metric.MetricType metricType, StepExecution stepExecution0, final long value) {
        boolean metricFound = false;
        final Metric[] metrics = stepExecution0.getMetrics();
        for (final Metric m : metrics) {
            if (m.getType() == metricType) {
                metricFound = true;
                Assertions.assertEquals(value, m.getValue());
                break;
            }
        }
        if (!metricFound) {
            throw new IllegalStateException("Unmatched MetricType " + metricType);
        }
    }

    protected void verifyMetric(final Metric.MetricType metricType, PartitionExecutionImpl partitionExecution, final long value) {
        boolean metricFound = false;
        final Metric[] metrics = partitionExecution.getMetrics();
        for (final Metric m : metrics) {
            if (m.getType() == metricType) {
                metricFound = true;
                Assertions.assertEquals(value, m.getValue());
                break;
            }
        }
        if (!metricFound) {
            throw new IllegalStateException("Unmatched MetricType " + metricType);
        }
    }
}
