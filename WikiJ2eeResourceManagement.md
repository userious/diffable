# Resource Management #

The Diffable for J2EE library takes a fairly straightforward approach to managing resources contained by [designated resource folders](WikiJ2eeDiffableConfigure#Resource_Folders.md).  Simply, the library uses MD5 hashes of a resource's absolute path to identify the resource, and MD5 hashes of the content to track versions.

## Manifest ##
In order to keep track of which resources are being managed, the library creates a 'diffable.manifest' properties file at the root of the resource store.  This file contains mappings between a resource's absolute path and the hash identifying that resource.  It is used to maintain some state between runs of the application.

## Resource Folders ##
Each resource's unique hash is then used to create a folder of the same name.  This folder is used for storing old versions of the resource along with deltas.  The last modified time of this folder is kept in sync with that of the actual resource, allowing the library to detect changes in a resource even if it is not currently running.

## Version Files ##
As changes to a given resource are detected, the new version of the resource is copied over to the corresponding resource folder.  The file created to store the version uses the MD5 hash of the resource's contents as its name and ends with '.version'.

## Deltas ##
Deltas are also stored in the resource's corresponding folder. If the contents of version 1 of a resource hash to 'aa,' and version 2 hashs to 'bb,' the delta is stored as 'aa\_bb.diff'.