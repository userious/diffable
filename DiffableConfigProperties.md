# Configuration Properties for Diffable for J2EE #

The Diffable library allows many of its configuration options to be overridden via a [Java properties file](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Properties.html) which must be referenced by a context parameter from within the [web.xml](WikiJ2eeDiffableConfigure#Diffable_Configuration_Properties.md) file. The following list of properties are available:

  * `ResourceStorePath=/some/path`
> The resource store path defines the folder Diffable will use to store versions of a given managed resource along with deltas between those versions.  If this is not explicitly defined, the artifacts will be stored in a '.diffable' folder under the root of the web application.  If a managed resource is removed from a resource folder then the artifacts corresponding to that resource will be deleted the next time the application starts.

  * `KeepResourcesInMemory=[true/false]`
> If true, the contents of the most recent version of managed resources will be kept in memory.  Although incurring a larger memory footprint, it speeds up the serving of resources by avoiding file reads.  This currently does not apply do deltas, although this may change in the future.  It defaults to true.

  * `MessageBundlePath=/some/path`
> This property can be used to define a different directory for fetching messages.  Currently, it defaults to the path to the bundled messages: 'com/google/diffable/bundles/'. The folder defined by this path must contain three files: DebugMessages.properties, ErrorMessages.properties and InfoMessages.properties.  Intended to ease internationalization, the folder defined may contain language specific sub-folders.

  * `BlockSize=[Integer]`
> The block size determines the fingerprint window used by the [VCDiff](http://code.google.com/p/open-vcdiff/) implementation. The number is basically the minimum length of a run string that can be detected when detecting parts of a document that haven't changed.  The larger it is, the faster the algorithm runs, but the fewer COPY commands will be used.  It defaults to 20.

  * `PrimeBase=[Integer]`
> The prime base influences the rolling hash algorithm used to calculate the fingerprint of a text block in [VCDiff](http://code.google.com/p/open-vcdiff/).  This number should be prime.  More information can be found in discussions of the [Rabin-Karp string search algorithm](http://en.wikipedia.org/wiki/Rabin-Karp_string_search_algorithm) on wikipedia.  The default value is 257.

  * `PrimeMod=[Integer]`
> The prime mod influences the rolling hash algorithm used to calculate the fingerprint of a text block in [VCDiff](http://code.google.com/p/open-vcdiff/).  More information can be found in discussions of the [Rabin-Karp string search algorithm](http://en.wikipedia.org/wiki/Rabin-Karp_string_search_algorithm) on wikipedia.  This should be a very large prime number.  The large it is, the lower the chance of two non-identical text blocks having the same hash value.  It defaults to 1000000007.

  * `ResourceMonitorInterval=[Integer]`
> This property controls how often Diffable will check for modifications to managed resources.  It is defined in milliseconds and defaults to 2000.  The monitor thread will sleep for this long before crawling all the provided resource folders to check for changes.

  * `PrintStackTraces=[true/false]`
> If set to true, handled exceptions will have their stack traces printed to standard out.  Otherwise, only log messages will be produced.