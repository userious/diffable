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
package com.google.diffable.scripts;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.google.diffable.config.MessageProvider;
import com.google.diffable.exceptions.StackTracePrinter;
import com.google.diffable.utils.IOUtils;
import com.google.inject.Inject;

/**
 * This class defines the wrapper for the Javascript delta bootstrap
 * 
 * @author joshua Harrison
 */
public class DeltaBootstrapWrapper {
	
	@Inject
	private StackTracePrinter printer;
	
	@Inject
	private MessageProvider provider;
	
	@Inject(optional=true)
	private Logger logger =
		Logger.getLogger(DeltaBootstrapWrapper.class);
	
	private String resourceString = null;
	
	public DeltaBootstrapWrapper() {
		InputStream in =
			this.getClass().getResourceAsStream(
				"/com/google/diffable/scripts/DeltaBootstrap.js");
		try {
			int currentChar;
			StringBuffer content = new StringBuffer();
			while ((currentChar = in.read()) != -1) {
				content.append(Character.toChars(currentChar));
			}
			resourceString = content.toString();
		} catch (Exception exc) {
			provider.error(
				logger, "bootstrap.resourceerror",
	            "/com/google/diffable/scripts/DeltaBootstrap.js");
			printer.print(exc);
		}finally{
			IOUtils.close(in);
		}
	}
	
	public String render(String resourceId, String diffContent) {
		return resourceString
			.replace("{{DJS_RESOURCE_IDENTIFIER}}", resourceId)
			.replace("{{DJS_DIFF_CONTENT}}", diffContent);
	}
}