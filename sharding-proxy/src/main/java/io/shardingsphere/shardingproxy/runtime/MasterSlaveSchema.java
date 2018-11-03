/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
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
 * </p>
 */

package io.shardingsphere.shardingproxy.runtime;

import com.google.common.eventbus.Subscribe;
import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.core.rule.DataSourceParameter;
import io.shardingsphere.core.rule.MasterSlaveRule;
import io.shardingsphere.orchestration.internal.event.config.MasterSlaveRuleChangedEvent;
import io.shardingsphere.orchestration.internal.rule.OrchestrationMasterSlaveRule;
import lombok.Getter;

import java.util.Map;

/**
 * Master-slave schema.
 *
 * @author panjuan
 */
@Getter
public final class MasterSlaveSchema extends LogicSchema {
    
    private MasterSlaveRule masterSlaveRule;
    
    public MasterSlaveSchema(final String name, final Map<String, DataSourceParameter> dataSources, final MasterSlaveRuleConfiguration masterSlaveRuleConfig, final boolean isUsingRegistry) {
        super(name, dataSources);
        masterSlaveRule = getMasterSlaveRule(masterSlaveRuleConfig, isUsingRegistry);
    }
    
    private MasterSlaveRule getMasterSlaveRule(final MasterSlaveRuleConfiguration masterSlaveRule, final boolean isUsingRegistry) {
        return isUsingRegistry ? new OrchestrationMasterSlaveRule(masterSlaveRule) : new MasterSlaveRule(masterSlaveRule);
    }
    
    /**
     * Renew master-slave rule.
     *
     * @param masterSlaveEvent master-slave event.
     */
    @Subscribe
    public void renew(final MasterSlaveRuleChangedEvent masterSlaveEvent) {
        if (!getName().equals(masterSlaveEvent.getShardingSchemaName())) {
            return;
        }
        masterSlaveRule = new MasterSlaveRule(masterSlaveEvent.getMasterSlaveRuleConfig());
    }
}
