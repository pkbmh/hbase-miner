package producer;

import execptions.ProducerException;
import model.Event;

public interface EventProducer {
    void produce(Event event) throws ProducerException;
    void produceAsync(Event event)throws ProducerException;
}
