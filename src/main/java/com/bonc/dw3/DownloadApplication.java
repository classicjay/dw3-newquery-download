package com.bonc.dw3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * <p>Title: BONC -  DownloadApplication</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@Configuration
@CrossOrigin(origins = "*")
public class DownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownloadApplication.class, args);
	}
}
