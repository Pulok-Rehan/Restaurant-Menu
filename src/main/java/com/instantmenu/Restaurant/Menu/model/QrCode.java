package com.instantmenu.Restaurant.Menu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long restaurant_id;
    private long user_id;
}
