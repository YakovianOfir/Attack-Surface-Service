package com.orca.spring.breacher;

import com.orca.spring.breacher.environment.CloudEnvironmentTestSamplesProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.ServiceCloudEnvironmentSystemPropertyName;

@SpringBootTest
class AttackSurfaceServiceTests
{
	static
	{
		System.setProperty(
				ServiceCloudEnvironmentSystemPropertyName,
				CloudEnvironmentTestSamplesProvider.Instance.readResourceContents("input-3.json"));
	}

	@Test
	void contextLoads()
	{}
}
