/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cotopaxi;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.octahedron.cotopaxi.controller.ModelController;
import br.octahedron.cotopaxi.controller.auth.AuthManager;
import br.octahedron.cotopaxi.controller.auth.UserNotAuthorizedException;
import br.octahedron.cotopaxi.controller.auth.UserNotLoggedException;
import br.octahedron.cotopaxi.metadata.MetadataHandler;
import br.octahedron.cotopaxi.metadata.MetadataMapper;
import br.octahedron.cotopaxi.metadata.PageNotFoundExeption;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.model.response.SuccessActionResponse;
import br.octahedron.cotopaxi.view.i18n.LocaleManager;
import br.octahedron.cotopaxi.view.response.ViewResponse;
import br.octahedron.cotopaxi.view.response.ViewResponseBuilder;
import br.octahedron.util.ThreadProperties;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * The Servlet is the entry point for the requests/responses. It's responsible by route the request,
 * and pass the request and the ModelMetadata to the controller manager and deliver the
 * ModelResponse to the ViewManager.
 * 
 * @see ControllerManager
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class CotopaxiServlet extends HttpServlet {

	private static final long serialVersionUID = 8958499809792016589L;

	/**
	 * Property used to define the CotopaxiConfigurator to be used by application. This property
	 * must be defined at "web.xml" file.
	 */
	private static final String MODEL_CONFIGURATOR_PROPERTY = "CONFIGURATOR";

	private static final Logger logger = Logger.getLogger(CotopaxiServlet.class.getName());
	private transient ModelController controller;
	private transient ViewResponseBuilder view;
	private transient MetadataMapper mapper;
	private transient AuthManager auth;
	private CotopaxiConfigView config;

	private LocaleManager localeManager;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try {
			logger.info("Loading Cotopaxi...");
			// Cotopaxi configuration
			this.configure(servletConfig.getInitParameter(MODEL_CONFIGURATOR_PROPERTY));
			// creating mapper
			this.mapper = new MetadataMapper(this.config);
			// create the Model Controller
			this.controller = new ModelController(this.config);
			// create Authentication Manager
			this.auth = new AuthManager(this.config);
			// create the ViewerManager
			this.view = new ViewResponseBuilder(this.config);
			this.localeManager = LocaleManager.getInstance();
			// Done!
			logger.info("Cotopaxi is ready to serve!");
		} catch (ClassNotFoundException e) {
			throw new ServletException("The class " + e.getMessage()
					+ " was not found. You should use the full class name (including the package). Eg.: java.lang.String");
		} catch (InstantiationException e) {
			throw new ServletException(
					"Unable to instantiate the Configurator. The configurator should have a Constructor method without parameters.");
		} catch (NoSuchMethodException e) {
			throw new ServletException(
					"Some of the given facades doesn't have a valid Constructor. All facades should have a Constructor method without parameters.");
		} catch (Exception e) {
			throw new ServletException("Error creating ModelFacade: " + e.getMessage(), e);
		}
	}

	/**
	 * Used by tests. Create and calls the {@link CotopaxiConfigurator}
	 */
	protected void configure(String configuratorClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.config = CotopaxiConfigView.getInstance();
		if (configuratorClassName != null) {
			CotopaxiConfig configurable = this.config.getCotopaxiConfig();
			CotopaxiConfigurator configurator = (CotopaxiConfigurator) ReflectionUtil.createInstance(configuratorClassName);
			configurator.configure(configurable);
		} else {
			throw new ClassNotFoundException(null);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.deliver(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.deliver(req, resp);
	}

	/**
	 * Dispatches a request/response.
	 * 
	 * <pre>
	 * 	Check if the requested URL is a redirect
	 * 		If is, redirect;
	 * 	Finds the {@link MetadataHandler}
	 * 		If not found, call the {@link ViewResponseBuilder} to show 404 page
	 * 	Call the {@link ControllerManager} to execute the controller
	 * 	Deliver the {@link SuccessActionResponse} to the {@link ViewResponseBuilder}
	 * </pre>
	 */
	private void deliver(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ViewResponse viewResponse;
		RequestWrapper request = new RequestWrapper(req);
		String requestedURL = request.getURL();

		if (this.config.isRedirect(requestedURL)) {
			// request is a redirect, send response!
			logger.fine("Redirecting request for " + requestedURL);
			viewResponse = this.view.getViewResponse(requestedURL);
		} else {
			this.prepareThreadProperties(req);
			Locale lc = this.localeManager.getLocale(request);
			try {
				MetadataHandler metadata = this.mapper.getMapping(request);
				this.auth.authorizeUser(metadata.getLoginMetadata()); // just check auth!
				ActionResponse actionResp = this.controller.executeRequest(request, metadata.getActionMetadata());
				viewResponse = this.view.getViewResponse(lc, request.getFormat(), actionResp, metadata);
			} catch (UserNotLoggedException e) {
				logger.fine("Request for " + requestedURL + " failed due authorization restrictions: no user logged!");
				viewResponse = this.view.getViewResponse(lc, e);
			} catch (UserNotAuthorizedException e) {
				logger.fine("Request for " + requestedURL + " failed due authorization restrictions: user not authorized!");
				viewResponse = this.view.getViewResponse(lc, e);
			} catch (PageNotFoundExeption pnfex) {
				logger.fine("Page Not Found! Requested url was " + requestedURL);
				viewResponse = this.view.getViewResponse(lc, pnfex);
			} catch (Exception ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
				viewResponse = this.view.getViewResponse(lc, ex);
			} finally {
				this.finalizeThreadProperties(req);
			}
		}

		// dispatch the view response
		try {
			ResponseWrapper response = new ResponseWrapper(resp);
			viewResponse.dispatch(response);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			Locale lc = this.localeManager.getLocale(request);
			viewResponse = this.view.getViewResponse(lc, e);
			ResponseWrapper response = new ResponseWrapper(resp);
			viewResponse.dispatch(response);
		}
	}

	/**
	 * Take the sessions attributes and put them at the {@link ThreadProperties}
	 */
	@SuppressWarnings("unchecked")
	private void prepareThreadProperties(HttpServletRequest request) {
		// Remove previous properties
		ThreadProperties.clear();
		// Get atts from session and put on ThreadProperties
		HttpSession session = request.getSession(false);
		if (session != null) {
			Enumeration<String> atts = session.getAttributeNames();
			while (atts.hasMoreElements()) {
				String att = atts.nextElement();
				ThreadProperties.setProperty(att, session.getAttribute(att));
			}
		}
	}

	/**
	 * Take the {@link ThreadProperties} attributes and put them at the Session (or remove from
	 * session)
	 */
	private void finalizeThreadProperties(HttpServletRequest request) {
		// Get atts from ThreadProperties and put on Session
		HttpSession session = request.getSession(false);
		if (!ThreadProperties.isEmpty()) {
			for (String key : ThreadProperties.keys()) {
				Object value = ThreadProperties.getProperty(key);
				if (value == null && session != null) {
					session.removeAttribute(key);
				} else {
					if (session == null) {
						session = request.getSession(true);
					}
					session.setAttribute(key, value);
				}
			}
		}
	}
}