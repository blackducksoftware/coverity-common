/**
 * coverity-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.sig.integration.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for updateComponentMap complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="updateComponentMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapId" type="{http://ws.coverity.com/v9}componentMapIdDataObj" minOccurs="0"/>
 *         &lt;element name="componentMapSpec" type="{http://ws.coverity.com/v9}componentMapSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateComponentMap", propOrder = {
        "componentMapId",
        "componentMapSpec"
})
public class UpdateComponentMap {

    protected ComponentMapIdDataObj componentMapId;
    protected ComponentMapSpecDataObj componentMapSpec;

    /**
     * Gets the value of the componentMapId property.
     * @return possible object is
     * {@link ComponentMapIdDataObj }
     */
    public ComponentMapIdDataObj getComponentMapId() {
        return componentMapId;
    }

    /**
     * Sets the value of the componentMapId property.
     * @param value allowed object is
     *              {@link ComponentMapIdDataObj }
     */
    public void setComponentMapId(ComponentMapIdDataObj value) {
        this.componentMapId = value;
    }

    /**
     * Gets the value of the componentMapSpec property.
     * @return possible object is
     * {@link ComponentMapSpecDataObj }
     */
    public ComponentMapSpecDataObj getComponentMapSpec() {
        return componentMapSpec;
    }

    /**
     * Sets the value of the componentMapSpec property.
     * @param value allowed object is
     *              {@link ComponentMapSpecDataObj }
     */
    public void setComponentMapSpec(ComponentMapSpecDataObj value) {
        this.componentMapSpec = value;
    }

}
