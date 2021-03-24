package org.geektimes.configuration.microprofile.config.source.filter;

import org.eclipse.microprofile.config.Config;

public class ServletConfigThreadLocal {

    private static ThreadLocal<Config> threadLocal = new ThreadLocal<>();

    public static void put(Config config) {
        threadLocal.set(config);
    }

    public static Config get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
