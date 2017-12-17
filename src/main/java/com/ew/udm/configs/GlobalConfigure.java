package com.ew.udm.configs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ew.udm.service.mapper")
public class GlobalConfigure {

}
