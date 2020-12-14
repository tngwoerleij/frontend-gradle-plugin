package org.siouan.frontendgradleplugin.infrastructure.gradle;

import static org.siouan.frontendgradleplugin.test.util.GradleBuildAssertions.assertTaskFailed;
import static org.siouan.frontendgradleplugin.test.util.GradleBuildAssertions.assertTaskSkipped;
import static org.siouan.frontendgradleplugin.test.util.GradleBuildAssertions.assertTaskSuccess;
import static org.siouan.frontendgradleplugin.test.util.GradleBuildAssertions.assertTaskUpToDate;
import static org.siouan.frontendgradleplugin.test.util.GradleBuildFiles.createBuildFile;
import static org.siouan.frontendgradleplugin.test.util.GradleHelper.runGradle;
import static org.siouan.frontendgradleplugin.test.util.GradleHelper.runGradleAndExpectFailure;
import static org.siouan.frontendgradleplugin.test.util.Resources.getResourcePath;
import static org.siouan.frontendgradleplugin.test.util.TaskTypes.buildNpmYarnTaskDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.siouan.frontendgradleplugin.FrontendGradlePlugin;
import org.siouan.frontendgradleplugin.test.util.FrontendMapBuilder;

/**
 * Functional tests to verify the {@link RunNode} task type, the {@link RunNpx} task type, the {@link RunNpmYarn} task
 * type in a Gradle build. This functional test relies on real Node.js and Yarn distributions.
 */
class NpmTaskTypeWithExitCodeFuncTest {

    private static final String RUN_NPM_DOC_OK_TASK_NAME = "myNpmDocOk";

    private static final String RUN_NPM_DOC_KO_TASK_NAME = "myNpmDocKo";

    @TempDir
    Path temporaryDirectoryPath;

    private Path projectDirectoryPath;

    @BeforeEach
    void setUp() {
        projectDirectoryPath = temporaryDirectoryPath;
    }

    @Test
    void shouldRunCustomTaskWithExitCode() throws IOException {
        final Path packageJsonDirectoryPath = Files.createDirectory(projectDirectoryPath.resolve("frontend"));
        Files.copy(getResourcePath("package-npm.json"), packageJsonDirectoryPath.resolve("package.json"));
        final FrontendMapBuilder frontendMapBuilder = new FrontendMapBuilder()
            .nodeVersion("14.15.1")
            .nodeInstallDirectory(projectDirectoryPath.resolve("node-dist"))
            .packageJsonDirectory(packageJsonDirectoryPath);
        final String runNpmDocOkTaskDefinition = buildNpmYarnTaskDefinition(RUN_NPM_DOC_OK_TASK_NAME,
            FrontendGradlePlugin.INSTALL_TASK_NAME, "run doc-ok");
        final String runNpmDocKoTaskDefinition = buildNpmYarnTaskDefinition(RUN_NPM_DOC_KO_TASK_NAME,
            FrontendGradlePlugin.INSTALL_TASK_NAME, "run doc-ko");
        createBuildFile(projectDirectoryPath, frontendMapBuilder.toMap(),
            String.join("\n", runNpmDocOkTaskDefinition, runNpmDocKoTaskDefinition));

        final BuildResult result5 = runGradle(projectDirectoryPath, RUN_NPM_DOC_OK_TASK_NAME);

        assertTaskSuccess(result5, FrontendGradlePlugin.NODE_INSTALL_TASK_NAME);
        assertTaskSkipped(result5, FrontendGradlePlugin.YARN_INSTALL_TASK_NAME);
        assertTaskSuccess(result5, FrontendGradlePlugin.INSTALL_TASK_NAME);
        assertTaskSuccess(result5, RUN_NPM_DOC_OK_TASK_NAME);

        final BuildResult result6 = runGradleAndExpectFailure(projectDirectoryPath, RUN_NPM_DOC_KO_TASK_NAME);

        assertTaskUpToDate(result6, FrontendGradlePlugin.NODE_INSTALL_TASK_NAME);
        assertTaskSkipped(result6, FrontendGradlePlugin.YARN_INSTALL_TASK_NAME);
        assertTaskSuccess(result6, FrontendGradlePlugin.INSTALL_TASK_NAME);
        assertTaskFailed(result6, RUN_NPM_DOC_KO_TASK_NAME);
    }
}
