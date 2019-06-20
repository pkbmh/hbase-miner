package transformer;

import com.ngdata.sep.SepEvent;
import model.Event;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.Tag;
import org.apache.hadoop.hbase.TagType;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Iterator;

public class HBaseCellToEventTransformer implements Transformer<SepEvent, Event> {
    @Override
    public Event transform(SepEvent sepEvent) {
        // FIXME Currently only one CF one column need to generate the list of event or something
        for (Cell cell : sepEvent.getKeyValues()) {
            long ttl = Long.MAX_VALUE;
            Iterator<Tag> i = CellUtil.tagsIterator(cell.getTagsArray(), cell.getTagsOffset(), cell.getTagsLength());
            while (i.hasNext()) {
                Tag t = i.next();
                if (TagType.TTL_TAG_TYPE == t.getType()) {
                    long ts = cell.getTimestamp();
                    assert t.getTagLength() == Bytes.SIZEOF_LONG;
                    ttl = Bytes.toLong(t.getBuffer(), t.getTagOffset(), t.getTagLength());
                }
            }
            return Event.builder().tableName(Bytes.toString(sepEvent.getTable()).replace(':', '-'))
                    .columnFamily(Bytes.toString(CellUtil.cloneFamily(cell)))
                    .columnName(Bytes.toString(CellUtil.cloneQualifier(cell)))
                    .columnKey(Bytes.toString(CellUtil.cloneRow(cell)))
                    .columnValue(Bytes.toString(CellUtil.cloneValue(cell)))
                    .eventType(getEventType(cell))
                    .receiveTimestamp(System.currentTimeMillis())
                    .sourceTimestamp(cell.getTimestamp())
                    .ttl(ttl)
                    .build();
        }
        return null;
    }
    private static Event.EventType getEventType(Cell cell) {
        if (CellUtil.isDelete(cell)) {
            return Event.EventType.DELETE;
        }
        return Event.EventType.CREATE;
    }
}
