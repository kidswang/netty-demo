package com.waiwaiwai.demo.design.strategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImgAddWaterMarkStrategy implements AddWaterMarkStrategy{
    @Override
    public void addWaterMark(InputStream inputStream, String waterMark, OutputStream outputStream, String imgType) {
        Graphics2D graphics2D = null;
        Font font = new Font("simsun", Font.BOLD, 50);//水印字体，大小
        Color markContentColor = Color.GRAY;//水印颜色
        Integer degree = -45;//设置水印文字的旋转角度
        float alpha = 1.0f;//设置水印透明度 默认为1.0  值越小颜色越浅
        try {
            BufferedImage bgImage = ImageIO.read(inputStream);
            int srcImgWidth = bgImage.getWidth(null);
            int srcImgHeight = bgImage.getHeight(null);

            BufferedImage image = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            graphics2D = image.createGraphics();
            graphics2D.drawImage(bgImage, 0, 0, srcImgWidth, srcImgHeight, null);
            graphics2D.setColor(markContentColor);
            graphics2D.setFont(font);
            //设置水印文字透明度
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            //设置水印旋转
//            graphics2D.rotate(Math.toRadians(degree), image.getWidth(), image.getHeight());
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(degree), 0, 0);
            Font rotatedFont = font.deriveFont(affineTransform);
            graphics2D.setFont(rotatedFont);

            JLabel label = new JLabel(waterMark);
            FontMetrics metrics = label.getFontMetrics(font);

            int width = metrics.stringWidth(label.getText());//文字水印的宽

            // 打印三行两列
            int rowWidth = srcImgHeight / 3;
            int columnWidth = srcImgWidth / 3;


            if (rowWidth < width || columnWidth < width) {
                int row = srcImgHeight / 3;
                int column = srcImgWidth / 3;
                graphics2D.drawString(waterMark, column, 2 * row);//画出水印,并设置水印位置
            } else {
                for (int j = 1; j < 4; j++) { // hang
                    for (int i = 1; i < 3; i++) { // lie
                        graphics2D.drawString(waterMark, i * columnWidth, j * rowWidth);//画出水印,并设置水印位置
                    }
                }
            }
            ImageIO.write(image, imgType, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (graphics2D != null) {
                graphics2D.dispose();
            }
        }
    }
}
