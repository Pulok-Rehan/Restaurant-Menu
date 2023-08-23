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
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
        log.info(Arrays.toString(outputStream.toByteArray()));
        String qrImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        log.info("byte array for QR-CODE: {}",qrImage);
        return qrRepository.save(QrCode.builder()
                .tableNo(qrCode.getTableNo())
                .userId(qrCode.getUserId())
                .restaurantId(qrCode.getRestaurantId())
                .qrImage(qrImage).build());
    }

    @Override
    public QrCode readQr(byte[] qrCode) throws NotFoundException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(qrCode);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        MultiFormatReader reader = new MultiFormatReader();

        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
        try {
            Result result = reader.decode(binaryBitmap, hints);
            log.info(result.getText(), result.getResultMetadata());
            return QrCode.builder().build();
        } catch (Exception e) {
            e.printStackTrace();
            return QrCode.builder().build();
        }

    }
}