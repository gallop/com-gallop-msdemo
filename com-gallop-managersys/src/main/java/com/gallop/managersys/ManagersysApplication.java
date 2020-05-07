package com.gallop.managersys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages="com.gallop.managersys.mapper")
@ComponentScan(basePackages= {"com.gallop"})
public class ManagersysApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagersysApplication.class, args);
	}

}
