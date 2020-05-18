package com.synopsys.integration.coverity.ws;

import static com.synopsys.integration.coverity.ws.ClientAuthenticationHandlerWSS.WSS_AUTH_LNAME;
import static com.synopsys.integration.coverity.ws.ClientAuthenticationHandlerWSS.WSS_AUTH_PREFIX;
import static com.synopsys.integration.coverity.ws.ClientAuthenticationHandlerWSS.WSS_AUTH_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ClientAuthenticationHandlerWSSTest {
    public final String testUsername = "username";
    public final String testPassword = "password";

    @Test
    public void testHandleFault() {
        SOAPMessageContext mockSoapMessageContext = Mockito.mock(SOAPMessageContext.class);

        ClientAuthenticationHandlerWSS clientAuthenticationHandlerWSS = new ClientAuthenticationHandlerWSS(testUsername, testPassword);

        assertTrue(clientAuthenticationHandlerWSS.handleFault(mockSoapMessageContext));
    }

    @Test
    public void testGetHeaders() {
        ClientAuthenticationHandlerWSS clientAuthenticationHandlerWSS = new ClientAuthenticationHandlerWSS(testUsername, testPassword);
        QName expectedSecurityHeader = new QName(WSS_AUTH_URI, WSS_AUTH_LNAME, WSS_AUTH_PREFIX);

        Set<QName> headers = clientAuthenticationHandlerWSS.getHeaders();

        assertEquals(headers.size(), 1);
        assertEquals(headers.stream().findAny().orElse(null), expectedSecurityHeader);
    }

    @Test
    public void testHandleInboundMessage() {
        SOAPMessageContext mockedSoapMessageContext = Mockito.mock(SOAPMessageContext.class);
        Mockito.when(mockedSoapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).thenReturn(Boolean.FALSE);

        ClientAuthenticationHandlerWSS clientAuthenticationHandlerWSS = new ClientAuthenticationHandlerWSS(testUsername, testPassword);

        assertTrue(clientAuthenticationHandlerWSS.handleMessage(mockedSoapMessageContext));
    }

    // @Test
    public void testHandleOutboundMessage() {
        //TODO: Implement this
    }
}
