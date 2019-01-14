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
 * <p>Java class for streamFilterSpecDataObj complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="streamFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="languageList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="descriptionPattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="namePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamFilterSpecDataObj", propOrder = {
        "languageList",
        "descriptionPattern",
        "namePattern"
})
public class StreamFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<String> languageList;
    protected String descriptionPattern;
    protected String namePattern;

    /**
     * Gets the value of the languageList property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the languageList property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguageList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getLanguageList() {
        if (languageList == null) {
            languageList = new ArrayList<String>();
        }
        return this.languageList;
    }

    /**
     * Gets the value of the descriptionPattern property.
     * @return possible object is
     * {@link String }
     */
    public String getDescriptionPattern() {
        return descriptionPattern;
    }

    /**
     * Sets the value of the descriptionPattern property.
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescriptionPattern(String value) {
        this.descriptionPattern = value;
    }

    /**
     * Gets the value of the namePattern property.
     * @return possible object is
     * {@link String }
     */
    public String getNamePattern() {
        return namePattern;
    }

    /**
     * Sets the value of the namePattern property.
     * @param value allowed object is
     *              {@link String }
     */
    public void setNamePattern(String value) {
        this.namePattern = value;
    }

}
