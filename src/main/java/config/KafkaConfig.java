package config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
public class KafkaConfig {
    @NonNull private Set<String> bootstrapServers;
    private int serverPort = 9092;
    @NonNull private Set<String> zookeepers;
    private int zookeeperPort = 2181;
    private Map<String, String> consumerConfig;
    private Map<String, String> producerConfig;
    public String getBootstrapServersConnectString() {
        StringBuffer stringBuffer = new StringBuffer();
        zookeepers.forEach(v -> stringBuffer.append(v).append(":").append(serverPort).append(","));
        stringBuffer.setLength(stringBuffer.length() - 1);
        return stringBuffer.toString();
   }
}
