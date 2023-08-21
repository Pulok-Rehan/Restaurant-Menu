package com.instantmenu.Restaurant.Menu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import com.instantmenu.Restaurant.Menu.model.QrCode;
import com.instantmenu.Restaurant.Menu.response.CommonResponse;
import com.instantmenu.Restaurant.Menu.service.QrService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
public class QrController {
    private final QrService qrService;

    public QrController(QrService qrService) {
        this.qrService = qrService;
    }
    @PostMapping(path = "/new-qr")
    public CommonResponse createQr(QrCode qrCode) throws JsonProcessingException, WriterException {
        BufferedImage bufferedImage = qrService.createQrCode(qrCode);
        if (bufferedImage.getData()==null){
            return CommonResponse.builder()
                    .hasError(true)
                    .message("Could not create QR-code").build();
        }
        return CommonResponse.builder()
                .hasError(false)
                .message("QR-code created successfully")
                .content(bufferedImage.toString()).build();
    }
}
