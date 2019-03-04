package com.synopsys.integration.coverity.executable

import com.synopsys.integration.coverity.exception.ExecutableException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory

import java.nio.file.Path

import static org.junit.jupiter.api.Assertions.*

class ExecutableManagerTest {
    private File coverityStaticAnalysisDirectory;
    private File coverityStaticAnalysisBinDirectory;

    @BeforeEach
    public void setup(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile()
        coverityStaticAnalysisDirectory = new File(directory, "common-coverity-analysis");
        coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        File coverityTool = new File(coverityStaticAnalysisBinDirectory, "tool");
        coverityTool.createNewFile();
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testConstructor() {
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        assertEquals(coverityStaticAnalysisDirectory, executableManager.getCoverityStaticAnalysisDirectory());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testAddCoverityBinToPathDirectoryDoesntExist(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        try {
            executableManager.addCoverityBinToArguments(new ArrayList<String>());
            fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testAddCoverityBinToPathBinDoesntExist(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        coverityStaticAnalysisDirectory.mkdirs();
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        try {
            executableManager.addCoverityBinToArguments(new ArrayList<String>());
            fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testAddCoverityBinToPathNoArguments(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        File coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        List<String> arguments = new ArrayList<String>();
        executableManager.addCoverityBinToArguments(arguments);
        assertTrue(arguments.isEmpty());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testAddCoverityBinToPathWithArgumentToolDoenstExist(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        File coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        List<String> arguments = new ArrayList<String>();
        arguments.add("test");
        try {
            executableManager.addCoverityBinToArguments(arguments);
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testAddCoverityBinToPathWithArgument(@TempDirectory.TempDir Path workingDirectoryPath) {
        File directory = workingDirectoryPath.toFile();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        File coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        File coverityTool = new File(coverityStaticAnalysisBinDirectory, "test");
        coverityTool.createNewFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        List<String> arguments = new ArrayList<String>();
        arguments.add("test");

        executableManager.addCoverityBinToArguments(arguments);
        assertEquals(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", arguments.get(0));
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilderEmpty(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        Executable executable = new Executable(Collections.emptyList(), workingDirectory);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        assertEquals(Collections.emptyList(), processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilder(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("tool", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "tool", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilder2(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("tool", "things", "--pa", "secretPassword");
        Executable executable = new Executable(arguments, workingDirectory);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "tool", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilderEnvironmentPassword(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("tool", "things");
        Map<String, String> preEnvironment = new HashMap<>();
        preEnvironment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");

        Executable executable = new Executable(arguments, workingDirectory, preEnvironment);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "tool", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilderEnvironmentPasswordOverriden(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("tool", "--pa", "secretPassword", "things");
        Map<String, String> preEnvironment = new HashMap<>();
        preEnvironment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");

        Executable executable = new Executable(arguments, workingDirectory, preEnvironment);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "tool", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    @ExtendWith(TempDirectory.class)
    public void testCreateProcessBuilderMultiplePasswords(@TempDirectory.TempDir Path workingDirectoryPath) {
        File workingDirectory = workingDirectoryPath.toFile();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("tool", "--pa", "secretPassword", "things", "--password", "secret");
        Executable executable = new Executable(arguments, workingDirectory);
        try {
            executableManager.createProcessBuilder(executable);
            fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

}
