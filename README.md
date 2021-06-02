# Starter Webapp

This is just a starter web application project for use in creating other web applications. Meant to be as a base, utilizing some basic examples of CRUD operations. Still needs to be cleaned up and tuned better, but this was originally made to see if Hibernate OGM and Hibernate ORM could be tied in to Spring Data JPA in a way that allows for setting a few properties in a "global properties" file, and allow for dynamic support for nearly any Database without needing to create multiple DAOs or other structures.
  
## Additional Modifications

 - Modified Spring Boot Starter For Dynamic configuration loading regardless of whether Spring Boot or Tomcat is being used.
 - Modified Spring Data JPA for use with modified Hibernate. Forces Hibernate OGM to use the Criteria API.


## Eventual Other Changes

 - Setting Up Spring Security and having actual user/group support.
 - Potentially Switching off of thymeleaf as the rendering engine, and trying to use Angular or some other front end system instead.
 - Setting up dynamic Rest API Creation as well as dynamic Rest API Doc creation
 - Creating a plugin/addon architecture that doesn't utilize/force osgi
 - If/When Hibernate updates and supports Criteria API for OGM, will need to switch to that. Current Forcing of the Criteria API "works", in the most basic sense, but in depth testing hasn't been done.
 - Project Cleanup and Restructuring. This project will be ever changing and likely will require a better organizational structure (it already does), so this "TODO" will be around for awhile.
 - Actual Error Page Handling
 - Internationalization
 - Multi T support

## License Information

Licensed under the Apache License, Version 2.0 


