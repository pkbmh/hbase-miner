package config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

@NoArgsConstructor
@Getter
public class MinerConfig {
    private Set<String> zookeepers;
    private  int zkPort;
    private String subscriptionName = "sepMiner";
    private int numThread = 1;

    public String getZookeeperConnectString() {
        return StringUtils.join(zookeepers, ":"+ zkPort +",");
    }
}
