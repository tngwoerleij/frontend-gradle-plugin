package org.siouan.frontendgradleplugin.domain.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.siouan.frontendgradleplugin.domain.provider.FileManager;

@ExtendWith(MockitoExtension.class)
class GetNpxExecutablePathTest {

    @Mock
    private FileManager fileManager;

    @InjectMocks
    private GetNpxExecutablePath usecase;

    @Test
    void shouldReturnTwoExecutableWhenOsIsWindows() {
        assertThat(usecase.getWindowsRelativeExecutablePath()).isEqualTo(Paths.get("npx.cmd"));

        verifyNoMoreInteractions(fileManager);
    }

    @Test
    void shouldReturnTwoExecutableWhenOsIsNotWindows() {
        assertThat(usecase.getNonWindowsRelativeExecutablePath()).isEqualTo(Paths.get("bin", "npx"));

        verifyNoMoreInteractions(fileManager);
    }
}
