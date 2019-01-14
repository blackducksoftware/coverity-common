/**
 * coverity-common
 *
 * Copyright (C) 2019 Black Duck Software, Inc.
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
package com.synopsys.integration.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for groupSpecDataObj complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="groupSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="domain" type="{http://ws.coverity.com/v9}serverDomainIdDataObj" minOccurs="0"/>
 *         &lt;element name="local" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roleAssignments" type="{http://ws.coverity.com/v9}roleAssignmentDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="syncEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupSpecDataObj", propOrder = {
        "domain",
        "local",
        "name",
        "roleAssignments",
        "syncEnabled"
})
public class GroupSpecDataObj {

    protected ServerDomainIdDataObj domain;
    protected Boolean local;
    protected String name;
    @XmlElement(nillable = true)
    protected List<RoleAssignmentDataObj> roleAssignments;
    protected Boolean syncEnabled;

    /**
     * Gets the value of the domain property.
     * @return possible object is
     * {@link ServerDomainIdDataObj }
     */
    public ServerDomainIdDataObj getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * @param value allowed object is
     *              {@link ServerDomainIdDataObj }
     */
    public void setDomain(ServerDomainIdDataObj value) {
        this.domain = value;
    }

    /**
     * Gets the value of the local property.
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setLocal(Boolean value) {
        this.local = value;
    }

    /**
     * Gets the value of the name property.
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the roleAssignments property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roleAssignments property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleAssignments().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoleAssignmentDataObj }
     */
    public List<RoleAssignmentDataObj> getRoleAssignments() {
        if (roleAssignments == null) {
            roleAssignments = new ArrayList<RoleAssignmentDataObj>();
        }
        return this.roleAssignments;
    }

    /**
     * Gets the value of the syncEnabled property.
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isSyncEnabled() {
        return syncEnabled;
    }

    /**
     * Sets the value of the syncEnabled property.
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setSyncEnabled(Boolean value) {
        this.syncEnabled = value;
    }

}
