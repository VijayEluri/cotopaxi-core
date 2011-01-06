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
package br.octahedron.cotopaxi.controller.adapters;

import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.converter.DateConverter;
import br.octahedron.cotopaxi.model.attribute.converter.IntegerConverter;
import br.octahedron.cotopaxi.model.attribute.converter.StringCommaSeparatedArrayConverter;
import br.octahedron.cotopaxi.model.attribute.validator.RangeValidator;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ManyAdapter extends InputAdapter {

	public ManyAdapter() {
		this.addAttribute("str", StringCommaSeparatedArrayConverter.class);
		this.addAttribute("when", DateConverter.class);
		this.addAttribute("int", IntegerConverter.class, new RangeValidator<Integer>(new Integer(1), new Integer(10)));
	}
}