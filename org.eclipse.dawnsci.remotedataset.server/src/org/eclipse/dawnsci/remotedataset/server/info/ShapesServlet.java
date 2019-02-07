/*
 * Copyright (c) 2019 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.dawnsci.remotedataset.server.info;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.dawnsci.analysis.api.io.IDataHolder;
import org.eclipse.dawnsci.analysis.api.persistence.IMarshallerService;
import org.eclipse.dawnsci.remotedataset.ServiceHolder;
import org.eclipse.dawnsci.remotedataset.XMLMarshallerService;
import org.eclipse.dawnsci.remotedataset.server.utils.DataServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ShapesServlet extends HttpServlet {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 5069190236143524266L;
	
	private static Logger logger = LoggerFactory.getLogger(ShapesServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doHandle(req, resp);
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doHandle(req, resp);
    }
    
	/**
	 * Remember with servlets each of these is done on a thread from the
	 * pool. Therefore it should filter down giving each session its
	 * own object with which to slice. In this way if a user decides to
	 * do a long running slice, they only block themselves.
	 * 
	 * TODO User should be able to cancel slice...
	 * 
	 */
	private void doHandle(HttpServletRequest  request,
						  HttpServletResponse response)  throws IOException, ServletException {
				
		final String path    = request.getParameter("path");
		
		try {
			
			ServiceHolder.getLoaderService().clearSoftReferenceCache();
			
			final IDataHolder holder = DataServerUtils.getDataHolderWithLogging(path);
			//needs a clone, or doesn't serialise
			final Map<String, int[]> shapes = new HashMap<String, int[]> (holder.getDatasetShapes());
			
			IMarshallerService mservice = new XMLMarshallerService();
			final String xml = mservice.marshal(shapes);
			response.setContentType("text/xml;charset=utf-8");
			response.getWriter().println(xml);
		   
		} catch (Exception e) {
			logger.info("Read of shapes from {} failed due to {}", path, e.getMessage());
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().println("<h1>"+e.getMessage()+"</h1>");
		}
		
		 
	}

 }