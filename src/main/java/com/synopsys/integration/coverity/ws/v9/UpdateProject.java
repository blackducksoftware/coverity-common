/**
 * coverity-common
 *
 * Copyright (c) 2019 Synopsys, Inc.
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
package com.synopsys.integration.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for updateProject complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="updateProject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="projectId" type="{http://ws.coverity.com/v9}projectIdDataObj" minOccurs="0"/>
 *         &lt;element name="projectSpec" type="{http://ws.coverity.com/v9}projectSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateProject", propOrder = {
        "projectId",
        "projectSpec"
})
public class UpdateProject {

    protected ProjectIdDataObj projectId;
    protected ProjectSpecDataObj projectSpec;

    /**
     * Gets the value of the projectId property.
     * @return possible object is
     * {@link ProjectIdDataObj }
     */
    public ProjectIdDataObj getProjectId() {
        return projectId;
    }

    /**
     * Sets the value of the projectId property.
     * @param value allowed object is
     *              {@link ProjectIdDataObj }
     */
    public void setProjectId(ProjectIdDataObj value) {
        this.projectId = value;
    }

    /**
     * Gets the value of the projectSpec property.
     * @return possible object is
     * {@link ProjectSpecDataObj }
     */
    public ProjectSpecDataObj getProjectSpec() {
        return projectSpec;
    }

    /**
     * Sets the value of the projectSpec property.
     * @param value allowed object is
     *              {@link ProjectSpecDataObj }
     */
    public void setProjectSpec(ProjectSpecDataObj value) {
        this.projectSpec = value;
    }

}
