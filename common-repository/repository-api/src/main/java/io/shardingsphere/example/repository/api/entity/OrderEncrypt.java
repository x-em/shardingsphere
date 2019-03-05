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

package io.shardingsphere.example.repository.api.entity;

import java.io.Serializable;

public class OrderEncrypt implements Serializable {
    
    private static final long serialVersionUID = 863434701950670170L;
    
    private long orderId;
    
    private int userId;
    
    private String md5Id;
    
    private String aesId;
    
    private String aesQueryId;
    
    public long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(final long orderId) {
        this.orderId = orderId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(final int userId) {
        this.userId = userId;
    }
    
    public String getMd5Id() {
        return md5Id;
    }
    
    public void setMd5Id(final String md5Id) {
        this.md5Id = md5Id;
    }
    
    public String getAesId() {
        return aesId;
    }
    
    public void setAesId(final String aesId) {
        this.aesId = aesId;
    }
    
    public String getAesQueryId() {
        return aesQueryId;
    }
    
    public void setAesQueryId(final String aesQueryId) {
        this.aesQueryId = aesQueryId;
    }
    
    @Override
    public String toString() {
        return String.format("order_id: %s, user_id: %s, md5_id: %s, aesId: %s, aesQueryId: %s", orderId, userId, md5Id, aesId, aesQueryId);
    }
}
