package org.siouan.frontendgradleplugin.test.fixture;

import java.util.Set;

import org.siouan.frontendgradleplugin.domain.model.SystemProxySettings;

public final class SystemProxySettingsFixture {

    private SystemProxySettingsFixture() {
    }

    public static SystemProxySettings defaultSystemProxySettings() {
        return new SystemProxySettings(null, 80, null, 443, Set.of("localhost", "127.*", "[::1]"));
    }
}
