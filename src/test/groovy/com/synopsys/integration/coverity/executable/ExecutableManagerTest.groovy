package com.synopsys.integration.coverity.executable

import com.synopsys.integration.coverity.exception.ExecutableException
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.*

class ExecutableManagerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File coverityStaticAnalysisDirectory;
    private File coverityStaticAnalysisBinDirectory;

    @Before
    public void setup() {
        File directory = temporaryFolder.newFolder();
        coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();
    }

    @Test
    public void testConstructor() {
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        assertEquals(coverityStaticAnalysisDirectory, executableManager.getCoverityStaticAnalysisDirectory());
    }

    @Test
    public void testAddCoverityBinToPathDirectoryDoesntExist() {
        File directory = temporaryFolder.newFolder();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        try {
            executableManager.addCoverityBinToArguments(new ArrayList<String>());
            Assert.fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testAddCoverityBinToPathBinDoesntExist() {
        File directory = temporaryFolder.newFolder();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        coverityStaticAnalysisDirectory.mkdirs();
        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        try {
            executableManager.addCoverityBinToArguments(new ArrayList<String>());
            Assert.fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testAddCoverityBinToPathNoArguments() {
        File directory = temporaryFolder.newFolder();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        File coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        List<String> arguments = new ArrayList<String>();
        executableManager.addCoverityBinToArguments(arguments);
        assertTrue(arguments.isEmpty());
    }

    @Test
    public void testAddCoverityBinToPathWithArgument() {
        File directory = temporaryFolder.newFolder();
        File coverityStaticAnalysisDirectory = new File(directory, "coverity-analysis");
        File coverityStaticAnalysisBinDirectory = new File(coverityStaticAnalysisDirectory, "bin");
        coverityStaticAnalysisBinDirectory.mkdirs();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);
        List<String> arguments = new ArrayList<String>();
        arguments.add("test");

        executableManager.addCoverityBinToArguments(arguments);
        assertEquals(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", arguments.get(0));
    }

    @Test
    public void testCreateProcessBuilderEmpty() {
        File workingDirectory = temporaryFolder.newFolder();

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
    public void testCreateProcessBuilder() {
        File workingDirectory = temporaryFolder.newFolder();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilder2() {
        File workingDirectory = temporaryFolder.newFolder();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        Executable executable = new Executable(arguments, workingDirectory);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilderEnvironmentPassword() {
        File workingDirectory = temporaryFolder.newFolder();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("test", "things");
        Map<String, String> preEnvironment = new HashMap<>();
        preEnvironment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");

        Executable executable = new Executable(arguments, workingDirectory, preEnvironment);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilderEnvironmentPasswordOverriden() {
        File workingDirectory = temporaryFolder.newFolder();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("--pa", "secretPassword", "test", "things");
        Map<String, String> preEnvironment = new HashMap<>();
        preEnvironment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "password");

        Executable executable = new Executable(arguments, workingDirectory, preEnvironment);
        ProcessBuilder processBuilder = executableManager.createProcessBuilder(executable);

        assertEquals(workingDirectory, processBuilder.directory());

        List<String> expectedArguments = Arrays.asList(coverityStaticAnalysisBinDirectory.getAbsolutePath() + File.separator + "test", "things");
        assertEquals(expectedArguments, processBuilder.command());

        Map<String, String> environment = new HashMap<>();
        environment.putAll(System.getenv());
        environment.put(Executable.COVERITY_PASSWORD_ENVIRONMENT_VARIABLE, "secretPassword");
        assertEquals(environment, processBuilder.environment());
    }

    @Test
    public void testCreateProcessBuilderMultiplePasswords() {
        File workingDirectory = temporaryFolder.newFolder();

        ExecutableManager executableManager = new ExecutableManager(coverityStaticAnalysisDirectory);

        List<String> arguments = Arrays.asList("test", "--pa", "secretPassword", "things", "--password", "secret");
        Executable executable = new Executable(arguments, workingDirectory);
        try {
            executableManager.createProcessBuilder(executable);
            Assert.fail("Should have thrown an exception");
        } catch (ExecutableException e) {
            assertNotNull(e);
        }
    }

}
