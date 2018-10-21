package com.ch.solrJD;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ch.solrJD.dao")
public class SolrJdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolrJdApplication.class, args);
	}
}
