package org.geektimes.configuration.microprofile.config;

import org.geektimes.configuration.microprofile.config.converter.Converters;
import org.geektimes.configuration.microprofile.config.source.ConfigSources;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * @author wangyongfei
 */
public class ServletRequestConfigurationToMicroProfileConfig extends DefaultConfig  {



        protected ServletRequest request;
//        protected  configSources

    ServletRequestConfigurationToMicroProfileConfig(ConfigSources configSources, Converters converters) {
        super(configSources, converters);
    }


    public Object getProperty(String key) {
            String[] values = this.request.getParameterValues(key);
            if (values != null && values.length != 0) {
                if (values.length == 1) {
                    return this.transformPropertyValue(values[0]);
                } else {
                    List<Object> result = new ArrayList(values.length);

                    for(int i = 0; i < values.length; ++i) {
                        Object val = this.transformPropertyValue(values[i]);
                        if (val instanceof Collection) {
                            result.addAll((Collection)val);
                        } else {
                            result.add(val);
                        }
                    }

                    return result;
                }
            } else {
                return null;
            }
        }

        public Iterator<String> getKeys() {
            Map<String, ?> parameterMap = this.request.getParameterMap();
            return parameterMap.keySet().iterator();
        }
    }


