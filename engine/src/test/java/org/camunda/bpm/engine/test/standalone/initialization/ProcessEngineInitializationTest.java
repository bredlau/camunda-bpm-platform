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
package org.camunda.bpm.engine.test.standalone.initialization;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.WrongDbException;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.db.DbSqlSession;
import org.camunda.bpm.engine.impl.db.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.test.PvmTestCase;

/**
 * @author Tom Baeyens
 */
public class ProcessEngineInitializationTest extends PvmTestCase {

  public void testNoTables() {
    try {
      ProcessEngineConfiguration
      .createProcessEngineConfigurationFromResource("org/camunda/bpm/engine/test/standalone/initialization/notables.camunda.cfg.xml")
        .buildProcessEngine();
      fail("expected exception");
    } catch (Exception e) {
      // OK
      assertTextPresent("no activiti tables in db", e.getMessage());
    }
  }

  public void testVersionMismatch() {
    // first create the schema
    ProcessEngineImpl processEngine = (ProcessEngineImpl) ProcessEngineConfiguration
      .createProcessEngineConfigurationFromResource("org/camunda/bpm/engine/test/standalone/initialization/notables.camunda.cfg.xml")
      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
      .buildProcessEngine();

    // then update the version to something that is different to the library
    // version
    DbSqlSessionFactory dbSqlSessionFactory = (DbSqlSessionFactory) processEngine.getProcessEngineConfiguration().getSessionFactories().get(DbSqlSession.class);
    SqlSessionFactory sqlSessionFactory = dbSqlSessionFactory.getSqlSessionFactory();
    SqlSession sqlSession = sqlSessionFactory.openSession();
    boolean success = false;
    try {
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("name", "schema.version");
      parameters.put("value", "25.7");
      parameters.put("revision", new Integer(1));
      parameters.put("newRevision", new Integer(2));
      sqlSession.update("updateProperty", parameters);
      success = true;
    } catch (Exception e) {
      throw new ProcessEngineException("couldn't update db schema version", e);
    } finally {
      if (success) {
        sqlSession.commit();
      } else {
        sqlSession.rollback();
      }
      sqlSession.close();
    }

    try {
      // now we can see what happens if when a process engine is being
      // build with a version mismatch between library and db tables
      ProcessEngineConfiguration
        .createProcessEngineConfigurationFromResource("org/camunda/bpm/engine/test/standalone/initialization/notables.camunda.cfg.xml")
        .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
        .buildProcessEngine();
      
      fail("expected exception");
    } catch (WrongDbException e) {
      assertTextPresent("version mismatch", e.getMessage());
      assertEquals("25.7", e.getDbVersion());
      assertEquals(ProcessEngine.VERSION, e.getLibraryVersion());
    }

    // closing the original process engine to drop the db tables
    processEngine.close();
  }
}
