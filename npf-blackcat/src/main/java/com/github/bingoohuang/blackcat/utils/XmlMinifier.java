package com.github.bingoohuang.blackcat.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlMinifier {
    static Pattern xmlPIPattern = Pattern.compile("<\\?.*?\\?>");
    static Pattern whitePattern = Pattern.compile("\\s+");
    static Pattern openTagPattern = Pattern.compile("<(\\w+?)>");
    static Pattern closeTagPattern = Pattern.compile("</\\w+?>");

    // XML 迷你化
    public static String minify(String xmlMsg) {
        // 去除所有的例如<pre><?xml version="1.0" encoding="UTF-8"?></pre>的XML PI
        String minified = xmlPIPattern.matcher(xmlMsg).replaceAll("");
        // 去除所有的空白字符(包括换行符)
        minified = whitePattern.matcher(minified).replaceAll("");
        // 替换所有的结束标签为$
        minified = closeTagPattern.matcher(minified).replaceAll("\\$");
        // 去除所有的块开始标记
        minified = StringUtils.replace(minified, "<![CDATA[", "");
        // 去除所有的块结束标记
        minified = StringUtils.replace(minified, "]]>", "");
        // 替换所有的开始标签,例如<abc>替换成abc:
        minified = openTagPattern.matcher(minified).replaceAll("$1:");

        return minified;
    }

    static Pattern recoverOpenTagPattern = Pattern.compile("(\\w+?):");

    // XML 部分回复
    public static String recover(String minified) {
        // 恢复开始标签
        String recovered = recoverOpenTagPattern.matcher(minified).replaceAll("<$1>");
        // 恢复结束标签
        recovered = recoverCloseTag(recovered);

        // 恢复接口报文中的CDATA块
        recovered = StringUtils.replace(recovered, "<SvcCont>",
                "<SvcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        recovered = StringUtils.replace(recovered, "</SvcCont>", "]]></SvcCont>");

        return recovered;
    }

    static Pattern openTagOrEndTagPattern = Pattern.compile("<\\w+?>|\\$(?=<|$|\\$)");
    private static String recoverCloseTag(String recovered) {
        Matcher matcher = openTagOrEndTagPattern.matcher(recovered);
        Stack<String> openTags = new Stack<String>();
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String tag = matcher.group();
            if (tag.equals("$")) {
                String openTag = openTags.pop();
                String closeTag = "</" + openTag.substring(1);
                matcher.appendReplacement(sb, closeTag);
            } else {
                openTags.push(tag);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}