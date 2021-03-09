package it.marczuk.pracadomowa_tydzien3.repository;

import it.marczuk.pracadomowa_tydzien3.entity.Car;
import it.marczuk.pracadomowa_tydzien3.entity.Color;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CarRepo {

    private List<Car> carList;

    public CarRepo() {
        this.carList = new ArrayList<>();
        createListOfCars();
    }

    public void addCar(Car car) {
        carList.add(car);
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void removeCar(Car car) {
        carList.remove(car);
    }

    public void modCar(Car newCar) {
        Long id = newCar.getId()-1;
        carList.remove(id.intValue());
        carList.add(id.intValue(), newCar);
    }

    public void createListOfCars() {
        carList.add(new Car(1L, "BMW", "i8", Color.BLACK));
        carList.add(new Car(2L, "Mercedes", "Brabus", Color.WHITE));
        carList.add(new Car(3L, "Fiat", "126p", Color.RED));
    }
}
