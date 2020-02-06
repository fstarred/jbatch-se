package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Named
public class MyProcessorFails implements ItemProcessor {

    @Inject
    Logger logger;

    @Inject
    @BatchProperty(name = "itemnumerror")
    Integer itemnumerror;

    @Inject
    @BatchProperty(name = "error")
    Boolean error;

    @Inject
    @BatchProperty(name = "addend")
    Integer addend;

    @Override
    public Object processItem(Object o) throws Exception {
        if (itemnumerror == o && error.booleanValue()) {
            logger.error("input: {}", o);
            throw new RuntimeException();
        }
        Integer output = (Integer) o + Optional.ofNullable(addend).orElse(0);
        logger.info("processed item: {}", output);

        return output;
    }
}
