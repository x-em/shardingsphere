/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.data.pipeline.core.check.consistency;

import org.apache.shardingsphere.data.pipeline.spi.check.consistency.SingleTableDataCalculator;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmFactory;
import org.apache.shardingsphere.spi.ShardingSphereServiceLoader;

import java.util.Collection;
import java.util.Properties;

/**
 * Single table data calculator factory.
 */
public final class SingleTableDataCalculatorFactory {
    
    static {
        ShardingSphereServiceLoader.register(SingleTableDataCalculator.class);
    }
    
    /**
     * Create new instance of single table data calculator.
     *
     * @param type algorithm type
     * @param props properties
     * @return new instance of single table data calculator
     */
    public static SingleTableDataCalculator newInstance(final String type, final Properties props) {
        return ShardingSphereAlgorithmFactory.createAlgorithm(new ShardingSphereAlgorithmConfiguration(type, props), SingleTableDataCalculator.class);
    }
    
    /**
     * Get all single table data calculator instances.
     *
     * @return all single table data calculator instances
     */
    public static Collection<SingleTableDataCalculator> getAllInstances() {
        return ShardingSphereServiceLoader.getServiceInstances(SingleTableDataCalculator.class);
    }
}