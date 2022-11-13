package com.orca.spring.breacher.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orca.spring.breacher.model.CloudEnvironment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class AttackSurfaceServiceSettings
{
    @AllArgsConstructor
    private enum AttackSurfaceServiceArguments
    {
        InputFilePath(0),
        ValidArgumentsCount(1);

        public Integer value;
    }

    // region Fields

    @Getter
    private CloudEnvironment cloudEnvironment;

    // endregion

    @Autowired
    public AttackSurfaceServiceSettings(ApplicationArguments applicationArguments, ObjectMapper jacksonObjectMapper) throws IOException
    {
        validateCommandLineArguments(applicationArguments);
        loadCloudEnvironmentSettings(applicationArguments, jacksonObjectMapper);
    }

    private void validateCommandLineArguments(ApplicationArguments applicationArguments)
    {
        var commandLineArguments = applicationArguments.getSourceArgs();

        if (commandLineArguments.length != AttackSurfaceServiceArguments.ValidArgumentsCount.value)
        {
            throw new IllegalArgumentException();
        }
    }

    public void loadCloudEnvironmentSettings(ApplicationArguments applicationArguments, ObjectMapper jacksonObjectMapper) throws IOException
    {
        var commandLineArguments = applicationArguments.getSourceArgs();
        var cloudEnvironmentSettingsFilePath = commandLineArguments[AttackSurfaceServiceArguments.InputFilePath.value];
        var cloudEnvironmentSettingsFile = new File(cloudEnvironmentSettingsFilePath);

        this.cloudEnvironment = jacksonObjectMapper.readValue(cloudEnvironmentSettingsFile, CloudEnvironment.class);
    }
}
