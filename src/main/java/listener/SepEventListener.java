package listener;

import com.ngdata.sep.EventListener;
import com.ngdata.sep.SepEvent;
import execptions.ProducerException;
import model.Event;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;
import producer.EventProducer;
import transformer.HBaseCellToEventTransformer;
import transformer.Transformer;

import java.rmi.UnexpectedException;
import java.util.List;

public class SepEventListener implements EventListener {
    private final List<EventProducer> producers;
    private final Transformer<SepEvent, Event> transformer;
    public SepEventListener(List<EventProducer> producers) {
        this.producers = producers;
        this.transformer = new HBaseCellToEventTransformer();
    }

    @Override
    public void processEvents(List<SepEvent> sepEvents) {
        for (SepEvent sepEvent : sepEvents) {
            System.out.println("Received event:");
            System.out.println("  table = " + Bytes.toString(sepEvent.getTable()));
//            for (Cell cell : sepEvent.getKeyValues()) {
                Event event = transformer.transform(sepEvent);
                if (producers != null) {
                    for(EventProducer producer : producers) {
                        try {
                            producer.produce(event);
                        } catch (ProducerException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }
//            }
        }
    }
}
