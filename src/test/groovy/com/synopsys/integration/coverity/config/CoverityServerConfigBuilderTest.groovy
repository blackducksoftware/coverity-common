package com.synopsys.integration.coverity.config

import com.synopsys.integration.log.SilentIntLogger
import com.synopsys.integration.rest.client.ConnectionResult
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CoverityServerConfigBuilderTest {
    private final MockWebServer server = new MockWebServer();

    @BeforeEach
    public void setUp() throws Exception {
        server.start();
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testGoodCoverityServerConfig() {
        setResponse(new MockResponse().setResponseCode(200))

        CoverityServerConfig serverConfig = CoverityServerConfig.newBuilder()
                .setUrl(server.url('/some/path').url().toString())
                .setUsername('someUsername')
                .setPassword('somePassword')
                .build()

        ConnectionResult connectionResult = serverConfig.attemptConnection(new SilentIntLogger())
        Assertions.assertTrue(connectionResult.isSuccess())
    }

    @Test
    public void testBadUrlCoverityServerConfig() {
        setResponse(new MockResponse().setResponseCode(404))

        CoverityServerConfigBuilder serverConfigBuilder = CoverityServerConfig.newBuilder()
                .setUrl(server.url('/bad/path').url().toString())
                .setUsername('someUsername')
                .setPassword('somePassword')

        Assertions.assertThrows(IllegalArgumentException.class, { serverConfigBuilder.build() })
    }

    @Test
    public void testBadCredentialsCoverityServerConfig() {
        setResponse(new MockResponse().setResponseCode(200))

        CoverityServerConfig serverConfig = CoverityServerConfig.newBuilder()
                .setUrl(server.url('/some/path').url().toString())
                .setUsername('badusername')
                .setPassword('badPassword')
                .build()

        setResponse(new MockResponse().setResponseCode(401))

        ConnectionResult connectionResult = serverConfig.attemptConnection(new SilentIntLogger())
        Assertions.assertTrue(connectionResult.isFailure())
    }

    @Test
    public void testMissingUsernameCoverityServerConfig() {
        setResponse(new MockResponse().setResponseCode(200))

        CoverityServerConfigBuilder serverConfigBuilder = CoverityServerConfig.newBuilder()
                .setUrl(server.url('/some/path').url().toString())
                .setPassword('somePassword')

        Assertions.assertThrows(IllegalArgumentException.class, { serverConfigBuilder.build() })
    }

    @Test
    public void testMissingPasswordCoverityServerConfig() {
        setResponse(new MockResponse().setResponseCode(200))

        CoverityServerConfigBuilder serverConfigBuilder = CoverityServerConfig.newBuilder()
                .setUrl(server.url('/some/path').url().toString())
                .setUsername('someUsername')

        Assertions.assertThrows(IllegalArgumentException.class, { serverConfigBuilder.build() })
    }

    @Test
    public void testMissingUrlCoverityServerConfig() {
        CoverityServerConfigBuilder serverConfigBuilder = CoverityServerConfig.newBuilder()
                .setUsername('someUsername')
                .setPassword('somePassword')

        Assertions.assertThrows(IllegalArgumentException.class, { serverConfigBuilder.build() })
    }

    private void setResponse(MockResponse response) {
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return response;
            }
        }
        server.setDispatcher(dispatcher);
    }

}
