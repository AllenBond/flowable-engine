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
package org.flowable.cmmn.engine.impl.behavior.impl;

import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.cmmn.engine.impl.behavior.CoreCmmnActivityBehavior;
import org.flowable.cmmn.engine.impl.persistence.entity.PlanItemInstanceEntity;
import org.flowable.cmmn.engine.impl.util.CmmnLoggingSessionUtil;
import org.flowable.cmmn.engine.impl.util.CommandContextUtil;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.logging.LoggingSessionConstants;

/**
 * @author Joram Barrez
 */
public class PlanItemJavaDelegateActivityBehavior extends CoreCmmnActivityBehavior {
    
    protected PlanItemJavaDelegate planItemJavaDelegate;

    public PlanItemJavaDelegateActivityBehavior(PlanItemJavaDelegate planItemJavaDelegate) {
        this.planItemJavaDelegate = planItemJavaDelegate;
    }
    
    @Override
    public void execute(CommandContext commandContext, PlanItemInstanceEntity planItemInstanceEntity) {
        CmmnEngineConfiguration cmmnEngineConfiguration = CommandContextUtil.getCmmnEngineConfiguration(commandContext);
        if (cmmnEngineConfiguration.isLoggingSessionEnabled()) {
            CmmnLoggingSessionUtil.addLoggingData(LoggingSessionConstants.TYPE_SERVICE_TASK_ENTER, 
                            "Executing service task with java class " + planItemJavaDelegate.getClass().getName(), planItemInstanceEntity);
        }
        
        planItemJavaDelegate.execute(planItemInstanceEntity);
        
        if (cmmnEngineConfiguration.isLoggingSessionEnabled()) {
            CmmnLoggingSessionUtil.addLoggingData(LoggingSessionConstants.TYPE_SERVICE_TASK_EXIT, 
                            "Executed service task with java class " + planItemJavaDelegate.getClass().getName(), planItemInstanceEntity);
        }
        
        CommandContextUtil.getAgenda(commandContext).planCompletePlanItemInstanceOperation(planItemInstanceEntity);
    }

}
