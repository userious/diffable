/**
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.diffable.data;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.diffable.tags.DiffableResourceTag;

/**
 * This class defines the diffable context which contains
 * data about the current file versions, the resource folders, ...  
 * 
 * @author ibrahim Chaehoi
 */
public class DiffableContext implements Serializable {

	// Stores the resource folders to check for resources.
	private List<File> resourceFolders = null;
	
	// Stores the servlet prefix set in the diffable servlet initialzation
	// parameters.
	private String servletPrefix = null;
	
	// Cache for storing the most recent version of a managed resource. Used by
	// the tag to insert the current version of a resource into the page
	// context.
	private Map<File, String> currentVersions = 
		new HashMap<File, String>();

	public List<File> getResourceFolders() {
		return resourceFolders;
	}

	public String getServletPrefix() {
		return servletPrefix;
	}

	public Map<File, String> getCurrentVersions() {
		return currentVersions;
	}

	public void setFolder(List<File> resourceFolders) {
		this.resourceFolders = resourceFolders;
	}
	
	public void setServletPrefix(String servletPrefix) {
		this.servletPrefix = servletPrefix;
	}
	
	public void setCurrentVersion(File resource, String currentVersion) {
		currentVersions.put(resource, currentVersion);
	}
}
