/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmonmad.filetoimage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.nio.file.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author PTV
 */
public class main {

    public static void main(String[] args) {
        try {
            for (String s : args) {
                System.out.println(s);
            }

            if (args[0].equals("-c")) {
                System.out.println("#################################");
                System.out.println("#######     Conversion     ######");
                System.out.println("#################################");
                System.out.println("Reading from " + args[1]);
                byte[] array = Files.readAllBytes(Paths.get(args[1]));
                System.out.println(array.length);
                System.out.println(Math.sqrt(array.length));
                System.out.println(Math.ceil(Math.sqrt(array.length)));
                int height = (int) Math.ceil(Math.sqrt(array.length));
                int widht = (int) Math.ceil(Math.sqrt(array.length));
                BufferedImage img = BigBufferedImage.create(height, widht, BigBufferedImage.TYPE_4BYTE_ABGR);

                double percentChange = 1.0 / height / widht;
                double percentage = 0;

                int contador = 0;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < widht; j++) {

                        percentage += percentChange;
                        System.out.println((int) (percentage * 100) + " %");

                        Color color = genPastelColor();

                        int bitToInt;
                        if (contador < array.length) {
                            bitToInt = array[contador] & 0xff;
                        } else {
                            bitToInt = 0;
                        }
                        System.out.println(bitToInt);
                        img.setRGB(i, j, new Color(color.getRed(), color.getGreen(), color.getBlue(), bitToInt).getRGB());
                        System.out.println(contador + " / " + array.length);
                        contador++;
                    }
                }

                if (contador < array.length) {

                }

                File outputfile = new File(args[2]);
                ImageIO.write(img, "png", outputfile);
                System.out.println("Generated file " + args[2]);

            } else if (args[0].equals("-rc")) {
                System.out.println("#################################");
                System.out.println("####### Reverse Conversion ######");
                System.out.println("#################################");
                System.out.println("Reading from " + args[1]);
                BufferedImage img = ImageIO.read(new File(args[1]));

                byte[] imgBytes = new byte[img.getWidth() * img.getHeight()];

                double percentChange = 1.0 / img.getHeight() / img.getWidth();
                double percentage = 0;

                int contador = 0;
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        percentage += percentChange;
                        System.out.println((int) (percentage * 100) + " %");
                        Color color = new Color(img.getRGB(i, j), true);

                        if (color.getAlpha() != 0) {
                            int intToByte = color.getAlpha() & 0xff;
                            System.out.println(intToByte);
                            imgBytes[contador] = (byte) intToByte;
                            contador++;
                        }
                    }
                }

                int validBytesCount = 0;
                byte[] validBytes;
                
                for (int i = 0; i < imgBytes.length; i++) {
                    if(imgBytes[i] != 0){
                        validBytesCount++;
                    }
                }
                
                validBytes = new byte[validBytesCount];
                
                for (int i = 0; i < validBytesCount; i++) {
                    if(imgBytes[i] != 0){
                        validBytes[i] = imgBytes[i];
                    }
                }

                try (FileOutputStream fos = new FileOutputStream(args[2])) {
                    fos.write(validBytes);
                    //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                }
                System.out.println("Generated file " + args[2]);

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }

    }

    public static Color genPastelColor() {
        Random random = new Random();
        final float hue = random.nextFloat();
        final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
        final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
        return Color.getHSBColor(hue, saturation, luminance);
    }

    public static Color genNormalColor() {
        int R = (int) (Math.random() * 256);
        int G = (int) (Math.random() * 256);
        int B = (int) (Math.random() * 256);
        return new Color(R, G, B);
    }
}
