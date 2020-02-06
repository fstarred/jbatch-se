package it.fstarred.jbatch.main;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Named;
import java.util.Properties;

@Named
public class MyMapper implements PartitionMapper {

    @Override
    public PartitionPlan mapPartitions() {
        return new PartitionPlanImpl() {

            @Override
            public int getPartitions() {
                return 3;
            }

            @Override
            public int getThreads() {
                return 3;
            }

            @Override
            public Properties[] getPartitionProperties() {
                final Properties[] props = new Properties[getPartitions()];

                for (int i = 0; i < getPartitions(); i++) {
                    props[i] = new Properties();
                    props[i].setProperty("start", String.valueOf(i * 10));
                    props[i].setProperty("end", String.valueOf((i * 10) + 10));
                }
                return props;
            }
        };
    }
}
