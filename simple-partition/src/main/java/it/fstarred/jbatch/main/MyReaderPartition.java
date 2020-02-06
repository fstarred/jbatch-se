package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
public class MyReaderPartition extends AbstractItemReader {

    @Inject
    Logger logger;

    List<Integer> output;
    int index;

    @Inject
    StepContext stepContext;

    @Inject
    @BatchProperty(name = "start")
    protected int partitionStart;

    @Inject
    @BatchProperty(name = "end")
    protected Integer partitionEnd;

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public Object readItem() throws Exception {
        final Object o = index < output.size() ? output.get(index++) : null;
        logger.info("reading item: {}", o);
        return o;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("open checkpoint: {} - start: {} - end: {}", checkpoint, partitionStart, partitionEnd);
        final int start = partitionStart;
        final int end = partitionEnd;
        final Integer startIndex = Optional.ofNullable(checkpoint).map(Integer.class::cast).orElse(start);
        output = IntStream.range(startIndex, end).boxed().collect(Collectors.toList());
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.info("current checkpoint: {}", index);
        return index;
    }
}
