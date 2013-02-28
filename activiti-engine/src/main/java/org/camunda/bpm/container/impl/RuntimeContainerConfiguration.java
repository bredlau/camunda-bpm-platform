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
package org.camunda.bpm.container.impl;

import org.camunda.bpm.container.impl.jmx.JmxRuntimeContainerDelegate;
import org.camunda.bpm.container.spi.RuntimeContainerDelegate;

/**
 * Allows to access the resources provided by the RuntimeContainer. 
 * 
 * @see RuntimeContainerDelegate
 * 
 * @author Daniel Meyer
 *
 */
public class RuntimeContainerConfiguration {
  
  private final static RuntimeContainerConfiguration INSTANCE = new RuntimeContainerConfiguration();
  
  // hide
  private RuntimeContainerConfiguration() {
  }
  
  private RuntimeContainerDelegate containerDelegate = new JmxRuntimeContainerDelegate();
  
  private String runtimeContainerName = "Embedded JMX Server";
  
  public String getRuntimeContainerName() {
    return runtimeContainerName;
  }
  
  public RuntimeContainerDelegate getContainerDelegate() {
    return containerDelegate;
  }
  
  public void setContainerDelegate(RuntimeContainerDelegate containerDelegate) {
    this.containerDelegate = containerDelegate;
  }
  
  public void setRuntimeContainerName(String runtimeContainerName) {
    this.runtimeContainerName = runtimeContainerName;
  }
  
  public static RuntimeContainerConfiguration getInstance() {
    return INSTANCE;
  }

}