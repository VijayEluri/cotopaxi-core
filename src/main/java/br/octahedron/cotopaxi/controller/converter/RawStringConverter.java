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
package br.octahedron.cotopaxi.controller.converter;

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.controller.ConvertionException;

/**
 * A raw string converter. This class doesn't perform any convertion, just returns the raw String.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RawStringConverter implements Converter<String> {

	@Override
	public String convert(String input) throws ConvertionException {
		return input;
	}

}
