package com.synopsys.integration.coverity.executable

import com.synopsys.integration.coverity.exception.ExecutableException
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory

import java.nio.file.Path

import static org.junit.jupiter.api.Assertions.*

public class ExecutableTest {
    @Test
    @ExtendWith(TempDirectory.class)
    public void testConstructors(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile()

        Executable executable = new Executable(Collections.emptyList());
        assertEquals(System.getProperty("user.dir"), executable.workingDirectory.getAbsolutePath());
        assertTrue(executable.executableArguments.isEmpty());
        assertTrue(executable.environmentVariables.isEmpty());

        List<String> arguments = Arrays.asList("things");

        executable = new Executable(arguments);
        assertEquals(System.getProperty("user.dir"), executable.workingDirectory.getAbsolutePath());
        assertFalse(executable.executableArguments.isEmpty());
        assertTrue(executable.environmentVariables.isEmpty());

        executable = new Executable(arguments, workingDirectory);
        assertEquals(workingDirectory.getAbsolutePath(), executable.workingDirectory.getAbsolutePath());
        assertFalse(executable.executableArguments.isEmpty());
        assertTrue(executable.environmentVariables.isEmpty());

        Map<String, String> environment = new HashMap<>();
        environment.put("stuff", "things");
        executable = new Executable(arguments, workingDirectory, environment);
        assertEquals(workingDirectory.getAbsolutePath(), executable.workingDirectory.getAbsolutePath());
        assertFalse(executable.executableArguments.isEmpty());
        assertFalse(executable.environmentVariables.isEmpty());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testGetExecutableArgumentsEmpty(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();
        Executable executable = new Executable(Collections.emptyList(), workingDirectory);
        assertTrue(executable.executableArguments.isEmpty());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testGetExecutableArguments(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        assertEquals("test --pa secretPassword things", executable.getJoinedExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(arguments, workingDirectory);
        assertEquals("--pa secretPassword test things", executable.getJoinedExecutableArguments());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testGetMaskedExecutableArgumentsEmpty(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();
        Executable executable = new Executable(Collections.emptyList(), workingDirectory);
        assertTrue(StringUtils.isEmpty(executable.getMaskedExecutableArguments()));
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testGetMaskedExecutableArguments(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        assertEquals(String.format("test --pa %s things", Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(arguments, workingDirectory);
        assertEquals(String.format("--pa %s test things", Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testGetMaskedExecutableArgumentsMultiplePasswords(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things", "--password", "secret");
        Executable executable = new Executable(arguments, workingDirectory);
        try {
            executable.getMaskedExecutableArguments();
            Assert.fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

}
