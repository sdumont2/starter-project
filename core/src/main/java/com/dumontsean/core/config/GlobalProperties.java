package com.dumontsean.core.config;

import com.dumontsean.core.AbstractPropertiesObject;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;

@Component("globalProperties")
public class GlobalProperties extends AbstractPropertiesObject {

    @PostConstruct
    public void init() {
        String propFilePath = "file:${catalina.home}/shared/classes/starter-global.properties";
        String altFilePath = "classpath:starter-global.properties";

        PropertiesConfiguration globalProps;
        try {
            globalProps = createPropertiesConfig(propFilePath, altFilePath);
        } catch (ConfigurationException | FileNotFoundException e) {
            throw new RuntimeException("Could not locate configuration file. ", e);
        }

        setConfiguration(globalProps);
    }

    private PropertiesConfiguration createPropertiesConfig(String propFilePath, String altFilePath) throws ConfigurationException, FileNotFoundException {

        File propFile = ResourceUtils.getFile(propFilePath);
        File altPropsFile = ResourceUtils.getFile(altFilePath);
        PropertiesConfiguration propsConfig;
        if (propFile.exists()) {
            propsConfig = getPropertiesConfigForFile(propFile);
        } else if (altPropsFile.exists()) {
            propsConfig = getPropertiesConfigForFile(altPropsFile);
        } else {
            throw new ConfigurationException("Could not locate property files " + propFilePath + " or " + altFilePath);
        }

        return propsConfig;
    }


}
