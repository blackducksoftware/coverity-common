package com.sig.integration.coverity.executable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.sig.integration.coverity.exception.CoverityExecutableException;

public class Executable {
    public static final String MASKED_PASSWORD = "********";
    public static final String COVERITY_PASSWORD_ENVIRONMENT_VARIABLE = "COVERITY_PASSPHRASE";
    private final File workingDirectory;
    private final Map<String, String> environmentVariables = new HashMap<>();
    private final String executablePath;
    private final List<String> executableArguments = new ArrayList<>();

    public Executable(final File workingDirectory, final String executablePath, final List<String> executableArguments) {
        this.workingDirectory = workingDirectory;
        this.executablePath = executablePath;
        this.executableArguments.addAll(executableArguments);
    }

    public Executable(final File workingDirectory, final Map<String, String> environmentVariables, final String executablePath, final List<String> executableArguments) {
        this.workingDirectory = workingDirectory;
        this.environmentVariables.putAll(environmentVariables);
        this.executablePath = executablePath;
        this.executableArguments.addAll(executableArguments);
    }

    public ProcessBuilder createProcessBuilder() throws CoverityExecutableException {
        List<String> processedExecutableArguments = processExecutableArguments();
        final List<String> processBuilderArguments = createProcessBuilderArguments(processedExecutableArguments);
        final ProcessBuilder processBuilder = new ProcessBuilder(processBuilderArguments);
        processBuilder.directory(workingDirectory);
        final Map<String, String> processBuilderEnvironment = processBuilder.environment();
        final Map<String, String> systemEnv = System.getenv();
        for (final String key : systemEnv.keySet()) {
            populateEnvironmentMap(processBuilderEnvironment, key, systemEnv.get(key));
        }
        for (final String key : environmentVariables.keySet()) {
            populateEnvironmentMap(processBuilderEnvironment, key, environmentVariables.get(key));
        }
        return processBuilder;
    }

    public String getExecutableArguments() {
        return StringUtils.join(createProcessBuilderArguments(), ' ');
    }

    private List<String> createProcessBuilderArguments() {
        return createProcessBuilderArguments(executableArguments);
    }

    private List<String> createProcessBuilderArguments(List<String> executableArguments) {
        // ProcessBuilder can only be called with a List<java.lang.String> so do any needed conversion
        final List<String> processBuilderArguments = new ArrayList<>();
        processBuilderArguments.add(executablePath.toString());
        for (final String arg : executableArguments) {
            processBuilderArguments.add(arg.toString());
        }
        return processBuilderArguments;
    }

    public String getMaskedExecutableArguments() throws CoverityExecutableException {
        final List<String> arguments = new ArrayList<>(createProcessBuilderArguments());

        Optional<Integer> passwordIndex = getPasswordIndex(arguments);
        if (passwordIndex.isPresent()) {
            arguments.set(passwordIndex.get(), MASKED_PASSWORD);
        }
        return StringUtils.join(arguments, ' ');
    }

    private List<String> processExecutableArguments() throws CoverityExecutableException {
        // If the User provided the password as an argument, we want to set it as the environment variable of the process so it is not exposed when looking up the process
        // Passwords are provided using --password password OR --pa password
        List<String> processedExecutableArguments = new ArrayList<>(executableArguments);

        Optional<Integer> passwordIndex = getPasswordIndex(processedExecutableArguments);
        if (passwordIndex.isPresent()) {
            int indexToRemove = passwordIndex.get().intValue();
            String removedValue = processedExecutableArguments.remove(indexToRemove);
            //also remove the argument before the password
            processedExecutableArguments.remove(indexToRemove - 1);
            populateEnvironmentMap(environmentVariables, COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, removedValue);
        }
        return processedExecutableArguments;
    }

    private Optional<Integer> getPasswordIndex(List<String> list) throws CoverityExecutableException {
        // Passwords are provided using --password password OR --pa password
        Optional<Integer> passwordIndex = Optional.empty();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                final String currentArgument = list.get(i);
                if (currentArgument.equals("--password") || currentArgument.equals("--pa")) {
                    if (i + 1 <= list.size() - 1) {
                        if (passwordIndex.isPresent()) {
                            throw new CoverityExecutableException("Can not provide multiple password arguments.");
                        }
                        passwordIndex = Optional.of(i + 1);
                    }
                }
            }
        }
        return passwordIndex;
    }

    private Optional<String> getValueAtIndex(int index, List<String> list) {
        if (index < 0 || index > list.size()) {
            return Optional.empty();
        } else {
            return Optional.of(list.get(index));
        }
    }

    private void populateEnvironmentMap(final Map<String, String> environment, final String key, final String value) {
        // ProcessBuilder's environment's keys and values must be non-null java.lang.String's
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            environment.put(key, value);
        }
    }
}
