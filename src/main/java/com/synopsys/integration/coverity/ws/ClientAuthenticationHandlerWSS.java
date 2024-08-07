/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.ws;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * SOAP handler for user authentication using ws-security.  This mechanism inserts the user's user name and password in
 * the SOAP header of each message.
 */

public class ClientAuthenticationHandlerWSS implements SOAPHandler<SOAPMessageContext> {
    public static final String WSS_AUTH_PREFIX = "wsse";
    public static final String WSS_AUTH_LNAME = "Security";
    public static final String WSS_AUTH_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private final String username;
    private final String password;

    public ClientAuthenticationHandlerWSS(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound != null && outbound) {
            try {
                SOAPMessage message = context.getMessage();
                SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
                SOAPHeader header = envelope.getHeader();
                if (header == null) {
                    header = envelope.addHeader();
                }

                // Add WS-Security header
                SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
                security.addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

                // Add UsernameToken
                SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
                usernameToken.addAttribute(new QName("wsu:Id"), "UsernameToken-" + UUID.randomUUID().toString());

                // Add Username
                SOAPElement usernameElement = usernameToken.addChildElement("Username", "wsse");
                usernameElement.addTextNode(username);

                // Add Password
                SOAPElement passwordElement = usernameToken.addChildElement("Password", "wsse");
                passwordElement.addTextNode(password);
                passwordElement.addAttribute(new QName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

                message.saveChanges();
            } catch (SOAPException e) {
                throw new RuntimeException("Error adding WS-Security header", e);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.singleton(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security"));
    }
}
