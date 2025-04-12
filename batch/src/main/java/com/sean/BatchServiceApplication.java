package com.sean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sean.batch.utils.VersionCommand;

@SpringBootApplication
@EnableScheduling
public class BatchServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		if (args.length == 0) {
			SpringApplication.run(BatchServiceApplication.class, args);
		} else {
			if (args[0].equals("-v") || args[0].equals("--version")) {
				String execute = VersionCommand.execute(args[0]);
				System.out.println(execute);
			}
		}
	}

}
