package com.waiwaiwai.demo.design.strategy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/26 9:12
 * @Description: 添加水印
 */
public interface AddWaterMarkStrategy {

    void addWaterMark(InputStream inputStream, String waterMark, OutputStream outputStream, String imgType);

}
