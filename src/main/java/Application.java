import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ngdata.sep.EventListener;
import config.Config;
import listener.SepEventListener;
import miner.SepMiner;
import producer.EventProducer;
import producer.KafkaEventProducer;
import producer.LogProducer;
import producer.Logger;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private void startApplication(String configFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Config config = mapper.readValue(getFileFromResources(configFile), Config.class);
            EventListener eventListener = new SepEventListener(getAllProducers(config));
            SepMiner sepMiner = new SepMiner(config.getMinerConfig(), eventListener);
            sepMiner.start();
            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<EventProducer> getAllProducers(Config config) {
        List<EventProducer> eventProducers = new ArrayList<>();
        if (config.getKafkaConfig() != null ) {
           eventProducers.add(KafkaEventProducer.create(config.getKafkaConfig()));
        }
        if(config.isConsoleProducer()) {
           eventProducers.add(new LogProducer(new Logger() {}));
        }
        return eventProducers;
    }
    // get file from classpath, resources folder
    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    public static void main(String[] args) {
        String file;
        if (args.length > 0) {
            file = args[0];
        } else {
            file = "config.yaml";
        }
        new Application().startApplication(file);
    }
}
