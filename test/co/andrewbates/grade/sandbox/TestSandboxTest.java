package co.andrewbates.grade.sandbox;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

import co.andrewbates.grade.sandbox.TestSandbox.CompileException;

public class TestSandboxTest {
    File studentsDir = Paths.get(System.getProperty("user.dir"), "testdata", "students").toFile();
    File testDir = Paths.get(System.getProperty("user.dir"), "testdata", "criteria").toFile();

    @Test
    public void testRunTests() throws FileNotFoundException, IOException {
        for (File studentDir : studentsDir.listFiles()) {
            Properties testProps = new Properties();
            Path propsPath = Paths.get(studentDir.getAbsolutePath(), "test.properties");
            testProps.load(new FileInputStream(propsPath.toFile()));
            TestSandbox sandbox = new TestSandbox(studentDir, testDir);
            try {
                List<Failure> failures = sandbox.runTests();
                if (failures.size() > 0 && testProps.getProperty("testsPass").equals("true")) {
                    StringBuilder message = new StringBuilder();
                    for (Failure failure : failures) {
                        message.append(failure + "\n");
                    }
                    fail("Expected tests to pass for " + studentDir.getName() + "\n" + message);
                } else if (failures.size() == 0 && !"true".equals(testProps.getProperty("testsPass"))) {
                    fail("Expected tests to fail for " + studentDir.getName());
                }

                if ("true".equals(testProps.getProperty("securityViolation"))) {
                    System.err.println(failures);
                    fail("Expected Security Exception for " + studentDir.getName());
                }
            } catch (SecurityException e) {
                if (!"true".equals(testProps.getProperty("securityViolation"))) {
                    fail("Test failed for " + studentDir.getName() + ":\n" + e);
                }
            } catch (CompileException e) {
                if ("true".equals(testProps.getProperty("compiles"))) {
                    fail("Expected tests to compile for " + studentDir.getName() + ":\n" + e.getMessage());
                }
            } catch (InitializationError e) {
                fail("Test failed for " + studentDir.getName() + ": " + e.getMessage());
            } finally {
                sandbox.close();
            }
        }
    }

}
