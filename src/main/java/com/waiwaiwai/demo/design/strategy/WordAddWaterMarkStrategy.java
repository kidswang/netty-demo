package com.waiwaiwai.demo.design.strategy;

import com.microsoft.schemas.vml.CTShape;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
@Slf4j
public class WordAddWaterMarkStrategy implements AddWaterMarkStrategy{

    @Override
    public void addWaterMark(InputStream inputStream, String waterMark, OutputStream outputStream, String imgType) {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(inputStream);
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);
            headerFooterPolicy.createWatermark(waterMark);

            XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
            XWPFParagraph paragraph = header.getParagraphArray(0);

            XmlObject[] xmlObjects = paragraph.getCTP().getRArray(0).getPictArray(0)
                    .selectChildren(new QName("urn:schemas-microsoft-com:vml", "shape"));

            if (xmlObjects.length > 0) {
                CTShape ctshape = (CTShape) xmlObjects[0];
                ctshape.setFillcolor("");
                ctshape.setStyle(ctshape.getStyle() + ";rotation:315");
            } else {
                log.error("加水印失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
