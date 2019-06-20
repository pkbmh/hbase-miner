import java.util.Iterator;
import java.util.List;

import com.ngdata.sep.EventListener;
import com.ngdata.sep.PayloadExtractor;
import com.ngdata.sep.SepEvent;
import com.ngdata.sep.SepModel;
import com.ngdata.sep.impl.BasePayloadExtractor;
import com.ngdata.sep.impl.SepConsumer;
import com.ngdata.sep.impl.SepModelImpl;
import com.ngdata.sep.util.zookeeper.ZkUtil;
import com.ngdata.sep.util.zookeeper.ZooKeeperItf;
import model.Event;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * A simple consumer that just logs the events.
 */
public class LoggingConsumer {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.setBoolean("hbase.replication", true);

        ZooKeeperItf zk = ZkUtil.connect("localhost", 20000);
        SepModel sepModel = new SepModelImpl(zk, conf);

        final String subscriptionName = "logger";

        if (!sepModel.hasSubscription(subscriptionName)) {
            sepModel.addSubscriptionSilent(subscriptionName);
        }

        PayloadExtractor payloadExtractor = new BasePayloadExtractor(Bytes.toBytes("sep-user-demo"), Bytes.toBytes("info"),
                Bytes.toBytes("payload"));

        SepConsumer sepConsumer = new SepConsumer(subscriptionName, 0, new EventLogger(), 1, "localhost", zk, conf,
                payloadExtractor);

        sepConsumer.start();
        System.out.println("Started");

        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }

    private static class EventLogger implements EventListener {
        @Override
        public void processEvents(List<SepEvent> sepEvents) {
            for (SepEvent sepEvent : sepEvents) {
                System.out.println("Received event:");
                System.out.println("  table = " + Bytes.toString(sepEvent.getTable()));
                for (Cell kv : sepEvent.getKeyValues()) {
                    System.out.println("column family:: " + Bytes.toString(CellUtil.cloneFamily(kv)));
                    System.out.println("column name:: " + Bytes.toString(CellUtil.cloneQualifier(kv)));
                    System.out.println("column key:: " + Bytes.toString(CellUtil.cloneRow(kv)));
                    System.out.println("column value:: " + Bytes.toString(CellUtil.cloneValue(kv)));
                    System.out.println("Operation:: " + getEventType(kv));

                    Iterator<Tag> i = CellUtil.tagsIterator(kv.getTagsArray(), kv.getTagsOffset(), kv.getTagsLength());
                    while (i.hasNext()) {
                        Tag t = i.next();

                        if (TagType.TTL_TAG_TYPE == t.getType()) {
                            long ts = kv.getTimestamp();
                            assert t.getTagLength() == Bytes.SIZEOF_LONG;
                            long ttl = Bytes.toLong(t.getBuffer(), t.getTagOffset(), t.getTagLength());
                            System.out.println("Value TTL:: " + ttl);
                        }
                    }
                }
            }
        }
        private static Event.EventType getEventType(Cell cell) {
            if (CellUtil.isDelete(cell)) {
                return Event.EventType.DELETE;
            }
            return Event.EventType.CREATE;
        }
    }


}