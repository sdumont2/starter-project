package com.dumontsean.starterwebapp.controllers;

import com.dumontsean.core.config.GlobalProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public abstract class AbstractController {
    public final Logger log = LogManager.getLogger(getClass());

    protected static final String ERR_MSG_ATTR = "errorMessage";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GlobalProperties globalProperties;

    private static final Locale currentLocale = LocaleContextHolder.getLocale();

    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    protected GlobalProperties getGlobalProperties() {
        return this.globalProperties;
    }
}
