package com.orca.spring.breacher;

import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.exception.ControllerErrorResponse;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeAccessibleAsset;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeVmIdentifier;

@Slf4j
class ServiceRestControllerHttpRequestTests extends AttackSurfaceServiceTestsSuiteBase
{
	static
	{
		CloudEnvironmentTestSampleEngine.Instance.load(AttackSurfaceServiceTestsConstants.CloudEnvironmentTestSample3);
	}

	// region Dependencies

	@LocalServerPort
	private int listeningPort;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private VirtualMachineAccessTable accessTable;

	// endregion

	@Test
	void testAnalyzeMachineAttackSurface_sanityHttpRequestResponse()
	{
		var accessibleAsset = randomizeAccessibleAsset(accessTable);
		var attackSurface = accessibleAsset.getValue().stream().map(vm -> vm.getIdentifier().id()).collect(Collectors.toList());

		var serverUrl = String.format("http://localhost:%d", listeningPort);
		var endpointUrl = String.format("%s%s%s", serverUrl, RestControllerServiceApiPath, RestControllerEndpointMappingAttack);
		var restRequestUrl = String.format("%s?%s=%s", endpointUrl, RestControllerEndpointAttackQueryParam, accessibleAsset.getKey().id());

		log.info("[Test-REST-HTTP]: Querying the service -> [URI: ({})]", restRequestUrl);

		var endpointResponse = Arrays.asList(restTemplate.getForObject(restRequestUrl, String[].class));

		log.info("[Test-REST-HTTP]: Service replied back. -> [Response: ({})]", endpointResponse);

		Assertions.assertIterableEquals(attackSurface, endpointResponse);
	}

	@Test
	void testAnalyzeMachineAttackSurface_errorHttpRequestResponse()
	{
		var invalidAssetIdentifier = randomizeVmIdentifier();
		var serverUrl = String.format("http://localhost:%d", listeningPort);
		var endpointUrl = String.format("%s%s%s", serverUrl, RestControllerServiceApiPath, RestControllerEndpointMappingAttack);
		var restRequestUrl = String.format("%s?%s=%s", endpointUrl, RestControllerEndpointAttackQueryParam, invalidAssetIdentifier);

		log.info("[Test-REST-HTTP]: Querying the service -> [URI: ({})]", restRequestUrl);

		var errorResponse = restTemplate.getForObject(restRequestUrl, ControllerErrorResponse.class);

		log.info("[Test-REST-HTTP]: Service replied back. -> [Response: ({})]", errorResponse);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatus());
		Assertions.assertEquals(HttpStatus.NOT_FOUND.name(), errorResponse.getErrorCode());
	}
}
