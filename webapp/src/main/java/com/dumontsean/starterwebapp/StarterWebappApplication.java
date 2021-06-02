package com.dumontsean.starterwebapp;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

@PropertySources({
        @PropertySource(name = "globalProperties", value = {
                "classpath:starter-global.properties"
        },
                encoding = "UTF-8"),
        @PropertySource(name = "globalProperties", value = {
                "file:${catalina.home}/shared/classes/starter-global.properties"
        },
                ignoreResourceNotFound = true,
                encoding = "UTF-8")
})
@SpringBootApplication(scanBasePackages = "com.dumontsean")
public class StarterWebappApplication extends SpringBootServletInitializer {

    // This allows us to start as a WAR or an executable Application
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return configureApplication(application);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder application) {
        return application.sources(StarterWebappApplication.class).bannerMode(Banner.Mode.OFF);
    }

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Setting Session Timeout and Tracking Type
        servletContext.setSessionTimeout(30);
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        super.onStartup(servletContext);
    }
}
