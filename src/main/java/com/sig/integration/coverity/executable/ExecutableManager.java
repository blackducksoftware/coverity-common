/**
 * coverity-common
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
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
package com.sig.integration.coverity.executable;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import com.blackducksoftware.integration.log.IntLogger;
import com.sig.integration.coverity.exception.ExecutableException;
import com.sig.integration.coverity.exception.ExecutableRunnerException;

public class ExecutableManager extends EnvironmentContributor {
    private final File coverityStaticAnalysisDirectory;

    public ExecutableManager(File coverityStaticAnalysisDirectory) {
        this.coverityStaticAnalysisDirectory = coverityStaticAnalysisDirectory;
    }

    public int execute(final Executable executable, IntLogger logger, PrintStream standardOutput, PrintStream errorOutput) throws InterruptedException, ExecutableException, ExecutableRunnerException {
        logger.info(String.format("Running executable >%s", executable.getMaskedExecutableArguments()));
        try {
            final ProcessBuilder processBuilder = createProcessBuilder(executable);
            final Process process = processBuilder.start();

            try (InputStream standardOutputStream = process.getInputStream(); InputStream standardErrorStream = process.getErrorStream()) {

                final ExecutableRedirectThread standardOutputThread = new ExecutableRedirectThread(standardOutputStream, standardOutput);
                standardOutputThread.start();

                final ExecutableRedirectThread errorOutputThread = new ExecutableRedirectThread(standardErrorStream, errorOutput);
                errorOutputThread.start();
                try {
                    final int returnCode = process.waitFor();
                    logger.info("Executable finished: " + returnCode);

                    standardOutputThread.join();
                    errorOutputThread.join();

                    return returnCode;
                } finally {
                    // All processes and threads should be closed before we leave
                    if (process.isAlive()) {
                        process.destroy();
                    }
                    if (standardOutputThread.isAlive()) {
                        standardOutputThread.interrupt();
                    }
                    if (errorOutputThread.isAlive()) {
                        errorOutputThread.interrupt();
                    }
                }
            }
        } catch (InterruptedException e) {
            throw e;
        } catch (final Exception e) {
            throw new ExecutableRunnerException(e);
        }
    }

    public ProcessBuilder createProcessBuilder(Executable executable) throws ExecutableException {
        List<String> processedExecutableArguments = executable.processExecutableArguments();
        addCoverityBinToArguments(processedExecutableArguments);
        final ProcessBuilder processBuilder = new ProcessBuilder(processedExecutableArguments);
        processBuilder.directory(executable.getWorkingDirectory());
        final Map<String, String> processBuilderEnvironment = processBuilder.environment();
        final Map<String, String> executableEnvironment = executable.getEnvironmentVariables();
        for (final String key : executableEnvironment.keySet()) {
            populateEnvironmentMap(processBuilderEnvironment, key, executableEnvironment.get(key));
        }
        return processBuilder;
    }

    private void addCoverityBinToArguments(List<String> arguments) throws ExecutableException {
        if (!coverityStaticAnalysisDirectory.isDirectory()) {
            throw new ExecutableException("The Coverity Static Analysis directory provided does not exist, or is not a directory.");
        }
        File coverityBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        if (!coverityBinDirectory.isDirectory()) {
            throw new ExecutableException("The bin directory does not exist in the Coverity Static Analysis directory provided, or it is not a directory.");
        }
        if (!arguments.isEmpty()) {
            arguments.set(0, coverityBinDirectory.getAbsolutePath() + File.separator + arguments.get(0));
        }
    }

    public File getCoverityStaticAnalysisDirectory() {
        return coverityStaticAnalysisDirectory;
    }

}
