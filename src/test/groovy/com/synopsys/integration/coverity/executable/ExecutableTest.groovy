package com.sig.integration.coverity.executable

import com.sig.integration.coverity.exception.ExecutableException
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

public class ExecutableTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testConstructors() {
        File workingDirectory = temporaryFolder.newFolder();

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
    public void testGetExecutableArgumentsEmpty() {
        File workingDirectory = temporaryFolder.newFolder();
        Executable executable = new Executable(Collections.emptyList(), workingDirectory);
        assertTrue(executable.executableArguments.isEmpty());
    }

    @Test
    public void testGetExecutableArguments() {
        File workingDirectory = temporaryFolder.newFolder();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        assertEquals("test --pa secretPassword things", executable.getJoinedExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(arguments, workingDirectory);
        assertEquals("--pa secretPassword test things", executable.getJoinedExecutableArguments());
    }

    @Test
    public void testGetMaskedExecutableArgumentsEmpty() {
        File workingDirectory = temporaryFolder.newFolder();
        Executable executable = new Executable(Collections.emptyList(), workingDirectory);
        assertTrue(StringUtils.isEmpty(executable.getMaskedExecutableArguments()));
    }

    @Test
    public void testGetMaskedExecutableArguments() {
        File workingDirectory = temporaryFolder.newFolder();

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        assertEquals(String.format("test --pa %s things", Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());

        arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        executable = new Executable(arguments, workingDirectory);
        assertEquals(String.format("--pa %s test things", Executable.MASKED_PASSWORD), executable.getMaskedExecutableArguments());
    }

    @Test
    public void testGetMaskedExecutableArgumentsMultiplePasswords() {
        File workingDirectory = temporaryFolder.newFolder();

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
