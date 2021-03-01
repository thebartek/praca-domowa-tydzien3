package it.marczuk.pracadomowa_tydzien3.service;

import it.marczuk.pracadomowa_tydzien3.entity.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> getAllCars();

    Optional<Car> getCarById(Long id);

    List<Car> getCarsByColor(String color);

    Optional<Car> addCar(Car car);

    Optional<Car> modCar(Car car);

    Optional<Car> modCarParameter(Long id, String modify);

    Optional<Car> removeCar(Long id);
}
