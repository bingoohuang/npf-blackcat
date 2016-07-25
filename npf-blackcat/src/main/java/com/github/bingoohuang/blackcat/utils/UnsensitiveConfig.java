package com.github.bingoohuang.blackcat.utils;

import lombok.Data;
import lombok.val;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
public class UnsensitiveConfig {
    String pattern;
    String replace;
    String method = "star";

    public UnsensitiveConfig(String pattern) {
        this.pattern = pattern;
    }

    public UnsensitiveConfig(String pattern, String replace) {
        this.pattern = pattern;
        this.replace = replace;
    }

    public String filter(String msg) {
        if (equalsIgnoreCase("star", method)) {
            val patternObj = Pattern.compile(pattern);
            val matcher = patternObj.matcher(msg);

            if (isEmpty(replace)) replace = "***";
            String filtered = matcher.replaceAll(replace);
            return filtered;
        }


        throw new UnsupportedOperationException(method + " is not supported!");
    }
}
