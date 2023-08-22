package com.instantmenu.Restaurant.Menu.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private long restaurantId;
    private int tableNo;
    @Column(length = 3000)
    private String qrImage;
}
