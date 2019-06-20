package producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import execptions.ProducerException;
import lombok.RequiredArgsConstructor;
import model.Event;

@RequiredArgsConstructor
public class LogProducer implements EventProducer {
    private final Logger logger;

    @Override
    public void produce(Event event) throws ProducerException {
        try {
            logger.print(utils.SerializerDeserializer.SerializerToString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw  new ProducerException(e.getMessage());
        }
    }

    @Override
    public void produceAsync(Event event) throws ProducerException {
        try {
            logger.print(utils.SerializerDeserializer.SerializerToString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw  new ProducerException(e.getMessage());
        }
    }
}
