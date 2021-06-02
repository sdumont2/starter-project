package com.dumontsean.core;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class AbstractPropertiesObject {

    public final Logger log = LogManager.getLogger(getClass());

    private PropertiesConfiguration config;
    private List<String> orderList;

    public PropertiesConfiguration getConfiguration() {
        return config;
    }

    public void setConfiguration(PropertiesConfiguration config) {
        this.config = config;
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public void setProp(String key, Object value) {
        if (this.config.containsKey(key) && !key.isEmpty())
            this.config.setProperty(key, value);
    }

    public String getProp(String key) {
        Object prop = this.config.getProperty(key);
        return null != prop ? this.config.getProperty(key).toString().trim().replace("\\", "\\\\") : null;
    }

    public boolean getBooleanProp(String key){
        String val = getProp(key);
        if (null != val) {
            return Boolean.parseBoolean(val);
        } else {
            return false;
        }
    }

    public String getStringProp(String key){
        String val = getProp(key);
        return val != null ? val : "";
    }

    public int getIntProp(String key){
        String val = getProp(key);
        if (null != val) {
            return Integer.parseInt(val);
        } else {
            return 0;
        }
    }

    protected void init(String propFileLocation, String altPropFile,List<String> orderList) {
        try {
            File propFile = ResourceUtils.getFile(propFileLocation);

            if (!propFile.exists()) {
                if (null != altPropFile && !altPropFile.isEmpty()) {
                    propFile = ResourceUtils.getFile(altPropFile);
                } else {
                    throw new ConfigurationException("Could not locate property files " + propFileLocation + " or " + altPropFile);
                }
            }

            config = getPropertiesConfigForFile(propFile);
            this.orderList = orderList;
        } catch (ConfigurationException | IOException e) {
            System.out.println("Error loading property file " + altPropFile + ": " + e.getMessage());
        }
    }

    public Map<String, String> getPropMap() {
        Map<String, String> props = new HashMap<>();
        for (String k : orderList) {
            props.put(k, getProp(k));
        }
        return props;
    }

    public Iterator<String> getPropKeys(){
        return orderList.iterator();
    }

    protected static PropertiesConfiguration getPropertiesConfigForFile(File file) throws ConfigurationException {
        Parameters params = new Parameters();
        ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased()
                                .setFile(file));
        // enable auto save mode
        builder.setAutoSave(true);
        PropertiesConfiguration configuration = (PropertiesConfiguration) builder.getConfiguration();
        configuration.setThrowExceptionOnMissing(false);
        return configuration;
    }
}
