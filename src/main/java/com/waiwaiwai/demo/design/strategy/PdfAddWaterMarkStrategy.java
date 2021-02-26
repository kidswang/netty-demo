package com.waiwaiwai.demo.design.strategy;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfAddWaterMarkStrategy implements AddWaterMarkStrategy {
    @Override
    public void addWaterMark(InputStream inputStream, String waterMark, OutputStream outputStream, String imgType) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(inputStream);
            stamper = new PdfStamper(reader, outputStream);
            // 这里的字体设置比较关键，这个设置是支持中文的写法
            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);// 使用系统字体
            int total = reader.getNumberOfPages() + 1;

            PdfContentByte under;
            // Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                // 获得PDF最顶层
                under = stamper.getOverContent(i);
                // set Transparency
                PdfGState gs = new PdfGState();
                // 设置透明度为0.2
                gs.setFillOpacity(0.5f);
                under.setGState(gs);
                under.saveState();
                under.restoreState();
                under.beginText();
                under.setFontAndSize(base, 35);
                under.setTextMatrix(30, 30);
                under.setColorFill(BaseColor.GRAY);
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 2; x++) {
                        // 水印文字成45度角倾斜
                        under.showTextAligned(Element.ALIGN_LEFT, waterMark, 100 + 300 * x, 300 * y, 45);
                    }
                }
                // 添加水印文字
                under.endText();
                under.setLineWidth(1f);
                under.stroke();
            }
        } catch (Exception e) {
//            log.error("PDF加水印失败");
            e.printStackTrace();
        } finally {
            try {
                if (stamper != null) {
                    stamper.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
