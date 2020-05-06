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
package com.synopsys.integration.coverity.executable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.coverity.exception.ExecutableException;

public class Executable extends EnvironmentContributor {
    public static final String MASKED_PASSWORD = "********";
    private final File workingDirectory;
    private final Map<String, String> environmentVariables = new HashMap<>();
    private final List<String> executableArguments = new ArrayList<>();

    public Executable(final List<String> executableArguments) {
        this(executableArguments, new File(System.getProperty("user.dir")));
    }

    public Executable(final List<String> executableArguments, final File workingDirectory) {
        this(executableArguments, workingDirectory, Collections.emptyMap());
    }

    public Executable(final List<String> executableArguments, final File workingDirectory, final Map<String, String> environmentVariables) {
        this.workingDirectory = workingDirectory;
        this.executableArguments.addAll(executableArguments);
        this.environmentVariables.putAll(environmentVariables);
    }

    public String getJoinedExecutableArguments() {
        return StringUtils.join(getExecutableArguments(), ' ');
    }

    public List<String> getExecutableArguments() {
        return executableArguments;
    }

    public String getMaskedExecutableArguments() throws ExecutableException {
        final List<String> arguments = new ArrayList<>(getExecutableArguments());
        return getMaskedExecutableArguments(arguments);
    }

    public String getMaskedExecutableArguments(final List<String> arguments) throws ExecutableException {
        int passwordIndex = getPasswordIndex(arguments);

        if (passwordIndex != -1) {
            arguments.set(passwordIndex, MASKED_PASSWORD);
        }

        return StringUtils.join(arguments, ' ');
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public List<String> processExecutableArguments() throws ExecutableException {
        // If the User provided the password as an argument, we want to set it as the environment variable of the process so it is not exposed when looking up the process
        // Passwords are provided using --password password OR --pa password
        final List<String> processedExecutableArguments = new ArrayList<>(getExecutableArguments());

        final int passwordIndex = getPasswordIndex(processedExecutableArguments);
        if (passwordIndex != -1) {
            final String removedValue = processedExecutableArguments.remove(passwordIndex);
            //also remove the argument before the password
            processedExecutableArguments.remove(passwordIndex - 1);
            populateEnvironmentMap(getEnvironmentVariables(), CoverityToolEnvironmentVariable.PASSPHRASE, removedValue);
        }
        return processedExecutableArguments;
    }

    private int getPasswordIndex(final List<String> list) throws ExecutableException {
        // Passwords are provided using --password password OR --pa password
        int passwordIndex = -1;
        for (int i = 1; i < list.size(); i++) {
            final String lastArgument = list.get(i - 1);
            if (lastArgument.equals("--password") || lastArgument.equals("--pa")) {
                if (passwordIndex != -1) {
                    throw new ExecutableException("Cannot provide multiple password arguments.");
                }
                passwordIndex = i;
            }
        }
        return passwordIndex;
    }

}
