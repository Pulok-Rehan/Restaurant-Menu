package com.instantmenu.Restaurant.Menu.repository;

import com.instantmenu.Restaurant.Menu.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrRepository extends JpaRepository<QrCode, Long> {
    QrCode findByQrImage(String qr);
}
