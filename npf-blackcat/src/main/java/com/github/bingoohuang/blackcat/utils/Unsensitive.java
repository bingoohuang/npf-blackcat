package com.github.bingoohuang.blackcat.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.ailk.ecs.esf.conf.EsfProperties.getProperty;

public class Unsensitive {
    static List<UnsensitiveConfig> configs;

    static {
        // like : [{pattern:"(?<=PASSWORD:)[^\s]+"}, {pattern:"(?is)(?<=<PASSWORD>).*(?=</PASSWORD>)"}]
        String configJSONArray = getProperty("Blackcat.Unsensitive.Config", "");
        if (StringUtils.isNotEmpty(configJSONArray)) {
            configs = JSON.parseArray(configJSONArray, UnsensitiveConfig.class);
        }
    }

    public static String unsensitive(String msg) {
        if (configs == null) return msg;

        String filtered = msg;
        for (UnsensitiveConfig config : configs) {
            filtered = config.filter(filtered);
        }

        return filtered;
    }
}
