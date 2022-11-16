package com.orca.spring.breacher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class AttackSurfaceService implements CommandLineRunner
{
	// region Dependencies

	@Autowired
	private ApplicationContext applicationContext;

	// endregion

	public static void main(String[] args)
	{
		SpringApplication.run(AttackSurfaceService.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		log.info("[Breacher]: Successfully initialized. [Application Context -> ({})]", applicationContext);
	}
}
