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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for fileIdDataObj complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="fileIdDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contentsMD5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="filePathname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileIdDataObj", propOrder = {
        "contentsMD5",
        "filePathname"
})
public class FileIdDataObj {

    @XmlElement(required = true)
    protected String contentsMD5;
    @XmlElement(required = true)
    protected String filePathname;

    /**
     * Gets the value of the contentsMD5 property.
     * @return possible object is
     * {@link String }
     */
    public String getContentsMD5() {
        return contentsMD5;
    }

    /**
     * Sets the value of the contentsMD5 property.
     * @param value allowed object is
     *              {@link String }
     */
    public void setContentsMD5(String value) {
        this.contentsMD5 = value;
    }

    /**
     * Gets the value of the filePathname property.
     * @return possible object is
     * {@link String }
     */
    public String getFilePathname() {
        return filePathname;
    }

    /**
     * Sets the value of the filePathname property.
     * @param value allowed object is
     *              {@link String }
     */
    public void setFilePathname(String value) {
        this.filePathname = value;
    }

}
