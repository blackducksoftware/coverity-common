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
    public static final String WSS_AUTH_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public static final String NAMESPACE_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    public static final String PASSWORD_QNAME_VALUE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
    public static final String WSS_AUTH_PREFIX = "wsse";
    public static final String WSS_AUTH_LNAME = "Security";
    public static final String WSU_NAMESPACE_PREFIX = "wsu";
    public static final String USERNAME_LNAME = "Username";
    public static final String PASSWORD_LNAME = "Password";
    public static final String USERNAME_TOKEN = "UsernameToken";
    public static final String USERNAME_TOKEN_QNAME_LPART = "wsu:Id";
    public static final String TYPE = "Type";
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

                SOAPElement security = header.addChildElement(WSS_AUTH_LNAME, WSS_AUTH_PREFIX, WSS_AUTH_URI);
                security.addNamespaceDeclaration(WSU_NAMESPACE_PREFIX, NAMESPACE_URI);

                SOAPElement usernameToken = security.addChildElement(USERNAME_TOKEN, WSS_AUTH_PREFIX);
                usernameToken.addAttribute(new QName(USERNAME_TOKEN_QNAME_LPART),
                        USERNAME_TOKEN + "-" + UUID.randomUUID().toString());

                SOAPElement usernameElement = usernameToken.addChildElement(USERNAME_LNAME, WSS_AUTH_PREFIX);
                usernameElement.addTextNode(username);

                SOAPElement passwordElement = usernameToken.addChildElement(PASSWORD_LNAME, WSS_AUTH_PREFIX);
                passwordElement.addTextNode(password);
                passwordElement.addAttribute(new QName(TYPE), PASSWORD_QNAME_VALUE);

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
        // Do nothing
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.singleton(new QName(WSS_AUTH_URI, WSS_AUTH_LNAME));
    }
}
