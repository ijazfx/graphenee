package io.graphenee.workshop;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.graphenee.core.GrapheneeCoreConfiguration;
import io.graphenee.util.TRCalendarUtil;

@SpringBootApplication
public class FlowApplication extends SpringBootServletInitializer {

	@Autowired
	GrapheneeCoreConfiguration graphenee;

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FlowApplication.class);
    }

	public static void main(String[] args) {
		TRCalendarUtil.setCustomDateFormatter(new SimpleDateFormat("dd.MM.yyyy"));
		TRCalendarUtil.setCustomDateTimeFormatter(new SimpleDateFormat("dd.MM.yyyy hh:mm a"));
		TRCalendarUtil.setCustomTimeFormatter(new SimpleDateFormat("hh:mm a"));

		SpringApplication.run(FlowApplication.class, args);
	}

}
