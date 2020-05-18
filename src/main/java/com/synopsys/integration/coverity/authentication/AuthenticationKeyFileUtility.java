/**
 * coverity-common
 *
 * Copyright (c) 2020 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
