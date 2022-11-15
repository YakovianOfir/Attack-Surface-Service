package com.orca.spring.breacher;

import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;

class ServiceApplicationContextTests extends AttackSurfaceServiceTestsSuiteBase
{
	static
	{
		CloudEnvironmentTestSampleEngine.Instance.loadRandom();
	}
}
