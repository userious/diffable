# Contents #
  1. [Importing](WikiJ2eeDiffableConfigure#Importing.md)
  1. [Configuring web.xml](WikiJ2eeDiffableConfigure#Configuring_web.xml.md)
  1. [Configuration Options](DiffableConfigProperties.md)

## Importing ##

To use Diffable in a J2EE web application, simply add the diffable-0.1.jar to the web application's WEB-INF/lib directory along with the other necessary third party jars.  The Diffable library provides the following features:
  * Custom tags for referencing resources that should be served using Diffable.
  * Automatic management of resource versions and the ability to generate deltas between versions.
  * Ability to serve wrapped Diffable resources. (Currently only Javascript)
  * Ability to integrate implementations of interfaces without modifying the source.

## Configuring web.xml ##

The Diffable library requires that certain modifications be made to the application's web.xml file.

### Resource Folders ###
First, a context parameter, "ResourceFolders," must be created.  This parameter contains the locations of folders containing resources to be managed by Diffable.  The following code snippet illustrates:
```
  <context-param>
      <description>
          Location of resources to be managed by Diffable.
      </description>
      <param-name>ResourceFolders</param-name>
      <param-value>WEB-INF/javascript</param-value>
  </context-param>
```
In this case, when Diffable starts up, it will check the web application's WEB-INF/javascript folder for resources to manage.  It will also periodically check this folder for changes to those resources and generate deltas when need be.

Multiple paths, separated by commas, can be provided via the param-value:
```
  <param-value>WEB-INF/javascript, WEB-INF/css</param-value>
```

Also, absolute paths residing outside of the web application can be provided by using a preceding slash:
```
  <param-value>/home/apache/resources/javascript</param-value>
```

### Application Listener ###
Second, the DiffableListener must be added as a listener servlet so that it can perform initialization functions when the application starts.  This can be accomplished with the following changes to web.xml:
```
  <listener>
    <display-name>DiffableListener</display-name>
    <listener-class>
      com.google.diffable.listeners.DiffableListener
    </listener-class>
  </listener>
```
Upon start-up, the DiffableListener will initialize a resource manager and monitor the resources residing in the designated resource folders.  It will also load any configuration options provided by DiffableConfigProperties.

### Diffable Configuration Properties ###
Because Diffable makes use of the Google Guice dependency injection framework, many of the options used at run time can be overwritten by defining a properties file in the web.xml with a DiffableConfigProperties context-param. The use of a diffable configuration file is optional, and if not provided, sensible defaults are used for all options.  To define a properties file, make the following addition to the web.xml.
```
  <context-param>
      <description>
          Location of properties provided to Guice for loading information and
          implementation overrides for the current resource manager strategy.
      </description>
      <param-name>DiffableConfigProperties</param-name>
      <param-value>WEB-INF/diffable.properties</param-value>
  </context-param>
```
As with the ResourceFolders parameter, the value of this context param can be either relative or absolute depending on whether the path is preceded by a slash.  Unlike the resource folders, there can be only one path defined.  The options available are enumerated on the DiffableConfigProperties page.

### Servlet Prefix Parameter ###
_**Note: The servlet prefix parameter change is checked in under head.**_

When a managed resource is addressed via the DiffableResourceTag, a prefix is needed so that the request can be addressed to the DiffableServlet.  This prefix is provided via the application's web.xml file using the ServletPrefix context parameter.  The provided value must be the absolute path to the DiffableServlet.  For instance, if the application is deployed as "DeltaJsTest" and the DiffableServlet is mapped to "/diffable/`*`" then the context parameter definition would look like:
```
  <context-param>
  	<description>
  		The prefix to use when addressing managed resource requests to the
  		Diffable servlet.
  	</description>
  	<param-name>ServletPrefix</param-name>
  	<param-value>/DeltaJSTest/diffable</param-value>
  </context-param>
```

### Diffable Servlet ###
Lastly, the DiffableServlet must be mapped to handle requests for resources.  This is done using a classic servlet tag in the web.xml:
```
  <servlet>
      <description>
          Serves managed diffable resources.
      </description>
      <servlet-name>DiffableServlet</servlet-name>
      <servlet-class>
          com.google.diffable.servlets.DiffableServlet
      </servlet-class>
  </servlet>
 
  <servlet-mapping>
      <servlet-name>DiffableServlet</servlet-name>
      <url-pattern>/diffable/*</url-pattern>
  </servlet-mapping>
```
Of course, the mapping can be modified to whatever URL is desired.