package org.cf.broker.util;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class YamlUtil {
    public String readYaml() throws IOException {
        File file = ResourceUtils.getFile("classpath:deployment/deploy_redis.yaml");
        return new String(Files.readAllBytes(file.toPath()));
    }

    public Map fromYaml(String yamlString) {
        Map<String, Object> map = new Yaml().load(yamlString);
        return map;
    }

}
