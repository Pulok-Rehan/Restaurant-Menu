package com.instantmenu.Restaurant.Menu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.instantmenu.Restaurant.Menu.model.QrCode;
import com.instantmenu.Restaurant.Menu.response.CommonResponse;
import com.instantmenu.Restaurant.Menu.service.QrService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@RestController
public class QrController {
    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }
    @PostMapping(path = "/new-qr")
    public CommonResponse createQr(@RequestBody QrCode qrCode) throws IOException, WriterException {
        ObjectMapper objectMapper = new ObjectMapper();
        QrCode qr = qrService.createQrCode(qrCode);
        return CommonResponse.builder()
                .hasError(false)
                .message("QR-code created successfully")
                .content(objectMapper.writeValueAsString(qr)).build();
    }
    @PostMapping(path = "/read-qr")
    public CommonResponse readQr(@RequestBody byte[] qrCode) throws IOException, NotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        QrCode decodedQr = qrService.readQr(qrCode);
        return CommonResponse.builder()
                .hasError(false)
                .message("QR-code read successfully!")
                .content(objectMapper.writeValueAsString(decodedQr)).build();
    }
}
