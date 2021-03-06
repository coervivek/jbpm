/**
 * Copyright 2010 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.process.audit;

import static org.jbpm.persistence.util.PersistenceUtil.createEnvironment;

import java.util.Properties;

import org.drools.core.RuleBase;
import org.drools.core.SessionConfiguration;
import org.drools.core.StatefulSession;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * This class tests the following classes:
 * <ul>
 * <li>WorkingMemoryDbLogger</li>
 * </ul>
 */
public class WorkingMemoryDbLoggerWithStatefulSessionTest extends AbstractWorkingMemoryDbLoggerTest {

    private StatefulSession session = null;

    @Override
    public ProcessInstance startProcess(String processId) {
        if (session == null) {
            // load the process
            RuleBase ruleBase = createKnowledgeBase();
            // create a new session
            Properties properties = new Properties();
            properties.put("drools.processInstanceManagerFactory", "org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory");
            properties.put("drools.processSignalManagerFactory", "org.jbpm.process.instance.event.DefaultSignalManagerFactory");
            SessionConfiguration config = new SessionConfiguration(properties);
            session = ruleBase.newStatefulSession(config, createEnvironment(context));
            
            new JPAWorkingMemoryDbLogger(session);
            session.getWorkItemManager().registerWorkItemHandler("Human Task", new SystemOutWorkItemHandler());
        }
        return session.startProcess(processId);
    }

}