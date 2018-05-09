package com.sig.integration.coverity.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for getServerTimeResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="getServerTimeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getServerTimeResponse", propOrder = {
        "_return"
})
public class GetServerTimeResponse {

    @XmlElement(name = "return")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar _return;

    /**
     * Gets the value of the return property.
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setReturn(XMLGregorianCalendar value) {
        this._return = value;
    }

}
