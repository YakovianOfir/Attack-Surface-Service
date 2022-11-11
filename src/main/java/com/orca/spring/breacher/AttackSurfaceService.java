package com.orca.spring.breacher;

import com.orca.spring.breacher.controller.AttackSurfaceServiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AttackSurfaceService implements CommandLineRunner
{
	@Autowired
	private AttackSurfaceServiceController serviceController;
	private AttackSurfaceServiceSettings attackSurfaceServiceSettings;

	public static void main(String[] args)
	{
		SpringApplication.run(AttackSurfaceService.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{}
}
