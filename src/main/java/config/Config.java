package config;

import jdk.nashorn.internal.objects.annotations.Property;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
public class Config {
    private MinerConfig minerConfig;
    private KafkaConfig kafkaConfig;
    boolean consoleProducer;
}
