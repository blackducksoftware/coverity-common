package com.synopsys.integration.coverity

import com.synopsys.integration.coverity.CoverityServerVerifier
import com.synopsys.integration.coverity.exception.CoverityIntegrationException
import com.synopsys.integration.coverity.ws.WebServiceFactory
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

class CoverityServerVerifierTest {

    private final MockWebServer server = new MockWebServer();

    @Before
    public void setUp() throws Exception {
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testNotCoverityServer() {
        setResponse(new MockResponse().setResponseCode(404))
        CoverityServerVerifier coverityServerVerifier = new CoverityServerVerifier()
        try {
            coverityServerVerifier.verifyIsCoverityServer(server.url(WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL).url())
            fail("Should have thrown an exception")
        } catch (CoverityIntegrationException e) {
            assertTrue(e.getMessage().contains("The Url does not appear to be a Coverity server"))
        }
    }

    @Test
    public void testIsCoverityServer() {
        setResponse(new MockResponse().setResponseCode(200))
        CoverityServerVerifier coverityServerVerifier = new CoverityServerVerifier()
        coverityServerVerifier.verifyIsCoverityServer(server.url(WebServiceFactory.CONFIGURATION_SERVICE_V9_WSDL).url())
    }


    private void setResponse(MockResponse response) {
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                response
            }
        };
        server.setDispatcher(dispatcher);
    }
}
