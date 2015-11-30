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

package org.gradoop.storage.impl.hbase;

import org.gradoop.model.api.EPGMEdge;
import org.gradoop.model.impl.id.GradoopId;
import org.gradoop.model.impl.pojo.VertexPojo;
import org.gradoop.storage.api.PersistentEdge;

/**
 * Represents a persistent edge data object.
 */
public class HBaseEdge
  extends HBaseGraphElement<EPGMEdge>
  implements PersistentEdge<VertexPojo> {

  /**
   * Source vertex
   */
  private VertexPojo source;

  /**
   * Target vertex.
   */
  private VertexPojo target;

  /**
   * Creates persistent edge.
   *
   * @param edge    edge
   * @param source  source vertex
   * @param target  target vertex
   */
  HBaseEdge(EPGMEdge edge,
    VertexPojo source,
    VertexPojo target) {
    super(edge);
    this.source = source;
    this.target = target;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexPojo getSource() {
    return source;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSource(VertexPojo sourceVertex) {
    this.source = sourceVertex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VertexPojo getTarget() {
    return target;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTarget(VertexPojo targetVertex) {
    this.target = targetVertex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GradoopId getSourceId() {
    return getEpgmElement().getSourceId();
  }

  /**
   * {@inheritDoc}
   * @param sourceId
   */
  @Override
  public void setSourceId(GradoopId sourceId) {
    getEpgmElement().setSourceId(sourceId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GradoopId getTargetId() {
    return getEpgmElement().getTargetId();
  }

  /**
   * {@inheritDoc}
   * @param targetId
   */
  @Override
  public void setTargetId(GradoopId targetId) {
    getEpgmElement().setTargetId(targetId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HBaseEdge{");
    sb.append("super=").append(super.toString());
    sb.append(", source=").append(source);
    sb.append(", target=").append(target);
    sb.append('}');
    return sb.toString();
  }
}