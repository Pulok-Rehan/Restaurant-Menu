package com.instantmenu.Restaurant.Menu.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.decoder.QRCodeDecoderMetaData;
import com.instantmenu.Restaurant.Menu.model.QrCode;
import com.instantmenu.Restaurant.Menu.repository.QrRepository;
import com.instantmenu.Restaurant.Menu.service.QrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

@Service
@Slf4j
public class QrServiceImpl implements QrService {
    private final QrRepository qrRepository;
    private int height = 250;
    private int width = 250;

    public QrServiceImpl(QrRepository qrRepository) {
        this.qrRepository = qrRepository;
    }

    @Override
    public QrCode createQrCode(QrCode qrCode) throws IOException, WriterException {
        ObjectMapper objectMapper = new ObjectMapper();
        BitMatrix matrix = new MultiFormatWriter().encode(
                objectMapper.writeValueAsString(qrCode),
                BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "png", outputStream);
        String qrImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        log.info("byte array for QR-CODE: {}",qrImage);
        return qrRepository.save(QrCode.builder()
                .tableNo(qrCode.getTableNo())
                .userId(qrCode.getUserId())
                .restaurantId(qrCode.getRestaurantId())
                .qrImage(qrImage).build());
    }

    @Override
    public QrCode readQr(String qrCode) throws NotFoundException {
        try {
            byte[] qrByte = Base64.getDecoder().decode(qrCode.getBytes());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrByte);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            BufferedImageLuminanceSource bufferedImageLuminanceSource = new BufferedImageLuminanceSource(bufferedImage);
            HybridBinarizer hybridBinarizer = new HybridBinarizer(bufferedImageLuminanceSource);
            BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
            MultiFormatReader multiFormatReader = new MultiFormatReader();

            Result result = multiFormatReader.decode(binaryBitmap);
            log.info(result.getText(), result.getResultMetadata());
            QrCode qr = qrRepository.findByQrImage(qrCode);
            return QrCode.builder()
                    .id(qr.getId())
                    .restaurantId(qr.getRestaurantId())
                    .userId(qr.getUserId())
                    .tableNo(qr.getTableNo())
                    .qrImage(qr.getQrImage()).build();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}