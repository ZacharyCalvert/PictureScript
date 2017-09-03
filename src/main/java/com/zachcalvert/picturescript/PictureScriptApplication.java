package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.conf.LoadConfiguration;
import com.zachcalvert.picturescript.out.conf.OutputConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({LoadConfiguration.class, OutputConfiguration.class})
public class PictureScriptApplication {
	public static void main(String[] args) {
		SpringApplication.run(PictureScriptApplication.class, args);
	}
}
