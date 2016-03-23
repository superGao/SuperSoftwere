package com.supergao.software.api.parser;

import java.io.InputStream;

/**
 * 解析器
 * Created by YangJie on 2015/11/10.
 */
public interface Parser<T> {
    /**
     * 根据字符串解析成指定对象
     * @param content 字符串内容
     * @return 解析后得到的对象
     */
    public T parse(String content) ;

    /**
     * 根据输入流数据解析成指定对象
     * @param inStream 输入流
     * @return 解析后得到的对象
     */
    public T parse(InputStream inStream) ;
}
