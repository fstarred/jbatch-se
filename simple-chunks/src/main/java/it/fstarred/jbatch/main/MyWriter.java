package it.fstarred.jbatch.main;

import org.slf4j.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
public class MyWriter extends AbstractItemWriter {

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Inject
    Logger logger;

    @Inject
    EntityManager entityManager;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("open: {}", checkpoint);
        super.open(checkpoint);
    }

    @Override
    public void writeItems(List<Object> list) throws Exception {
        logger.info("writeItems: {}", list.size());
//        list.stream()
//                .map(Listino.class::cast)
//                .forEach(entityManager::merge);
        entityManager.getTransaction().commit();
        ArrayList<List<Object>> recorded = (ArrayList<List<Object>>) stepContext.getPersistentUserData();
        if (recorded == null) {
            recorded = new ArrayList<>();
        }
        recorded.add(list);
        stepContext.setPersistentUserData(recorded);
    }

    @Override
    public void close() throws Exception {
        logger.info("close");
        super.close();
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return super.checkpointInfo();
    }
}
