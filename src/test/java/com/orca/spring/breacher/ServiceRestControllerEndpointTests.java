package com.orca.spring.breacher;

import com.orca.spring.breacher.controller.AttackSurfaceServiceController;
import com.orca.spring.breacher.controller.AttackSurfaceServiceCoreEngine;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.exception.InternalRestEndpointException;
import com.orca.spring.breacher.exception.VirtualMachineNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeVmIdentifier;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeVmIdentifierList;
import static com.orca.spring.breacher.utils.ServiceTestActuatorMetricsUtils.randomizeRuntimeStatistics;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceRestControllerEndpointTests extends AttackSurfaceServiceTestsSuiteBase
{
	static
	{
		CloudEnvironmentTestSampleEngine.Instance.loadRandom();
	}

	// region Dependencies

	@Mock
	private AttackSurfaceServiceCoreEngine engine;

	@InjectMocks
	private AttackSurfaceServiceController controller;

	// endregion

	@Test
	void testEngineDependencyInjectionForController_smokeTest()
	{
		Assertions.assertNotNull(engine);
		Assertions.assertNotNull(controller);
		Assertions.assertEquals(engine, controller.getEngine());
	}

	@Test
	void testAnalyzeMachineAttackSurface_mockBasicScenario() throws VirtualMachineNotFoundException, InternalRestEndpointException
	{
		var assetIdentifierRequest = randomizeVmIdentifier();
		var assetIdentifiersResponse = randomizeVmIdentifierList();

		when(engine.analyzeMachineAttackSurface(assetIdentifierRequest)).thenReturn(assetIdentifiersResponse);

		Assertions.assertEquals(assetIdentifiersResponse, controller.analyzeMachineAttackSurface(assetIdentifierRequest));
	}

	@Test
	void testAnalyzeMachineAttackSurface_mockErrorHandlingInvalidAssetIdentifier() throws VirtualMachineNotFoundException, InternalRestEndpointException
	{
		var validAssetIdentifier = randomizeVmIdentifier();
		var validAssetIdentifiersResponse = randomizeVmIdentifierList();

		when(engine.analyzeMachineAttackSurface(validAssetIdentifier)).thenReturn(validAssetIdentifiersResponse);

		var invalidAssetIdentifier = randomizeVmIdentifier();

		when(engine.analyzeMachineAttackSurface(invalidAssetIdentifier)).thenThrow(VirtualMachineNotFoundException.class);

		Assertions.assertEquals(validAssetIdentifiersResponse, controller.analyzeMachineAttackSurface(validAssetIdentifier));
		Assertions.assertThrows(VirtualMachineNotFoundException.class, () -> { controller.analyzeMachineAttackSurface(invalidAssetIdentifier); });
	}

	@Test
	void testAnalyzeMachineAttackSurface_mockErrorHandlingInternalRestFailure() throws VirtualMachineNotFoundException, InternalRestEndpointException
	{
		var validAssetIdentifier = randomizeVmIdentifier();
		var validAssetIdentifiersResponse = randomizeVmIdentifierList();

		when(engine.analyzeMachineAttackSurface(validAssetIdentifier)).thenReturn(validAssetIdentifiersResponse);

		var invalidAssetIdentifier = randomizeVmIdentifier();

		when(engine.analyzeMachineAttackSurface(invalidAssetIdentifier)).thenThrow(NullPointerException.class);

		Assertions.assertEquals(validAssetIdentifiersResponse, controller.analyzeMachineAttackSurface(validAssetIdentifier));
		Assertions.assertThrows(InternalRestEndpointException.class, () -> { controller.analyzeMachineAttackSurface(invalidAssetIdentifier); });
	}

	@Test
	void testFetchServiceRuntimeStatistics_mockBasicScenario() throws InternalRestEndpointException
	{
		var serviceStatisticsResponse = randomizeRuntimeStatistics();

		when(engine.fetchServiceRuntimeStatistics()).thenReturn(serviceStatisticsResponse);

		Assertions.assertEquals(serviceStatisticsResponse, controller.fetchServiceRuntimeStatistics());
	}

	@Test
	void testFetchServiceRuntimeStatistics_mockErrorHandlingInternalRestFailure() throws InternalRestEndpointException
	{
		var serviceStatisticsResponse = randomizeRuntimeStatistics();

		when(engine.fetchServiceRuntimeStatistics()).thenReturn(serviceStatisticsResponse).thenThrow(NullPointerException.class);

		Assertions.assertEquals(serviceStatisticsResponse, controller.fetchServiceRuntimeStatistics());
		Assertions.assertThrows(InternalRestEndpointException.class, () -> { controller.fetchServiceRuntimeStatistics(); });
	}
}
