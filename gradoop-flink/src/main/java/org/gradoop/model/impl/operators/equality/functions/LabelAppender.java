package org.gradoop.model.impl.operators.equality.functions;

import org.apache.flink.api.common.functions.JoinFunction;
import org.gradoop.model.impl.operators.equality.tuples.DataLabel;

public class LabelAppender
  implements JoinFunction<DataLabel, DataLabel, DataLabel>{

  @Override
  public DataLabel join(DataLabel left, DataLabel right)
    throws Exception {

    left.setLabel(
      left.getLabel() + "(" + (right == null ? "" : right.getLabel()) + ")"
    );

    return left;
  }
}