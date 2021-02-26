package com.waiwaiwai.demo.design.strategy;

import java.util.HashMap;
import java.util.Map;

public class WaterMarkStrategyFactory {

    private static final Map<String, AddWaterMarkStrategy> waterMarkStrategy = new HashMap<>();

    static {
        ImgAddWaterMarkStrategy imgAddWaterMarkStrategy = new ImgAddWaterMarkStrategy();
        PdfAddWaterMarkStrategy pdfAddWaterMarkStrategy = new PdfAddWaterMarkStrategy();
        WordAddWaterMarkStrategy wordAddWaterMarkStrategy = new WordAddWaterMarkStrategy();
        waterMarkStrategy.put(WaterMarkType.PNG, imgAddWaterMarkStrategy);
        waterMarkStrategy.put(WaterMarkType.JPEG, imgAddWaterMarkStrategy);
        waterMarkStrategy.put(WaterMarkType.JPG, imgAddWaterMarkStrategy);
        waterMarkStrategy.put(WaterMarkType.PDF, pdfAddWaterMarkStrategy);
        waterMarkStrategy.put(WaterMarkType.DOC, wordAddWaterMarkStrategy);
        waterMarkStrategy.put(WaterMarkType.DOCX, wordAddWaterMarkStrategy);
    }

    public static AddWaterMarkStrategy getWaterMarkStrategy(String type) {
        return waterMarkStrategy.get(type);
    }



}
