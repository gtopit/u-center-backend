package com.gtop.uc.common.environment;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hongzw
 */
public class UcEnvironmentPostProcessor implements EnvironmentPostProcessor, ApplicationListener<ApplicationPreparedEvent> {

    private static final DeferredLog LOGGER = new DeferredLog();
    private static final AtomicBoolean isLoggerReplayed = new AtomicBoolean(false);

    private static final PropertySourceFactory yamlPropertySourceFactory = (name, resource) -> {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new Resource[]{resource.getResource()});
        factory.afterPropertiesSet();
        Properties ymlProperties = factory.getObject();
        String propertyName = !StringUtils.isEmpty(name) ? name : resource.getResource().getFilename();
        return new PropertiesPropertySource(propertyName, ymlProperties);
    };

    public UcEnvironmentPostProcessor() {}

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String active = environment.getProperty("spring.profiles.active");
        if (StrUtil.isEmpty(active)) {
            LOGGER.warn("缺少spring.profiles.active启动参数，默认加载resources/config/local文件夹下配置文件。");
            active = "local";
            environment.setActiveProfiles(active);
        }
        boolean localConfigEnable = true;
        String additionalConfigPath;
        if (environment.containsProperty("uc.localConfig.enable")) {
            additionalConfigPath = environment.getProperty("uc.localConfig.enable");
            if (!"true".equalsIgnoreCase(additionalConfigPath)) {
                LOGGER.info("本地配置文件已关闭，需要开启请配置uc.localConfig.enable为true。");
                localConfigEnable = false;
            }
        }

        if (!StrUtil.isEmpty(active) && localConfigEnable) {
            this.loadResources(environment, "classpath:/config/" + active + "/*.yml");
            this.loadResources(environment, "classpath:/config/" + active + "/*.yaml");
        }

        this.loadResources(environment, "classpath*:/default-config/*.yml");
        this.loadResources(environment, "classpath*:/default-config/*.yaml");
        additionalConfigPath = environment.getProperty("spring.config.additional-location");
        if (!StrUtil.isEmpty(additionalConfigPath)) {
            this.loadAdditionalConfigResources(environment, additionalConfigPath);
        } else {
            ApplicationHome ah = new ApplicationHome(this.getClass());
            File source = ah.getSource();
            if (source != null) {
                File parent = source.getParentFile();
                if (parent != null) {
                    String jarPath = parent.getAbsolutePath();
                    this.loadAdditionalConfigResources(environment, jarPath + "/config/");
                }
            }
        }
        EnvironmentHelper.setEnvironment(environment);
    }

    private void loadResources(ConfigurableEnvironment environment, String locationPattern) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new PathMatchingResourcePatternResolver());
            Resource[] resources = resolver.getResources(locationPattern);
            int length = resources.length;

            for(int i = 0; i < length; ++i) {
                Resource resource = resources[i];
                environment.getPropertySources().addLast(this.loadResourceFile(resource));
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new LoadPropertySourceFailedException("加载配置文件属性失败。", e);
        }

    }

    private void loadAdditionalConfigResources(ConfigurableEnvironment environment, String additionalConfigPath) {
        File configFolder = new File(additionalConfigPath);
        File[] files = configFolder.listFiles();
        if (files != null && files.length > 0) {
            int length = files.length;

            for(int i = 0; i < length; ++i) {
                File file = files[i];
                if (this.isYamlFile(file)) {
                    Resource resource = new FileSystemResource(file.getPath());
                    environment.getPropertySources().addFirst(this.loadResourceFile(resource));
                }
            }
        }

    }

    private PropertySource<?> loadResourceFile(Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("配置文件资源不存在。Resource:" + resource.getFilename());
        } else {
            try {
                EnvironmentHelper.addResource(resource);
                String name = Objects.requireNonNull(resource.getFilename());
                EncodedResource encodedResource = new EncodedResource(resource, "utf-8");
                return yamlPropertySourceFactory.createPropertySource(name, encodedResource);
            } catch (IOException ex) {
                throw new IllegalStateException("加载配置文件失败。Resource:" + resource.getFilename(), ex);
            }
        }
    }

    private boolean isYamlFile(File file) {
        String filename = file.getName();
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        return ".yml".equals(fileSuffix) || ".yaml".equals(fileSuffix);
    }

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        if (isLoggerReplayed.compareAndSet(false, true)) {
            LOGGER.replayTo(UcEnvironmentPostProcessor.class);
        }
    }

}
