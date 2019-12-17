package io.graphenee.accounting.vaadin;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(com.vaadin.spring.VaadinConfiguration.class)
@ComponentScan("io.graphenee.accounting.vaadin")
public class GrapheneeAccountingVaadinConfiguration {

}
