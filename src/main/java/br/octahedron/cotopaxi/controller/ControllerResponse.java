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
package br.octahedron.cotopaxi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * Indicates an {@link Controller}'s response
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface ControllerResponse {

	/**
	 * Dispatches this {@link ControllerResponse} through a {@link HttpServletResponse}.
	 * 
	 * @param servletResponse
	 *            The {@link HttpServletResponse} to be used to dispatch this response.
	 * @throws IOException
	 *             If some error occurs while dispatching
	 */
	public abstract void dispatch(HttpServletResponse servletResponse) throws IOException;

}
