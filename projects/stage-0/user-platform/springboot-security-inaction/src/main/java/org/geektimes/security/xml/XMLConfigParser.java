package org.geektimes.security.xml;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * parse mybatis-config.xml {@link XMLConfigBuilder}
 */
public class XMLConfigParser extends BaseBuilder {

    private final Logger logger = LoggerFactory.getLogger(XMLConfigParser.class.getName());

    // environments node
    private final Environments environments = new Environments();

    private boolean parsed;
    private final XPathParser parser;
    private String environment;
    private final ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();

    public XMLConfigParser(InputStream inputStream) {
        this(inputStream, null, null);
    }

    public XMLConfigParser(InputStream inputStream, String environment) {
        this(inputStream, environment, null);
    }

    public XMLConfigParser(InputStream inputStream, String environment, Properties props) {
        this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    private XMLConfigParser(XPathParser parser, String environment, Properties props) {
        super(new Configuration());
        ErrorContext.instance().resource("SQL Mapper Configuration");
        this.configuration.setVariables(props);
        this.parsed = false;
        this.environment = environment;
        this.parser = parser;
    }

    public Configuration parse() {
        if (parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        }
        parsed = true;
        parseConfiguration(parser.evalNode("/configuration"));
        return configuration;
    }

    private void parseConfiguration(XNode root) {
        try {
            environmentsElement(root.evalNode("environments"));
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }

    private void environmentsElement(XNode context) {
        if (context == null) return;

        if (environment == null) {
            environment = context.getStringAttribute("default");
            environments.setDef(environment);
        }

        for (XNode child : context.getChildren()) {
            environments.addEnvironment(parseEnvironment(child));
        }
    }

    /**
     *         <environment id="dev">
     *             <transactionManager type="JDBC"/>
     *             <dataSource type="POOLED">
     *                 <property name="driver" value="org.h2.Driver" />
     *                 <property name="url" value="jdbc:h2:~/test" />
     *                 <property name="username" value="sa" />
     *                 <property name="password" value="sa" />
     *             </dataSource>
     *         </environment>
     * @param context
     * @return
     */
    private Environments.Environment parseEnvironment(XNode context) {
        Environments.Environment environment = new Environments.Environment();
        environment.setId(context.getStringAttribute("id"));

        Properties props = getDatasourceProperties(context);
        Environments.DataSourceConfig dataSourceConfig = Environments.DataSourceConfig.from(props);
        environment.setDataSource(dataSourceConfig);

        return environment;
    }

    private Properties getDatasourceProperties(XNode context) {
        XNode dataSourceNode = context.evalNode("dataSource");
        String type = dataSourceNode.getStringAttribute("type");
        Properties props = dataSourceNode.getChildrenAsProperties();
        props.put("type", type);
        return props;
    }

    /**
     * 获取数据库配置
     * @return
     */
    public Environments.DataSourceConfig getDataSourceConfig() {
        return environments.getDataSourceConfig();
    }

}
