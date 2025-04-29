package org.example.app.backend.model;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.app.backend.model.Car;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID id;
    private String type;
    private String status;
    private Instant date;

    private Car car;
    private User user;

}