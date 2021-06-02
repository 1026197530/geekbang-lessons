package org.geekbang.projects.springcloud.configserver.file.config;

import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;

/**
 * An {@link EnvironmentRepository} for local file.
 */
@Component
public class FileEnvironmentRepository implements EnvironmentRepository {

    @Override
    public Environment findOne(String application, String profile, String label) {
        return new Environment(application, new String[] { profile }, label, null, null);
    }

}
