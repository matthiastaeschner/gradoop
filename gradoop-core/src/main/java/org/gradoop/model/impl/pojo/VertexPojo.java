/*
 * This file is part of Gradoop.
 *
 * Gradoop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Gradoop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Gradoop.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gradoop.model.impl.pojo;

import org.gradoop.model.api.EPGMVertex;
import org.gradoop.model.impl.id.GradoopId;
import org.gradoop.model.impl.id.GradoopIdSet;
import org.gradoop.model.impl.properties.PropertyList;

/**
 * POJO Implementation of an EPGM vertex.
 */
public class VertexPojo extends GraphElementPojo implements EPGMVertex {

  /**
   * Default constructor.
   */
  public VertexPojo() {
  }

  /**
   * Creates a vertex based on the given parameters.
   *
   * @param id         vertex identifier
   * @param label      vertex label
   * @param properties vertex properties
   * @param graphs     graphs that vertex is contained in
   */
  VertexPojo(
    final GradoopId id,
    final String label,
    final PropertyList properties,
    final GradoopIdSet graphs) {
    super(id, label, properties, graphs);
  }

  @Override
  public String toString() {
    return String.format("(%s)", super.toString());
  }
}