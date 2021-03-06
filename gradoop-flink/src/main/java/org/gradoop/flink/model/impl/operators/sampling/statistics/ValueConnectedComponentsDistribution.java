/*
 * Copyright © 2014 - 2018 Leipzig University (Database Research Group)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradoop.flink.model.impl.operators.sampling.statistics;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.gradoop.flink.algorithms.gelly.connectedcomponents.ValueWeaklyConnectedComponents;
import org.gradoop.flink.model.impl.epgm.LogicalGraph;
import org.gradoop.flink.model.api.operators.UnaryGraphToValueOperator;

/**
 * Computes the weakly connected components of a graph structure. Uses the gradoop wrapper
 * {@link ValueWeaklyConnectedComponents} of Flinks ConnectedComponents.
 *
 * Returns a mapping of VertexId -> ComponentId
 */
public class ValueConnectedComponentsDistribution
  implements UnaryGraphToValueOperator<DataSet<Tuple2<Long, Long>>> {

  /**
   * Max iterations.
   */
  private final int maxIteration;

  /**
   * Creates an instance of this operator to calculate the connected components distribution.
   *
   * @param maxiIteration max iteration count.
   */
  public ValueConnectedComponentsDistribution(int maxiIteration) {
    this.maxIteration = maxiIteration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSet<Tuple2<Long, Long>> execute(LogicalGraph graph) {
    return new ValueWeaklyConnectedComponents(maxIteration).execute(graph);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return ValueConnectedComponentsDistribution.class.getName();
  }
}
