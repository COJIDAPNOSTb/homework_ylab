package org.example.app.backend.repository;

import org.example.app.backend.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
}