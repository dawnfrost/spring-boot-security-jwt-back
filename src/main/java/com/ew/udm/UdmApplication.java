package com.ew.udm;

import com.ew.udm.configs.GlobalConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ServletComponentScan
@Import({GlobalConfigure.class})
public class UdmApplication {
	public static void main(String[] args) {
		SpringApplication.run(UdmApplication.class, args);
	}
}
