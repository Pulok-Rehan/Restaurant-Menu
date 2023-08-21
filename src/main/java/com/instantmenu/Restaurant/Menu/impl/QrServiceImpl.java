package com.instantmenu.Restaurant.Menu.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.instantmenu.Restaurant.Menu.model.QrCode;
import com.instantmenu.Restaurant.Menu.service.QrService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class QrServiceImpl implements QrService {
    private int height = 250;
    private int width = 250;
    @Override
    public BufferedImage createQrCode(QrCode qrCode) throws JsonProcessingException, WriterException {
        ObjectMapper objectMapper = new ObjectMapper();
        BitMatrix matrix = new MultiFormatWriter().encode(
                objectMapper.writeValueAsString(qrCode),
                BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
