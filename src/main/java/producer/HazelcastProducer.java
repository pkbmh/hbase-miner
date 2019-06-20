package producer;

import execptions.ProducerException;
import model.Event;

public class HazelcastProducer implements EventProducer {
    @Override
    public void produce(Event event) throws ProducerException {

    }

    @Override
    public void produceAsync(Event event) throws ProducerException {

    }
}
