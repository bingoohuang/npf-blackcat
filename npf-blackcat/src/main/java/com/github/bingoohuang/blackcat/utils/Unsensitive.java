package com.github.bingoohuang.blackcat.utils;

import com.alibaba.fastjson.JSON;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.ailk.ecs.esf.conf.EsfProperties.getProperty;

@UtilityClass
public class Unsensitive {
    List<UnsensitiveConfig> configs;

    {
        // like : [{pattern:"(?<=PASSWORD:)[^\s]+"}, {pattern:"(?is)(?<=<PASSWORD>).*(?=</PASSWORD>)"}]
        val configJSONArray = getProperty("Blackcat.Unsensitive.Config", "");
        if (StringUtils.isNotEmpty(configJSONArray)) {
            configs = JSON.parseArray(configJSONArray, UnsensitiveConfig.class);
        }
    }

    public String unsensitive(String msg) {
        if (configs == null) return msg;

        String filtered = msg;
        for (val config : configs) {
            filtered = config.filter(filtered);
        }

        return filtered;
    }
}
