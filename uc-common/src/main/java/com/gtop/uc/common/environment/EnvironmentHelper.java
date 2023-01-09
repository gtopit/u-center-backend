package com.gtop.uc.common.environment;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.*;

/**
 * @author hongzw
 */
public final class EnvironmentHelper {

    private static Environment environment;
    private static final Map<String, EnvironmentHelper.ResourceProperties> resourcePropertiesMap = new HashMap();

    private EnvironmentHelper() {
    }

    static void setEnvironment(Environment environment) {
        EnvironmentHelper.environment = environment;
    }

    public static void addResource(String name, Resource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new Resource[]{resource});
        Properties properties = factory.getObject();
        properties.load(resource.getInputStream());
        EnvironmentHelper.ResourceProperties resourceProperties = new EnvironmentHelper.ResourceProperties(resource, properties);
        resourcePropertiesMap.put(name, resourceProperties);
    }

    public static void addResource(Resource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new Resource[]{resource});
        Properties properties = factory.getObject();
        properties.load(resource.getInputStream());
        EnvironmentHelper.ResourceProperties resourceProperties = new EnvironmentHelper.ResourceProperties(resource, properties);
        String filePath = resourceProperties.resource.getURI().toString();
        resourcePropertiesMap.put(filePath, resourceProperties);
    }

    public static Set<String> getPropertiesByKeyPrefix(String keyPrefix) {
        Set<String> result = new HashSet();
        Iterator var2 = resourcePropertiesMap.values().iterator();

        while(var2.hasNext()) {
            EnvironmentHelper.ResourceProperties resourceProperties = (EnvironmentHelper.ResourceProperties)var2.next();
            resourceProperties.getProperties().forEach((key, value) -> {
                if (key.toString().startsWith(keyPrefix)) {
                    String valueStr = String.valueOf(value);
                    if (StrUtil.isNotEmpty(valueStr) && !result.contains(valueStr)) {
                        result.add(String.valueOf(value));
                    }
                }

            });
        }

        return result;
    }

    public static List<String> getPropertiesByKey(String targetKey) {
        List<String> result = new ArrayList();
        Iterator var2 = resourcePropertiesMap.values().iterator();

        while(var2.hasNext()) {
            EnvironmentHelper.ResourceProperties resourceProperties = (EnvironmentHelper.ResourceProperties)var2.next();
            resourceProperties.getProperties().forEach((key, value) -> {
                if (key.toString().startsWith(targetKey)) {
                    String valueStr = String.valueOf(value);
                    if (StrUtil.isNotEmpty(valueStr)) {
                        result.add(String.valueOf(value));
                    }
                }

            });
        }

        return result;
    }

    public static String getProperty(String key) {
        return (String)getRequiredProperty(key, String.class);
    }

    public static boolean containsProperty(String key) {
        if (environment.containsProperty(key)) {
            return StrUtil.isNotEmpty(environment.getProperty(key));
        } else {
            return false;
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String value = environment.getProperty(key);
        return StrUtil.isEmpty(value) ? defaultValue : value;
    }

    public static <T> T getRequiredProperty(String key, Class<T> targetType) {
        return environment.getRequiredProperty(key, targetType);
    }

    public static class ResourceProperties {
        private Resource resource;
        private Properties properties;

        public Resource getResource() {
            return this.resource;
        }

        public Properties getProperties() {
            return this.properties;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof EnvironmentHelper.ResourceProperties)) {
                return false;
            } else {
                EnvironmentHelper.ResourceProperties other = (EnvironmentHelper.ResourceProperties)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$resource = this.getResource();
                    Object other$resource = other.getResource();
                    if (this$resource == null) {
                        if (other$resource != null) {
                            return false;
                        }
                    } else if (!this$resource.equals(other$resource)) {
                        return false;
                    }

                    Object this$properties = this.getProperties();
                    Object other$properties = other.getProperties();
                    if (this$properties == null) {
                        if (other$properties != null) {
                            return false;
                        }
                    } else if (!this$properties.equals(other$properties)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof EnvironmentHelper.ResourceProperties;
        }

        @Override
        public int hashCode() {
            int result = 1;
            Object resource = this.getResource();
            result = result * 59 + (resource == null ? 43 : resource.hashCode());
            Object properties = this.getProperties();
            result = result * 59 + (properties == null ? 43 : properties.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "EnvironmentHelper.ResourceProperties(resource=" + this.getResource() + ", properties=" + this.getProperties() + ")";
        }

        public ResourceProperties(Resource resource, Properties properties) {
            this.resource = resource;
            this.properties = properties;
        }
    }
}
