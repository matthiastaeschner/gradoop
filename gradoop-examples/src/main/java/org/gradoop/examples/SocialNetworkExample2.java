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
 * along with Gradoop. If not, see <http://www.gnu.org/licenses/>.
 */

package org.gradoop.examples;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.flink.api.common.ProgramDescription;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.gradoop.model.api.functions.TransformationFunction;
import org.gradoop.model.impl.EPGMDatabase;
import org.gradoop.model.impl.LogicalGraph;
import org.gradoop.model.impl.algorithms.labelpropagation.GellyLabelPropagation;
import org.gradoop.model.impl.operators.aggregation.ApplyAggregation;
import org.gradoop.model.impl.operators.aggregation.functions.EdgeCount;
import org.gradoop.model.impl.operators.aggregation.functions.VertexCount;
import org.gradoop.model.impl.operators.combination.ReduceCombination;
import org.gradoop.model.impl.pojo.EdgePojo;
import org.gradoop.model.impl.pojo.GraphHeadPojo;
import org.gradoop.model.impl.pojo.VertexPojo;
import org.gradoop.util.GradoopFlinkConfig;

/**
 * Benchmark program that works on LDBC datasets.
 *
 * The program executes the following workflow:
 *
 * 1) Extract subgraph with:
 *    - vertex predicate: must be of type 'Person'
 *    - edge predicate: must be of type 'knows'
 * 2) Transform vertices and edges to necessary information
 * 3) Compute communities using label propagation
 * 4) Compute vertex count per community
 * 5) Select communities with a vertex count greater than a given threshold
 * 6) Combine the remaining graphs to a single graph
 * 7) Group the graph using the vertex attributes 'city' and 'gender' and
 *    - count the number of vertices represented by each super vertex
 *    - count the number of edges represented by each super edge
 * 8) Aggregate the grouped graph:
 *    - add the total vertex count as new graph property
 *    - add the total edge count as new graph property
 */
public class SocialNetworkExample2 implements ProgramDescription {

  /**
   * File containing EPGM vertices.
   */
  public static final String NODES_JSON = "nodes.json";
  /**
   * File containing EPGM edges.
   */
  public static final String EDGES_JSON = "edges.json";
  /**
   * File containing EPGM graph heads.
   */
  public static final String GRAPHS_JSON = "graphs.json";

  /**
   * Runs the example program.
   *
   * Need a (possibly HDFS) input directory that contains
   *  - nodes.json
   *  - edges.json
   *  - graphs.json
   *
   * Needs a (possibly HDFS) output directory to write the resulting graph to.
   *
   * @param args args[0] = input dir, args[1] output dir
   * @throws Exception
   */
  @SuppressWarnings({
    "unchecked",
    "Duplicates"
  })
  public static void main(String[] args) throws Exception {
    Preconditions.checkArgument(
      args.length == 2, "input dir and output dir required");
    String inputDir  = args[0];
    String outputDir = args[1];

    ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

    GradoopFlinkConfig<GraphHeadPojo, VertexPojo, EdgePojo> gradoopConf =
      GradoopFlinkConfig.createDefaultConfig(env);

    EPGMDatabase<GraphHeadPojo, VertexPojo, EdgePojo> epgmDatabase =
      EPGMDatabase.fromJsonFile(
        inputDir + NODES_JSON,
        inputDir + EDGES_JSON,
        inputDir + GRAPHS_JSON,
        gradoopConf
      );

    LogicalGraph<GraphHeadPojo, VertexPojo, EdgePojo> result =
      execute(epgmDatabase.getDatabaseGraph());

    result.writeAsJson(
      outputDir + NODES_JSON,
      outputDir + EDGES_JSON,
      outputDir + GRAPHS_JSON
    );
  }

  /**
   * The actual computation.
   *
   * @param socialNetwork social network graph
   * @return summarized, aggregated graph
   */
  private static LogicalGraph<GraphHeadPojo, VertexPojo, EdgePojo>
  execute(LogicalGraph<GraphHeadPojo, VertexPojo, EdgePojo> socialNetwork) {

    final int maxIterations   = 4;
    // LDBC graphalytics dataset:
    // 1    -   1000
    // 10   -   7500
    // 100  -  50000
    // 1000 - 350000
    final int minClusterSize  = 350000;
    final String vertexCount  = "vertexCount";
    final String edgeCount    = "edgeCount";
    final String person       = "person";
    final String knows        = "knows";
    final String city         = "city";
    final String gender       = "gender";
    final String birthday     = "birthday";
    final String label        = "label";

    return socialNetwork
      // 1) extract subgraph
      .subgraph(new FilterFunction<VertexPojo>() {
        @Override
        public boolean filter(VertexPojo vertex) throws Exception {
          return vertex.getLabel().equals(person);
        }
      }, new FilterFunction<EdgePojo>() {
        @Override
        public boolean filter(EdgePojo edge) throws Exception {
          return edge.getLabel().equals(knows);
        }
      })
      // 2) project to necessary information
      .transform(new TransformationFunction<GraphHeadPojo>() {
        @Override
        public GraphHeadPojo execute(GraphHeadPojo current,
          GraphHeadPojo transformed) {
          return current;
        }
      }, new TransformationFunction<VertexPojo>() {
        @Override
        public VertexPojo execute(VertexPojo current, VertexPojo transformed) {
          transformed.setLabel(current.getLabel());
          transformed.setProperty(city, current.getPropertyValue(city));
          transformed.setProperty(gender, current.getPropertyValue(gender));
          transformed.setProperty(label, current.getPropertyValue(birthday));
          return transformed;
        }
      }, new TransformationFunction<EdgePojo>() {
        @Override
        public EdgePojo execute(EdgePojo current, EdgePojo transformed) {
          transformed.setLabel(current.getLabel());
          return transformed;
        }
      })
      // 3a) compute communities
      .callForGraph(
        new GellyLabelPropagation<GraphHeadPojo, VertexPojo, EdgePojo>(
          maxIterations, label))
      // 3b) separate communities
      .splitBy(label)
      // 4) compute vertex count per community
      .apply(new ApplyAggregation<>(
          vertexCount, new VertexCount<GraphHeadPojo, VertexPojo, EdgePojo>()))
      // 5) select graphs with more than minClusterSize vertices
      .select(new FilterFunction<GraphHeadPojo>() {
        @Override
        public boolean filter(GraphHeadPojo g) throws Exception {
          return g.getPropertyValue(vertexCount).getLong() > minClusterSize;
        }
      })
      // 6) reduce filtered graphs to a single graph using combination
      .reduce(new ReduceCombination<GraphHeadPojo, VertexPojo, EdgePojo>())
      // 7) group that graph by vertex properties
      .groupBy(Lists.newArrayList(city, gender))
      // 8a) count vertices of grouped graph
      .aggregate(
        vertexCount, new VertexCount<GraphHeadPojo, VertexPojo, EdgePojo>())
      // 8b) count edges of grouped graph
      .aggregate(
        edgeCount, new EdgeCount<GraphHeadPojo, VertexPojo, EdgePojo>());
  }

  @Override
  public String getDescription() {
    return SocialNetworkExample2.class.getName();
  }
}