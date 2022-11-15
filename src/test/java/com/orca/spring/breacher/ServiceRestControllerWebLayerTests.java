package com.orca.spring.breacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsConstants;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceTestsSuiteBase;
import com.orca.spring.breacher.environment.CloudEnvironmentTestSampleEngine;
import com.orca.spring.breacher.topology.VirtualMachineAccessTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants.*;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeAccessibleAsset;
import static com.orca.spring.breacher.utils.ServiceTestAccessTableUtils.randomizeVmIdentifier;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
class ServiceRestControllerWebLayerTests extends AttackSurfaceServiceTestsSuiteBase
{
	static
	{
		CloudEnvironmentTestSampleEngine.Instance.load(AttackSurfaceServiceTestsConstants.CloudEnvironmentTestSample3);
	}

	// region Dependencies

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VirtualMachineAccessTable accessTable;

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	// endregion

	@Test
	void testAnalyzeMachineAttackSurface_sanityHttpRequestResponse() throws Exception
	{
		var accessibleAsset = randomizeAccessibleAsset(accessTable);
		var expectedAttackSurface = accessibleAsset.getValue().stream().map(vm -> vm.getIdentifier().id()).collect(Collectors.toList());
		var endpointUrl = String.format("%s%s", RestControllerServiceApiPath, RestControllerEndpointMappingAttack);
		var restUrlTemplate = String.format("%s?%s=%s", endpointUrl, RestControllerEndpointAttackQueryParam, accessibleAsset.getKey().id());

		log.info("[Test-Web-Layer]: Querying the service (Mock MVC) -> [URI-Template: ({})]", restUrlTemplate);

		var endpointResponse = mockMvc.perform(get(restUrlTemplate))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		log.info("[Test-Web-Layer]: Service replied back (Mock MVC) -> [Response: ({})]", endpointResponse);

		var actualAttackSurface = jacksonObjectMapper.readValue(endpointResponse.getResponse().getContentAsString(), String[].class);

		Assertions.assertIterableEquals(expectedAttackSurface, Arrays.asList(actualAttackSurface));
	}

	@Test
	void testAnalyzeMachineAttackSurface_errorHttpRequestResponse() throws Exception
	{
		var invalidAssetIdentifier = randomizeVmIdentifier();
		var endpointUrl = String.format("%s%s", RestControllerServiceApiPath, RestControllerEndpointMappingAttack);
		var restUrlTemplate = String.format("%s?%s=%s", endpointUrl, RestControllerEndpointAttackQueryParam, invalidAssetIdentifier);

		log.info("[Test-Web-Layer]: Querying the service (Mock MVC) -> [URI-Template: ({})]", restUrlTemplate);

		mockMvc.perform(get(restUrlTemplate)).andDo(print()).andExpect(status().isNotFound());
	}
}
