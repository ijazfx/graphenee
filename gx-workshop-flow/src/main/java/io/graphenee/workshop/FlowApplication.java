package io.graphenee.workshop;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.util.TRCalendarUtil;

@SpringBootApplication
public class FlowApplication {

    @Autowired
    GrapheneeCoreConfiguration graphenee;

    public static void main(String[] args) {
        TRCalendarUtil.dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
        TRCalendarUtil.dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
        TRCalendarUtil.timeFormatter = new SimpleDateFormat("hh:mm a");

        SpringApplication.run(FlowApplication.class, args);
    }

}
