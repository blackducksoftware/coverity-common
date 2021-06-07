/*
 * coverity-common
 *
 * Copyright (c) 2021 Synopsys, Inc.
 *
 * Use subject to the terms and conditions of the Synopsys End User Software License and Maintenance Agreement. All rights reserved worldwide.
 */
package com.synopsys.integration.coverity.executable;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.coverity.exception.ExecutableException;
import com.synopsys.integration.coverity.exception.ExecutableRunnerException;
import com.synopsys.integration.log.IntLogger;

public class ExecutableManager extends EnvironmentContributor {
    private final File coverityStaticAnalysisDirectory;

    public ExecutableManager(final File coverityStaticAnalysisDirectory) {
        this.coverityStaticAnalysisDirectory = coverityStaticAnalysisDirectory;
    }

    public int execute(final Executable executable, final IntLogger logger, final PrintStream standardOutput, final PrintStream errorOutput) throws InterruptedException, ExecutableException, ExecutableRunnerException {
        try {
            final ProcessBuilder processBuilder = createProcessBuilder(executable);
            logger.info(String.format("Running executable >%s", executable.getMaskedExecutableArguments(processBuilder.command())));
            final Process process = processBuilder.start();

            try (InputStream standardOutputStream = process.getInputStream(); InputStream standardErrorStream = process.getErrorStream()) {

                final ExecutableRedirectThread standardOutputThread = new ExecutableRedirectThread(standardOutputStream, standardOutput);
                standardOutputThread.start();

                final ExecutableRedirectThread errorOutputThread = new ExecutableRedirectThread(standardErrorStream, errorOutput);
                errorOutputThread.start();
                try {
                    final int returnCode = process.waitFor();
                    standardOutputThread.join();
                    errorOutputThread.join();

                    logger.info("Executable finished: " + returnCode);
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
        } catch (final InterruptedException | ExecutableException e) {
            throw e;
        } catch (final Exception e) {
            throw new ExecutableRunnerException(e);
        }
    }

    public ProcessBuilder createProcessBuilder(final Executable executable) throws ExecutableException {
        final List<String> processedExecutableArguments = executable.processExecutableArguments();
        addCoverityBinToArguments(processedExecutableArguments);

        final ProcessBuilder processBuilder = new ProcessBuilder(processedExecutableArguments);
        processBuilder.directory(executable.getWorkingDirectory());

        final Map<String, String> processBuilderEnvironment = processBuilder.environment();
        final Map<String, String> executableEnvironment = executable.getEnvironmentVariables();
        executableEnvironment.forEach((key, value) -> populateEnvironmentMap(processBuilderEnvironment, key, value));

        return processBuilder;
    }

    private void addCoverityBinToArguments(final List<String> arguments) throws ExecutableException {
        if (!coverityStaticAnalysisDirectory.isDirectory()) {
            throw new ExecutableException(String.format("The Coverity Static Analysis directory '%s' does not exist, or is not a directory.", coverityStaticAnalysisDirectory.getAbsolutePath()));
        }
        final File coverityBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        if (!coverityBinDirectory.isDirectory()) {
            throw new ExecutableException(String.format("The  Coverity Static Analysis bin directory '%s' does not exist, or is not a directory.", coverityBinDirectory.getAbsolutePath()));
        }
        if (!arguments.isEmpty()) {
            String toolPath = null;
            final String toolName = FilenameUtils.removeExtension(arguments.get(0).toLowerCase(Locale.ENGLISH));
            for (final File toolFile : coverityBinDirectory.listFiles()) {
                final String currentToolName = FilenameUtils.removeExtension(toolFile.getName());
                if (currentToolName.equalsIgnoreCase(toolName)) {
                    toolPath = toolFile.getAbsolutePath();
                }
            }
            if (StringUtils.isBlank(toolPath)) {
                throw new ExecutableException(String.format("The  Coverity Static Analysis bin directory '%s' does not contain a tool named '%s'.", coverityBinDirectory.getAbsolutePath(), toolName));
            }
            arguments.set(0, toolPath);
        }
    }

    public File getCoverityStaticAnalysisDirectory() {
        return coverityStaticAnalysisDirectory;
    }

}
