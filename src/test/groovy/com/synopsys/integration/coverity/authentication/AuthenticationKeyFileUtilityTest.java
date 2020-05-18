package com.synopsys.integration.coverity.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthenticationKeyFileUtilityTest {
    private final String expectedKey = "5789a068e7b82e7f76c03b410e847efe";
    private final Long expectedId = 10001L;
    private final String expectedType = "Coverity authentication key";
    private final String expectedUsername = "admin";
    private final String expectedDomain = "local";
    private final Integer expectedVersion = 2;
    private static Map<String, String> expectedComments = null;

    @BeforeAll
    public static void initializeExpectedComments() {
        if (expectedComments == null) {
            expectedComments = new HashMap<>();
            expectedComments.put("port", "1784");
            expectedComments.put("host", "localhost");
            expectedComments.put("description", "adminKeyFile");
            expectedComments.put("creationDate", "2020-05-14T16:14:57.171Z");
            expectedComments.put("ssl", "false");
            expectedComments.put("expirationDate", "2050-05-14T16:14:57.158Z");
        }
    }

    @Test
    public void testAuthenticationKeyFileFromStream() {
        AuthenticationKeyFileUtility authenticationKeyFileUtility = AuthenticationKeyFileUtility.defaultUtility();
        try (InputStream inputStream = getClass().getResourceAsStream("/AuthenticationKeyFileUtility/auth-key.txt")) {
            AuthenticationKeyFile authenticationKeyFile = authenticationKeyFileUtility.readAuthenticationKeyFile(inputStream);

            assertEquals(authenticationKeyFile.version, expectedVersion);
            assertEquals(authenticationKeyFile.key, expectedKey);
            assertEquals(authenticationKeyFile.id, expectedId);
            assertEquals(authenticationKeyFile.type, expectedType);
            assertEquals(authenticationKeyFile.username, expectedUsername);
            assertEquals(authenticationKeyFile.domain, expectedDomain);
            assertTrue(expectedComments.entrySet().containsAll(authenticationKeyFile.comments.entrySet()));
        } catch (IOException e) {
            fail("Exception occurred when trying to read test auth-key file", e);
        }
    }

    @Test
    public void testAuthenticationKeyFileFromPath() {
        AuthenticationKeyFileUtility authenticationKeyFileUtility = AuthenticationKeyFileUtility.defaultUtility();
        AuthenticationKeyFile authenticationKeyFile = null;
        try {
            Path authKeyFilePath = Paths.get(getClass().getResource("/AuthenticationKeyFileUtility/auth-key.txt").toURI());
            authenticationKeyFile = authenticationKeyFileUtility.readAuthenticationKeyFile(authKeyFilePath);
        } catch (URISyntaxException | IOException e) {
            fail("Exception occurred when trying to read test auth-key file", e);
        }

        assertEquals(authenticationKeyFile.version, expectedVersion);
        assertEquals(authenticationKeyFile.key, expectedKey);
        assertEquals(authenticationKeyFile.id, expectedId);
        assertEquals(authenticationKeyFile.type, expectedType);
        assertEquals(authenticationKeyFile.username, expectedUsername);
        assertEquals(authenticationKeyFile.domain, expectedDomain);
        assertTrue(expectedComments.entrySet().containsAll(authenticationKeyFile.comments.entrySet()));
    }

}
