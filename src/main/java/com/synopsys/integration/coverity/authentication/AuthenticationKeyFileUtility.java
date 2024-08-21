/*
 * coverity-common
 *
 * Copyright (c) 2024 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

public class AuthenticationKeyFileUtility {
    private final Gson gson;

    public static AuthenticationKeyFileUtility defaultUtility() {
        return new AuthenticationKeyFileUtility(new Gson());
    }

    public AuthenticationKeyFileUtility(final Gson gson) {
        this.gson = gson;
    }

    public AuthenticationKeyFile readAuthenticationKeyFile(final Path pathToAuthenticationKeyFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(pathToAuthenticationKeyFile)) {
            return gson.fromJson(reader, AuthenticationKeyFile.class);
        }
    }

    public AuthenticationKeyFile readAuthenticationKeyFile(final InputStream authenticationKeyFileContents) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(authenticationKeyFileContents)) {
            return gson.fromJson(reader, AuthenticationKeyFile.class);
        }
    }

}
