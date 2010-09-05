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
package com.google.diffable.tags;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DiffableResourceTag extends TagSupport {
	// Stores the resource folders to check for resources.
	private static List<File> resourceFolders;
	public static void setFolder(List<File> resourceFolders) {
		DiffableResourceTag.resourceFolders = resourceFolders;
	}
	
	// Cache for storing the most recent version of a managed resource. Used by
	// the tag to insert the current version of a resource into the page
	// context.
	private static Map<File, String> currentVersions = 
		new HashMap<File, String>();
	public static void setCurrentVersion(File resource, String currentVersion) {
		DiffableResourceTag.currentVersions.put(resource, currentVersion);
	}
	
    private String servletPrefix;	
	private String resource;
	private String type;
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public void setServletPrefix(String servletPrefix) {
		this.servletPrefix = servletPrefix;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	private String hashPath(File resource) 
	throws Exception {
		byte[] filePathBytes = resource.getAbsolutePath().getBytes();
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(filePathBytes, 0, filePathBytes.length);
		return new BigInteger(1, md5.digest()).toString(16);
	}
	
	@Override
	public int doStartTag() throws JspException {
		try {
			// Set the response of the page containing this resource to be
			// uncacheable.
			((HttpServletResponse)pageContext.getResponse())
				.setHeader("Cache-Control", "private, max-age=0");
		    ((HttpServletResponse)pageContext.getResponse())
				.setHeader("Expires", "-1");
		
			File found = null;
			if (!(new File(resource).exists())) {
				for (File folder : DiffableResourceTag.resourceFolders) {
					File test =
						new File(folder.getAbsolutePath() +
								 File.separator + resource);
					if (test.exists()) {
						found = test;
						break;
					}
				}
			} else {
				found = new File(resource);
			}
			if (found == null) {
				throw new JspException("Cannot find resource " +
				    resource + " referenced in DiffableTag");
			} else if(!DiffableResourceTag.currentVersions.containsKey(found)) {
				throw new JspException(
					"Cannot find current version of resource '" +
					found.getAbsolutePath() + ".'");
			} else {
				String resourceHash = hashPath(found);
				pageContext.getOut().println("<script type='text/javascript'>");
				pageContext.getOut().println(
					"if(!window['deltajs']) { window['deltajs'] = {}; }");
				pageContext.getOut().println(
					"window['deltajs']['" + resourceHash + "']={};");
				pageContext.getOut().println(
					"window['deltajs']['" + resourceHash + "']" +
					"['cv'] = '" +
					DiffableResourceTag.currentVersions.get(found) + "';");
				pageContext.getOut().println("</script>");
				pageContext.getOut().println(
				    "<script type='text/javascript' src='" +
					servletPrefix + "/" + resourceHash + "'></script>");
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return SKIP_BODY;
	}
}
