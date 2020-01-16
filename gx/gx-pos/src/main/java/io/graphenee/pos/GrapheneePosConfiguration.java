package io.graphenee.pos;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.graphenee.core.GrapheneeCoreConfiguration;

@Configuration
@AutoConfigureAfter(GrapheneeCoreConfiguration.class)
@ComponentScan("io.graphenee.pos")
public class GrapheneePosConfiguration {

}
