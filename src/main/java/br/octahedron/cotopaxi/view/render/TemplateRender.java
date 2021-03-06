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
package br.octahedron.cotopaxi.view.render;

import java.io.Writer;
import java.util.Map;

import br.octahedron.cotopaxi.CotopaxiProperty;

/**
 * The default interface for TemplateRenders
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface TemplateRender {

	/**
	 * Renders the given template, using the given output {@link Map}, writing to to given {@link Writer}
	 *  
	 * @param templatePath The template to be used, relative to the {@link CotopaxiProperty#TEMPLATE_FOLDER}.
	 * @param output The output objects {@link Map}
	 * @param writer The writer to be used to write the rendered template. It isn't necessary to close/flush the writer.
	 */
	public abstract void render(String templatePath, Map<String, Object> output, Writer writer);

}