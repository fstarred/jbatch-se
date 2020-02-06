package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class MyWriterFails extends AbstractItemWriter {

    @Inject
    Logger logger;

    @Inject
    StepContext stepContext;

    @SuppressWarnings("squid:S2629")
    @Override
    public void writeItems(List<Object> list) throws Exception {
        logger.info("output: {}", list.stream().map(String::valueOf).collect(Collectors.joining(" , ", "{", "}")));
        ArrayList<Integer> processed = Optional.ofNullable(stepContext.getPersistentUserData()).map(ArrayList.class::cast).orElse(new ArrayList<Integer>());
        processed.addAll(list.stream().map(Integer.class::cast).collect(Collectors.toList()));
        stepContext.setPersistentUserData(processed);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        final Integer persistedDataSize = Optional.of(stepContext.getPersistentUserData()).map(List.class::cast).map(List::size).orElse(0);
        logger.info("checkpoint reached, persisted data size: {}", persistedDataSize);
        return persistedDataSize;
    }
}
