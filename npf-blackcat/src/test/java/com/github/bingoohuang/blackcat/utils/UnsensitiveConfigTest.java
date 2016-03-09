package com.github.bingoohuang.blackcat.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UnsensitiveConfigTest {
    @Test
    public void testKeyValueInJSON() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?<=\"PASSWORD\":\").*?(?=\"[,\\]}])");
        String filter = config.filter("{\"PASSWORD\":\"BINGOOHUANG\"}");
        assertThat(filter).isEqualTo("{\"PASSWORD\":\"***\"}");
    }

    @Test
    public void testKeyValue() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?<=PASSWORD:)[^\\s]+");
        String filter = config.filter("PASSWORD:BINGOOHUANG");
        assertThat(filter).isEqualTo("PASSWORD:***");
    }

    @Test
    public void testKeyValue0() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?<=PASSWORD:)[^\\s]+");
        String filter = config.filter("PASSWORD:BINGOOHUANG, NAME:BINGOO");
        assertThat(filter).isEqualTo("PASSWORD:*** NAME:BINGOO");
    }

    @Test
    public void testKeyValue2() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?<=PASSWORD:)[^\\s]+", "%%%");
        String filter = config.filter("PASSWORD:BINGOOHUANG");
        assertThat(filter).isEqualTo("PASSWORD:%%%");
    }

    @Test
    public void testKeyValue3() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?<=PASSWORD:)[^\\s]+", "\\$\\$\\$");
        String filter = config.filter("PASSWORD:BINGOOHUANG");
        assertThat(filter).isEqualTo("PASSWORD:$$$");
    }

    @Test
    public void testXML() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(?is)(?<=<PASSWORD>).*(?=</PASSWORD>)");
        String filter = config.filter("<PASSWORD>BINGOOHUANG</PASSWORD>");
        assertThat(filter).isEqualTo("<PASSWORD>***</PASSWORD>");

        filter = config.filter("<PASSWORD>\r\nBINGOOHUANG\r\n</PASSWORD>");
        assertThat(filter).isEqualTo("<PASSWORD>***</PASSWORD>");

        filter = config.filter("<password>\r\nBINGOOHUANG\r\n</password>");
        assertThat(filter).isEqualTo("<password>***</password>");
    }


    @Test
    public void testPattern() {
        UnsensitiveConfig config = new UnsensitiveConfig(
                "(\\d{3})\\d{3}(?:19|20)\\d{2}(?:0|1)\\d(?:[0123])\\d{2}(\\d{3})", "$1***$2");

        String filter = config.filter("130105199601011336");
        assertThat(filter).isEqualTo("130***336");
    }
}
