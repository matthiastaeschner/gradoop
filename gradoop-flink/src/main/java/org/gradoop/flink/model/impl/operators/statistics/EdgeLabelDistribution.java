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
package org.gradoop.flink.model.impl.operators.statistics;

import org.apache.flink.api.java.DataSet;
import org.gradoop.flink.model.impl.epgm.LogicalGraph;
import org.gradoop.flink.model.api.operators.UnaryGraphToValueOperator;
import org.gradoop.flink.model.impl.functions.epgm.Label;
import org.gradoop.flink.model.impl.tuples.WithCount;

/**
 * Convenience operator to compute the distribution of edge label.
 *
 * For each edge label, the output contains a tuple consisting of the label and the number of edges
 * with that label.
 */
public class EdgeLabelDistribution
  implements UnaryGraphToValueOperator<DataSet<WithCount<String>>> {

  @Override
  public DataSet<WithCount<String>> execute(LogicalGraph graph) {
    return new EdgeValueDistribution<>(new Label<>()).execute(graph);
  }

  @Override
  public String getName() {
    return EdgeLabelDistribution.class.getName();
  }
}
