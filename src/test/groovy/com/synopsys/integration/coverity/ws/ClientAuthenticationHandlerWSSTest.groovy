package com.synopsys.integration.coverity.ws

import org.junit.jupiter.api.Test
import org.mockito.Mockito

import javax.xml.namespace.QName
import javax.xml.ws.handler.MessageContext
import javax.xml.ws.handler.soap.SOAPMessageContext

import static com.synopsys.integration.coverity.ws.ClientAuthenticationHandlerWSS.*
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

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

    //TODO: Implement this
    public void testHandleOutboundMessage() {}
}
