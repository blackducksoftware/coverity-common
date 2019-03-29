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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getMergedDefectDetectionHistory complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="getMergedDefectDetectionHistory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mergedDefectIdDataObj" type="{http://ws.coverity.com/v9}mergedDefectIdDataObj" minOccurs="0"/>
 *         &lt;element name="streamIds" type="{http://ws.coverity.com/v9}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMergedDefectDetectionHistory", propOrder = {
        "mergedDefectIdDataObj",
        "streamIds"
})
public class GetMergedDefectDetectionHistory {

    protected MergedDefectIdDataObj mergedDefectIdDataObj;
    protected List<StreamIdDataObj> streamIds;

    /**
     * Gets the value of the mergedDefectIdDataObj property.
     * @return possible object is
     * {@link MergedDefectIdDataObj }
     */
    public MergedDefectIdDataObj getMergedDefectIdDataObj() {
        return mergedDefectIdDataObj;
    }

    /**
     * Sets the value of the mergedDefectIdDataObj property.
     * @param value allowed object is
     *              {@link MergedDefectIdDataObj }
     */
    public void setMergedDefectIdDataObj(MergedDefectIdDataObj value) {
        this.mergedDefectIdDataObj = value;
    }

    /**
     * Gets the value of the streamIds property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIds property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     */
    public List<StreamIdDataObj> getStreamIds() {
        if (streamIds == null) {
            streamIds = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIds;
    }

}
