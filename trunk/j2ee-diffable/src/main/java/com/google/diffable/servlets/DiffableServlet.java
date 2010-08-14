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
package com.google.diffable.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.diffable.config.MessageProvider;
import com.google.diffable.data.ResourceManager;
import com.google.diffable.data.ResourceRequest;
import com.google.diffable.diff.JSONHelper;
import com.google.diffable.exceptions.StackTracePrinter;
import com.google.diffable.scripts.DeltaBootstrapWrapper;
import com.google.diffable.scripts.JsDictionaryBootstrapWrapper;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This class defines the servlet which will handle the request for the diffable resources.
 *  
 * @author joshua Harrison
 */
public class DiffableServlet extends HttpServlet {

	@Inject
	private StackTracePrinter printer;
	
	@Inject
	private MessageProvider provider;
	
	@Inject(optional=true)
	private Logger logger = Logger.getLogger(DiffableServlet.class);
	
	@Inject
	private ResourceManager mgr;
	
	@Inject
	private JsDictionaryBootstrapWrapper jsDictWrapper;
	
	@Inject
	private DeltaBootstrapWrapper jsDiffWrapper;
	
	private Injector inj;
	
	private Calendar jan2000 = Calendar.getInstance();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		inj = (Injector)
		    config.getServletContext().getAttribute(
		    	"diffable.DiffableGuiceInjector");
		inj.getMembersInjector(DiffableServlet.class).injectMembers(this);
		
		// Set the 2010 calendar.
		jan2000.set(2000, 1, 1);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Strip the servlet path information, leaving just the resource 
		// name, which includes all information after the servlet path.
		String basePath = req.getContextPath() + req.getServletPath() + "/";
		String requestString = req.getRequestURI().replace(basePath, "");
		provider.debug(logger, "servlet.resourcerequest", requestString);
		try {
			ResourceRequest request = inj.getInstance(ResourceRequest.class); 
			request.setRequest(requestString);
			mgr.getResource(request);
			if (request.getResponse() != null) {
				resp.setStatus(200);
				Calendar twoYearsFromNow = Calendar.getInstance();
				twoYearsFromNow.set(Calendar.YEAR, 
						            twoYearsFromNow.get(Calendar.YEAR) + 2);
				// All responses are denoted as last being modified on Jan 1,
				// 2000 to allow for very agressive caching.
				resp.setHeader("Last-Modified",
					new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",
							             Locale.US).format(
						jan2000.getTime()));
				resp.setHeader("Cache-Control", "public, max-age=63072000");
				resp.setHeader("Expires", 
					new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",
							             Locale.US).format(
						twoYearsFromNow.getTime()));
				if (request.isDiff()) {
					String response = jsDiffWrapper.render(
						request.getResourceHash(), request.getResponse());
					resp.setContentLength(response.length());
					resp.getWriter().print(response);
				} else {
					String response = jsDictWrapper.render(
						request.getResourceHash(),
						JSONHelper.quote(request.getResponse()),
						request.getNewVersionHash(), basePath);
					resp.setContentLength(response.length());
					resp.getWriter().print(response);
				}
			} else {
				resp.setStatus(404);
			}
		} catch (Exception exc) {
			//provider.error(logger, "servlet.cantserverequest",
			//		       req.getRequestURI());
			printer.print(exc);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected long getLastModified(HttpServletRequest req) {
		// Last modified times are also returned as Jan 1, 2000 to match with
		// the last-modified cache response header.
		return jan2000.getTimeInMillis();
	}
}
