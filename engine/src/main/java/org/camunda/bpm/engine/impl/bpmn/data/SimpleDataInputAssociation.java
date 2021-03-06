/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.bpmn.data;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

/**
 * A simple data input association between a source and a target with assignments
 * 
 * @author Esteban Robles Luna
 */
public class SimpleDataInputAssociation extends AbstractDataAssociation {

  protected List<Assignment> assignments = new ArrayList<Assignment>();
  
  public SimpleDataInputAssociation(Expression sourceExpression, String target) {
    super(sourceExpression, target);
  }

  public SimpleDataInputAssociation(String source, String target) {
    super(source, target);
  }

  public SimpleDataInputAssociation(String variables) {
    super(variables);
  }

  public SimpleDataInputAssociation(Expression businesKeyExpression) {
    super(businesKeyExpression);
  }

  public void addAssignment(Assignment assignment) {
    this.assignments.add(assignment);
  }

  public void evaluate(ActivityExecution execution) {
    for (Assignment assignment : this.assignments) {
      assignment.evaluate(execution);
    }
  }
}
