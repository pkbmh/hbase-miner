package config;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
public class HBaseConfig {
    private Set<String> zookeepers;
    private Map<String, String> config;
}
