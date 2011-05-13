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
package br.octahedron.cotopaxi.request;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.response.ResponseProcessor;
import br.octahedron.cotopaxi.response.ResponseProcessorManager;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ControllerExecutorProcessor implements RequestProcessor {

	private ResponseProcessor responseProcessor;
	
	public ControllerExecutorProcessor(ResponseProcessorManager responseProcessor) {
		this.responseProcessor = responseProcessor;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.controller.RequestProcessor#process(br.octahedron.cotopaxi.controller.Request)
	 */
	@Override
	public void process(Request request) throws RequestProcessingException {
		Controller controller = request.getController();
		controller.process(request, this.responseProcessor);
	}

}