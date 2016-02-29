Once Diffable has been configured, using it is simply a matter of replacing normal script tags with the custom tag provided by diffable.  To use the tag from within a JSP page, the tag library must be imported.  This can be done with the following page directive:
```
  <%@ taglib prefix="diff" uri="http://www.google.com/j2ee/diffable" %>
```

Once imported, script tags can be replaced with diff resource tags.  For instance, the following JSP page:
```
<%@ page language="java" contentType="text/html; charset-ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<html>
  <head>
    <script type="text/javascript" src="javascript/somescript.js"></script>
  </head>
  <body>
  ...
  </body>
</html>
```

can be changed to:

```
<%@ page language="java" contentType="text/html; charset-ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="diff" uri="http://www.google.com/j2ee/diffable" %>
<html>
  <head>
    <diff:resource servletPrefix="/WebAppContext/servletpath/" resource="somescript.js"/>
  </head>
  <body>
  ...
  </body>
</html>
```

In the above example, the two attributes to note are:

  * `servletPrefix`
> _**Note: The servletPrefix attribute is no longer included under head and has been replaced with the ServletPrefix context parameter in the application's web.xml.**_

> The servlet prefix is prepended to all requests made from the client.  It basically must be a fully qualified path to the diffable servlet. If my web application is mapped to 'http://localhost/someapp' and the Diffable servlet is mapped to '/diffable/`*`' then the servlet prefix would be '/someapp/diffable'.  Do not include a trailing slash, as one will be added when making the resource request.

  * `resource`
> This value should be a path relative to the resource folder where the file is located. If you are attempting to load the Javascript file '/WEB-INF/javascript/loader/loader.js' and 'WEB-INF/javascript' has been [provided as a resource folder in the web.xml](WikiJ2eeDiffableConfigure#Resource_Folders.md), then the resource attribute would have the value 'loader/loader.js'.

The custom tag will emit the Javascript necessary to load, patch if necessary and execute the correct version of the resource.