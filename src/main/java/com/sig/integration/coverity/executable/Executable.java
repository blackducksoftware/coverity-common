package com.sig.integration.coverity.executable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sig.integration.coverity.exception.ExecutableException;

public class Executable extends EnvironmentContributor {
    public static final String MASKED_PASSWORD = "********";
    public static final String COVERITY_PASSWORD_ENVIRONMENT_VARIABLE = "COVERITY_PASSPHRASE";
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

        Optional<Integer> passwordIndex = getPasswordIndex(arguments);
        if (passwordIndex.isPresent()) {
            arguments.set(passwordIndex.get(), MASKED_PASSWORD);
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
        List<String> processedExecutableArguments = new ArrayList<>(getExecutableArguments());

        Optional<Integer> passwordIndex = getPasswordIndex(processedExecutableArguments);
        if (passwordIndex.isPresent()) {
            int indexToRemove = passwordIndex.get().intValue();
            String removedValue = processedExecutableArguments.remove(indexToRemove);
            //also remove the argument before the password
            processedExecutableArguments.remove(indexToRemove - 1);
            populateEnvironmentMap(getEnvironmentVariables(), COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, removedValue);
        }
        return processedExecutableArguments;
    }

    private Optional<Integer> getPasswordIndex(List<String> list) throws ExecutableException {
        // Passwords are provided using --password password OR --pa password
        Optional<Integer> passwordIndex = Optional.empty();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                final String currentArgument = list.get(i);
                if (currentArgument.equals("--password") || currentArgument.equals("--pa")) {
                    if (i + 1 <= list.size() - 1) {
                        if (passwordIndex.isPresent()) {
                            throw new ExecutableException("Can not provide multiple password arguments.");
                        }
                        passwordIndex = Optional.of(i + 1);
                    }
                }
            }
        }
        return passwordIndex;
    }

}
