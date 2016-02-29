# Introduction #

The implementation of Diffable for J2EE is still very much in **BETA**.

Features:
  * Automatically tracks versions and generates deltas between managed resources.
  * Can serve Javascript resources using Diffable.
  * Provides custom tags for integrating into JSP pages.

Further Information:
  * [Configuring Diffable for J2EE](WikiJ2eeDiffableConfigure.md)
  * [Using Diffable for J2EE](WikiJ2eeUsingDiffable.md)
  * [Resource Management in Diffable for J2EE](WikiJ2eeResourceManagement.md)

## Building ##

The J2EE implementation uses Maven for package management and build tasks. From the README:

```
To build Diffable from source requires Apache Maven 2.1 or better:
http://maven.apache.org/

It also requires Java EE 6 or better:
http://java.sun.com/javaee/downloads/index.jsp

The following 3rd party libraries must exist on the classpath when building
from source and must also be linked from web projects that use diffable-0.1.jar:

Google Guice 2.0 or better:
 - guice-2.0.jar : http://code.google.com/p/google-guice/

AOP Alliance 1.0 or better:
 - aopalliance.jar : http://sourceforge.net/projects/aopalliance/

Apache Commons IO 1.4 or better:
 - commons-io-1.4.jar : http://commons.apache.org/io/
 
Log4J 1.2 or better:
 - log4j-1.2.16.jar : http://logging.apache.org/log4j/1.2/
 
The following 3rd party apps are not required when using Diffable but are
required when building from source:

EasyMock 3.0 or better:
 - easymock-3.0.jar : http://easymock.org/

Code Generation Library 2.2 or better:
 - cglib-nodep-2.2.jar : http://cglib.sourceforge.net/

Objenesis 1.2 or better:
 - objenesis-1.2.jar : http://code.google.com/p/objenesis/
 
Once the class path has been constructed correctly, simply cd to the
j2ee-diffable directory and run:

mvn package
```

As mentioned above, Maven takes care of downloading the required dependencies when building from scratch.  When using Diffable from within a J2EE application though, it is necessary for the user to place the required libraries in the application's build path.