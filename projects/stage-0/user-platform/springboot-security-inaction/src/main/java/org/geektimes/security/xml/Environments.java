package org.geektimes.security.xml;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 *     <environments default="dev">
 *         <environment id="dev">
 *             <transactionManager type="JDBC"/>
 *             <dataSource type="POOLED">
 *                 <property name="driver" value="org.h2.Driver" />
 *                 <property name="url" value="jdbc:h2:~/test" />
 *                 <property name="username" value="sa" />
 *                 <property name="password" value="sa" />
 *             </dataSource>
 *         </environment>
 *     </environments>
 */
@Data
@NoArgsConstructor
public class Environments {

    private String def;

    private List<Environment> environmentList = new ArrayList<>();

    public void addEnvironment(Environment env) {
        this.environmentList.add(env);
    }

    public DataSourceConfig getDataSourceConfig() {
        if (environmentList == null || environmentList.isEmpty()) {
            return null;
        }

        Optional<Environment> optionalEnv = environmentList.stream().filter(env -> env.getId().equals(def)).findAny();
        return optionalEnv.map(Environment::getDataSource).orElse(null);
    }

    @Data
    @NoArgsConstructor
    public static class Environment {

        private String id;
        private TransactionManagerConfig transactionManager;
        private DataSourceConfig dataSource;

    }

    @Data
    @NoArgsConstructor
    public static class TransactionManagerConfig {

        private String type;

    }

    @Data
    @NoArgsConstructor
    public static class DataSourceConfig {

        private String type;
        private String driver;
        private String url;
        private String username;
        private String password;

        /**
         * 可以使用 {@link PropertyDescriptor#writeMethodRef} 来简化实现
         * @param props
         * @return
         */
        public static DataSourceConfig from(Properties props) {
            DataSourceConfig dataSourceConfig = new DataSourceConfig();
            dataSourceConfig.setType(props.getProperty("type"));
            dataSourceConfig.setDriver(props.getProperty("driver"));
            dataSourceConfig.setUrl(props.getProperty("url"));
            dataSourceConfig.setUsername(props.getProperty("username"));
            dataSourceConfig.setPassword(props.getProperty("password"));
            return dataSourceConfig;
        }
    }

}
