package org.geekbang.projects.springcloud.configserver.file.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.geekbang.projects.springcloud.configserver.file.redis.RedisConfigProperties;
import org.geekbang.projects.springcloud.configserver.file.redis.RedissLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis配置连接lettuce工厂
 * 可支持多数剧源配置
 * @author  wangyongfei
 */
@Configuration
public class RedissonAutoConfiguration {

        @Autowired
        private Environment environment;

        @Autowired
        RedisConfigProperties redisConfigProperties;

        @Bean
        @ConfigurationProperties(prefix = "spring.redis.cluster.lettuce.pool")
        public GenericObjectPoolConfig redisLettucePool(){
                return new GenericObjectPoolConfig();
        }

        @Bean(name = "redisClusterConfig")
        public RedisClusterConfiguration redisClusterConfiguration(){
                Map<String,Object> source=new HashMap<String,Object>();
                source.put("spring.redis.cluster.nodes",environment.getProperty("spring.redis.cluster.nodes"));
                return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration",source));
        }

        @Bean(name = "lettuceConnectionFactory")
        public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig,@Qualifier("redisClusterConfig") RedisClusterConfiguration redisClusterConfiguration){
                LettucePoolingClientConfiguration build = LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
                return new LettuceConnectionFactory(redisClusterConfiguration,build);
        }

        @Bean
        public RedisTemplate redisTemplate(@Qualifier("lettuceConnectionFactory") RedisConnectionFactory redisConnectionFactory){
                return getRedisTemplate(redisConnectionFactory);
        }


        private RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(redisConnectionFactory);
                Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
                ObjectMapper om = new ObjectMapper();
                om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
                om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
                jackson2JsonRedisSerializer.setObjectMapper(om);

                StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                // key采用String的序列化方式
                template.setKeySerializer(stringRedisSerializer);
                // hash的key也采用String的序列化方式
                template.setHashKeySerializer(stringRedisSerializer);
                // value序列化方式采用jackson
                template.setValueSerializer(jackson2JsonRedisSerializer);
                // hash的value序列化方式采用jackson
                template.setHashValueSerializer(jackson2JsonRedisSerializer);
                template.afterPropertiesSet();
                return template;
        }


        @Bean
        public Redisson redisson() {
                List<String> clusterNodes = new ArrayList<>();
                for (int i = 0; i < redisConfigProperties.getCluster().getNodes().size(); i++) {
                        clusterNodes.add("redis://" + redisConfigProperties.getCluster().getNodes().get(i));
                }
                Config config = new Config();
                ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(clusterNodes.toArray(new String[clusterNodes.size()]))
                    .setMasterConnectionMinimumIdleSize(redisConfigProperties.getMasterConnectionMinimumIdleSize())
                    .setMasterConnectionPoolSize(redisConfigProperties.getMasterConnectionPoolSize())
                    .setSlaveConnectionMinimumIdleSize(redisConfigProperties.getSlaveConnectionMinimumIdleSize())
                    .setSlaveConnectionPoolSize(redisConfigProperties.getSlaveConnectionPoolSize());
                if(StringUtils.isNotBlank(redisConfigProperties.getPassword())){
                        clusterServersConfig.setPassword(redisConfigProperties.getPassword());
                }
                return (Redisson) Redisson.create(config);
        }

        @Bean
        RedissLockUtil redissLockUtil( RedissonClient redissonClient1) {
                RedissLockUtil redissLockUtil = new RedissLockUtil();
                redissLockUtil.setRedissonClient(redissonClient1);
                return redissLockUtil;
        }


}
