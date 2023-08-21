package com.instantmenu.Restaurant.Menu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import com.instantmenu.Restaurant.Menu.model.QrCode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public interface QrService {
    BufferedImage createQrCode(QrCode qrCode) throws JsonProcessingException, WriterException;
}
