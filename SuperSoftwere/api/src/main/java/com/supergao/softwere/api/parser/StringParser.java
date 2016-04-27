package com.supergao.softwere.api.parser;

import java.io.InputStream;

/**
 * 字符串信息解析器
 * Created by YangJie on 2015/11/10.
 */
public class StringParser implements Parser<String> {
    @Override
    public String parse(String content) {
        return content;
    }

    @Override
    public String parse(InputStream inStream) {
        return null;
    }
}
