package io.graphenee.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.graphenee.core.GrapheneeCoreConfiguration;

@Configuration
@ComponentScan(basePackages = { "io.graphenee", "io.graphenee.workshop" })
public class FlowApplicationConfiguration {

    @Autowired
    GrapheneeCoreConfiguration graphenee;

}
