package co.andrewbates.grade.rubric;

import java.nio.file.Path;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import co.andrewbates.grade.Main;
import co.andrewbates.grade.model.Submission;
import co.andrewbates.grade.sandbox.Sandbox;
import co.andrewbates.grade.sandbox.TestSandbox.CompileException;

public class CompileCriteria implements Criteria {
    JavaCompiler compiler;

    public CompileCriteria() {
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    public void grade(Submission submission) throws Exception {
        try {
            Path submissionPath = Main.database.getSubmissionPath(submission);
            Path testPath = Main.database.getTestPath(submission);
            Sandbox sandbox = new Sandbox(submissionPath, testPath);
            sandbox.compileFiles();
            submission.setScore(new Score("compile", 1, 1));
            sandbox.close();
        } catch (CompileException e) {
            submission.setScore(new Score("compile", 0, 1, e.getMessage()));
        }
    }
}
