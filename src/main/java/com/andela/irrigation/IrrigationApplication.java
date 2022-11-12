package com.andela.irrigation;

import com.andela.irrigation.scheduler.IrrigationEventScheduler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EntityScan("com.andela")
@ComponentScan(basePackages = "com.andela")
public class IrrigationApplication {

	@Autowired private IrrigationEventScheduler irrigationEventScheduler;

	public static void main(String[] args) {
		SpringApplication.run(IrrigationApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@PostConstruct
	private void startEventScheduler(){
		if (irrigationEventScheduler != null) irrigationEventScheduler.start();
	}
}
