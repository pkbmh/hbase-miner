package producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import config.KafkaConfig;
import execptions.ProducerException;
import lombok.RequiredArgsConstructor;
import model.Event;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.SerializerDeserializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
public class KafkaEventProducer implements EventProducer {
    private final Producer<String, String> producer;
    public static KafkaEventProducer create(KafkaConfig kafkaConfig) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServersConnectString());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "test");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (kafkaConfig.getProducerConfig() != null) props.putAll(kafkaConfig.getProducerConfig());
        return new KafkaEventProducer(new KafkaProducer<>(props));
    }
    @Override
    public void produceAsync(Event event) throws ProducerException {
        try {
            producer.send(new ProducerRecord<>(event.getTableName(), event.getColumnKey(), SerializerDeserializer.SerializerToString(event)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ProducerException(e.getMessage());
        }
    }

    @Override
    public void produce(Event event) throws ProducerException {
        try {
            producer.send(new ProducerRecord<>(event.getTableName(), event.getColumnKey(), SerializerDeserializer.SerializerToString(event))).get();
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new ProducerException(e.getMessage());
        }
    }
}
