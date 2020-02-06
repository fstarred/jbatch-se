package it.fstarred.jbatch.main;

import org.slf4j.Logger;

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
public class MyReaderFails extends AbstractItemReader {

    @Inject
    Logger logger;

    List<Integer> output;
    int index;

    @Inject
    StepContext stepContext;

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public Object readItem() throws Exception {
        logger.info("reading item: {}", index);
        return index < output.size() ? output.get(index++) : null;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("open: {}", checkpoint);
        int startIndex = Optional.ofNullable(checkpoint).map(Integer.class::cast).orElse(0);
        output = IntStream.range(startIndex, 30).boxed().collect(Collectors.toList());
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        logger.info("current checkpoint: {}", index);
        return index;
    }
}
