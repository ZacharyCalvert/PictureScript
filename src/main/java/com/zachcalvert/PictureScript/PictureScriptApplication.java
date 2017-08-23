package com.zachcalvert.PictureScript;

import com.zachcalvert.PictureScript.conf.LoadConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LoadConfiguration.class)
public class PictureScriptApplication {
	public static void main(String[] args) {
		SpringApplication.run(PictureScriptApplication.class, args);
	}
}
