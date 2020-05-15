package com.synopsys.integration.coverity.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

public class AuthenticationKeyFileUtility {
    private final Gson gson;

    public AuthenticationKeyFileUtility(final Gson gson) {
        this.gson = gson;
    }

    public AuthenticationKeyFile readAuthenticationKeyFile(final Path pathToAuthenticationKeyFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(pathToAuthenticationKeyFile)) {
            return gson.fromJson(reader, AuthenticationKeyFile.class);
        }
    }

}
