_**Please be sure to [read the blog](http://googlediffable.blogspot.com/) for project news and updates!**_

# Introduction #
_**Diffable**_ is a method for reducing page load latency by transmitting differential encodings of static files.  It works by sending deltas between versions of files that reside in a browser's cache.  In web applications, files such as Javascript, HTML, and CSS are often updated every week or more, but change very little between versions.  This method significantly reduces the bandwidth and transmission time for these files and is implementable on all legacy browsers that support Javascript.

This project aims to capture implementations of this technique for different web application frameworks.

[Presentation Slides From Velocity 2010 Conference](http://docs.google.com/present/view?id=dhp4jpqq_58hmf8zxdk)
<sub>Edit: The bar graph on slide 13 has been updated to start at 0 on the Y-axis. Originally, it started at 3500.</sub>

### Implementations ###
As Diffable is implemented for different frameworks, they will be listed here.
  * [Diffable for J2EE](WikiJ2eeDiffable.md)
  * [Diffable for Node.js](https://github.com/plotnikoff/connect-diffable) for the [Connect Framework](https://github.com/senchalabs/connect/)

## Details ##
The Diffable technique for serving static content relies on the browser's cache (although there are plans for using HTML5 local storage as well) to store static content and "patching," cached versions of resources from within the client as they change.

### Caching ###
Many modern sites contain large amounts of Javascript and CSS which can contribute significantly to page load times as they are downloaded from the server.  To deal with this, browsers cache the content between visits to the page, allowing for these resources to be loaded from the local machine.  However, if the resource has changed at all the cache is invalid and the entire resource must be loaded again.  This is often an inefficient process as small changes to a given resource incur large costs in page load time.  Often times this "breaking," of the cache is made explicit by changing the URL referring to a given resource, assuring that the user has the latest version.  For instance, every time a new Javascript binary is pushed for http://maps.google.com, the user must re-download the entire ~100k file.

### Deltas ###
A delta is a representation of the changes between two different versions of a given resource.  Often, changes to website resources are incremental and occur frequently.  The delta between two versions of a given resource is often a tiny fraction of the size of the original resource.

### Technique ###
The Diffable technique works by converting a given managed resource into a Javascript string and embedding the string within a chunk of bootstrap code that is responsible for integrating the resource into the page.  The URL for the resource is kept constant and the resource is served with aggressive cache headers.  The bootstrap code contains the version of the resource embedded within, along with methods for requesting deltas and performing patching.  This modified resource is then referenced, via a standard script import, from a container page which has cache-headers set to prevent caching.  The container page also has the most current version of the given resource stored in the global Javascript context.  When the page is loaded the bootstrap code compares the version of the embedded resource against the most current version of the resource as defined by the container page, and if they do not match, requests a delta from the server, which it then applies to the embedded version before integrating it into the page.  If the versions do match, the embedded version is integrated into the page immediately.

The diagram below provides a detailed description of the Diffable method:

![http://farm5.static.flickr.com/4038/4602395857_188246580d_b.jpg](http://farm5.static.flickr.com/4038/4602395857_188246580d_b.jpg)