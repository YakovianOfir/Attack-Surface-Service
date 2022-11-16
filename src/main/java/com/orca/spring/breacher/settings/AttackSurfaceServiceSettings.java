package com.orca.spring.breacher.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.definitions.AttackSurfaceServiceConstants;
import com.orca.spring.breacher.environment.CloudEnvironmentProviderFactory;
import com.orca.spring.breacher.environment.CloudEnvironmentProviderType;
import com.orca.spring.breacher.environment.ICloudEnvironmentProvider;
import com.orca.spring.breacher.exception.InvalidCommandLineException;
import com.orca.spring.breacher.model.CloudEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AttackSurfaceServiceSettings
{
    // region Fields

    private ICloudEnvironmentProvider environmentProvider;

    // endregion

    // region Constants

    private static final Integer ServiceCliMaximumArgumentsCount = 1;
    private static final Integer ServiceCliArgumentInputFilePathIndex = 0;

    // endregion

    @Autowired
    public AttackSurfaceServiceSettings(ApplicationArguments applicationArguments, ObjectMapper jacksonObjectMapper) throws IOException
    {
        try
        {
            log.info("[Service-Settings] Loading provided Cloud Environment settings of tenant.");
            loadAttackSurfaceServiceSettings(applicationArguments, jacksonObjectMapper);
        }
        catch (InvalidCommandLineException e)
        {
            log.error("[Service-Settings] Invalid command line. -> ({}).", e);
            printServiceUsage(e);
        }
    }

    private void loadAttackSurfaceServiceSettings(ApplicationArguments applicationArguments, ObjectMapper jacksonObjectMapper) throws InvalidCommandLineException, IOException
    {
        var providerType = diagnoseCloudEnvironmentProviderType(applicationArguments);
        log.info("[Service-Settings] Detected cloud environment provider type. ({})", providerType);

        var providerArgument = diagnoseCloudEnvironmentProviderArgument(applicationArguments, providerType);
        log.info("[Service-Settings] Detected cloud environment provider argument. ({})", providerArgument);

        this.environmentProvider = CloudEnvironmentProviderFactory.create(providerType, providerArgument, jacksonObjectMapper);
        log.info("[Service-Settings] Loaded settings. (OK) ({})", environmentProvider);
    }

    private CloudEnvironmentProviderType diagnoseCloudEnvironmentProviderType(ApplicationArguments applicationArguments) throws InvalidCommandLineException
    {
        if (applicationArguments == null)
        {
            return CloudEnvironmentProviderType.SystemPropertyProvider;
        }

        var sourceArguments = applicationArguments.getSourceArgs();

        if (sourceArguments.length > ServiceCliMaximumArgumentsCount)
        {
            throw new InvalidCommandLineException("Provided too many arguments.");
        }

        if (sourceArguments.length == ServiceCliMaximumArgumentsCount)
        {
            return CloudEnvironmentProviderType.FileSystemProvider;
        }

        if (sourceArguments.length == 0)
        {
            return CloudEnvironmentProviderType.SystemPropertyProvider;
        }

        throw new IllegalArgumentException();
    }

    private String diagnoseCloudEnvironmentProviderArgument(ApplicationArguments applicationArguments, CloudEnvironmentProviderType providerType)
    {
        switch (providerType)
        {
            case FileSystemProvider ->
            {
                return applicationArguments.getSourceArgs()[ServiceCliArgumentInputFilePathIndex];
            }

            case SystemPropertyProvider ->
            {
                return AttackSurfaceServiceConstants.ServiceCloudEnvironmentSystemPropertyName;
            }

            default ->
            {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void printServiceUsage(Throwable e)
    {
        System.err.println(String.format("\nError loading service. Fault -> \n[%s]\n", e.toString()));
        System.err.println("\n[Usage]: sudo java -jar <absolute-path-to-breacher.jar> <absolute-path-to-valid-cloud-environment.json>\n");
    }

    public CloudEnvironment getCloudEnvironment()
    {
        return environmentProvider.get();
    }
}