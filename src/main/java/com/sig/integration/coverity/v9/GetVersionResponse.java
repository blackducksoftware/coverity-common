package com.sig.integration.coverity.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getVersionResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="getVersionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.coverity.com/v9}versionDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getVersionResponse", propOrder = {
        "_return"
})
public class GetVersionResponse {

    @XmlElement(name = "return")
    protected VersionDataObj _return;

    /**
     * Gets the value of the return property.
     * @return possible object is
     * {@link VersionDataObj }
     */
    public VersionDataObj getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * @param value allowed object is
     *              {@link VersionDataObj }
     */
    public void setReturn(VersionDataObj value) {
        this._return = value;
    }

}
