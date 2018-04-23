package com.sig.integration.coverity.executable

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals;

public class ExecutableTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testGetExecutableArgumentsEmpty() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();
        Executable executable = new Executable(workingDirectory, executablePath, Collections.emptyList());
        assertEquals(executablePath, executable.getExecutableArguments());
    }

    @Test
    public void testGetExecutableArguments() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(workingDirectory, executablePath, arguments);
        assertEquals(String.format("%s test --pa secretPassword things", executablePath), executable.getExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(workingDirectory, executablePath, arguments);
        assertEquals(String.format("%s --pa secretPassword test things", executablePath), executable.getExecutableArguments());
    }

    @Test
    public void testGetMaskedExecutableArgumentsEmpty() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();
        Executable executable = new Executable(workingDirectory, executablePath, Collections.emptyList());
        assertEquals(executablePath, executable.getMaskedExecutableArguments());
    }

    @Test
    public void testGetMaskedExecutableArguments() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(workingDirectory, executablePath, arguments);
        assertEquals(String.format("%s test --pa %s things", executablePath, Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(workingDirectory, executablePath, arguments);
        assertEquals(String.format("%s --pa %s test things", executablePath, Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());
    }

    @Test
    public void testCreateProcessBuilderEmpty() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();
        Executable executable = new Executable(workingDirectory, executablePath, Collections.emptyList());
        ProcessBuilder processBuilder = executable.createProcessBuilder();

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> arguments = Arrays.asList(executablePath);
        assertEquals(arguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilder() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();
        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(workingDirectory, executablePath, arguments);
        ProcessBuilder processBuilder = executable.createProcessBuilder();

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(executablePath, "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilder2() {
        File workingDirectory = temporaryFolder.newFolder();
        String executablePath = workingDirectory.getAbsolutePath();
        List<String> arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        Executable executable = new Executable(workingDirectory, executablePath, arguments);
        ProcessBuilder processBuilder = executable.createProcessBuilder();

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(executablePath, "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }
}
