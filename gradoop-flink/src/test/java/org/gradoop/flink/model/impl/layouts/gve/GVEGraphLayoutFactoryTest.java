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
package org.gradoop.flink.model.impl.layouts.gve;

import org.gradoop.common.model.impl.pojo.Edge;
import org.gradoop.common.model.impl.pojo.GraphHead;
import org.gradoop.common.model.impl.pojo.Vertex;
import org.gradoop.flink.model.api.layouts.LogicalGraphLayoutFactory;
import org.gradoop.flink.model.impl.layouts.LogicalGraphLayoutFactoryTest;
import org.gradoop.flink.util.GradoopFlinkConfig;

/**
 * Test class {@link GVEGraphLayoutFactory}.
 */
public class GVEGraphLayoutFactoryTest extends LogicalGraphLayoutFactoryTest {
  @Override
  protected LogicalGraphLayoutFactory<GraphHead, Vertex, Edge> getFactory() {
    // create the factory to test
    GVEGraphLayoutFactory factory = new GVEGraphLayoutFactory();
    // create a default gradoop flink config and set it to the layout
    GradoopFlinkConfig config = GradoopFlinkConfig.createConfig(getExecutionEnvironment());
    factory.setGradoopFlinkConfig(config);
    return factory;
  }
}
