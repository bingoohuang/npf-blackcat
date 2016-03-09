package com.github.bingoohuang.blackcat.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class UnsensitiveConfig {
    String pattern;
    String replace;
    String method;

    public UnsensitiveConfig() {
    }

    public UnsensitiveConfig(String pattern) {
        this.pattern = pattern;
    }

    public UnsensitiveConfig(String pattern, String replace) {
        this.pattern = pattern;
        this.replace = replace;
    }

    public String filter(String msg) {
        if (isEmpty(method)) method = "star";

        if (equalsIgnoreCase("star", method)) {
            Pattern patternObj = Pattern.compile(pattern);
            Matcher matcher = patternObj.matcher(msg);

            if (isEmpty(replace)) replace = "***";
            String filtered = matcher.replaceAll(replace);
            return filtered;
        }


        throw new UnsupportedOperationException(method + " is not supported!");
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}
