package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    private String tableName;
    private String columnFamily;
    private String columnName;
    private String columnKey;
    private String columnValue;
    private EventType eventType;
    private long receiveTimestamp;
    private long sourceTimestamp;
    private long ttl;

    public enum EventType {
        UPDATE,
        CREATE,
        DELETE
    }
}
