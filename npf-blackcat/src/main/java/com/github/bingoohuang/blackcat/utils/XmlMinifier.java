package com.github.bingoohuang.blackcat.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.Stack;
import java.util.regex.Pattern;

@UtilityClass
public class XmlMinifier {
    Pattern xmlPIPattern = Pattern.compile("<\\?.*?\\?>");
    Pattern whitePattern = Pattern.compile("\\s+");
    Pattern openTagPattern = Pattern.compile("<(\\w+?)>");
    Pattern closeTagPattern = Pattern.compile("</\\w+?>");

    /**
     * 简单形式的XML迷你化.
     * 1. 去除PI
     * 2. 去除空白字符
     * 3. 替换结束标签为$
     * 4. 去除CDATA块开始和结束标记
     * 5. 转换开始标签格式
     * 例如:
     * <abc><efg>value</efg></abc>将被转换为abc:efg:value$$
     */
    public String minify(String xmlMsg) {
        // 去除所有的例如<pre><?xml version="1.0" encoding="UTF-8"?></pre>的XML PI
        String minified = xmlPIPattern.matcher(xmlMsg).replaceAll("");
        // 去除所有的空白字符(包括换行符)
        minified = whitePattern.matcher(minified).replaceAll("");
        // 替换所有的结束标签为$
        minified = closeTagPattern.matcher(minified).replaceAll("\\$");
        // 去除所有的块开始/结束标记
        minified = StringUtils.replace(minified, "<![CDATA[", "");
        minified = StringUtils.replace(minified, "]]>", "");
        // 替换所有的开始标签,例如<abc>替换成abc:
        minified = openTagPattern.matcher(minified).replaceAll("$1:");

        return minified;
    }

    Pattern recoverOpenTagPattern = Pattern.compile("(\\w+?):");

    /**
     * XML 部分恢复(不包括空白字符的恢复).
     * 例如: abc:efg:value$$恢复为
     * <pre>
     * <?xml version="1.0" encoding="UTF-8"?><abc><efg>value</efg></abc>
     * </pre>
     */
    public String recover(String minified) {
        // 恢复开始标签
        String recovered = recoverOpenTagPattern.matcher(minified).replaceAll("<$1>");
        // 恢复结束标签
        recovered = recoverCloseTag(recovered);

        // 恢复接口报文中的CDATA块
        recovered = StringUtils.replace(recovered, "<SvcCont>",
            "<SvcCont><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        recovered = StringUtils.replace(recovered, "</SvcCont>", "]]></SvcCont>");

        // 恢复XML PI
        recovered = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + recovered;

        return recovered;
    }

    Pattern openTagOrEndTagPattern = Pattern.compile("<\\w+?>|\\$(?=<|$|\\$)");

    private String recoverCloseTag(String recovered) {
        val matcher = openTagOrEndTagPattern.matcher(recovered);

        val openTags = new Stack<String>();
        val sb = new StringBuffer();
        while (matcher.find()) {
            val tag = matcher.group();
            if (tag.equals("$")) {
                val openTag = openTags.pop();
                val closeTag = "</" + openTag.substring(1);
                matcher.appendReplacement(sb, closeTag);
            } else {
                openTags.push(tag);
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}
