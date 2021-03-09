package it.marczuk.pracadomowa_tydzien3.service;

import it.marczuk.pracadomowa_tydzien3.entity.Car;
import it.marczuk.pracadomowa_tydzien3.entity.Color;
import it.marczuk.pracadomowa_tydzien3.repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private CarRepo repository;

    @Autowired
    public CarServiceImpl(CarRepo repository) {
        this.repository = repository;
    }

    @Override
    public List<Car> getAllCars() {
        return repository.getCarList();
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        List<Car> carList = repository.getCarList();
        return carList.stream().filter(car -> car.getId() == id).findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        List<Car> carList = repository.getCarList();
        return carList.stream().filter(car -> car.getColor().toString().equals(color)).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> addCar(Car car) {
        List<Car> carList = repository.getCarList();
        boolean isCarExists = carList.stream().anyMatch(newCar -> newCar.getMark().equals(car.getMark()));
        return isCarExists ? Optional.empty() : Optional.of(saveCar(car));
    }

    @Override
    public Car modCar(Car newCar) {
        repository.modCar(newCar);
        return newCar;
    }

    @Override
    public Optional<Car> modCarParameter(Long id, String modify) {
        String[] mod = modify.split("\"");
        String type = mod[1];
        String modArg = mod[3];
        return updateCarMethod(id, type, modArg);
    }

    @Override
    public Optional<Car> removeCar(Long id) {
        List<Car> carList = repository.getCarList();
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        repository.removeCar(first.get());
        return first;
    }

    private Optional<Car> updateCarMethod(long id, String type, String modArg){
        List<Car> carList = repository.getCarList();
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if(first.isPresent()) {
            if(type.equals("mark")){
                first.get().setMark(modArg);
            }
            if(type.equals("model")){
                first.get().setModel(modArg);
            }
            if(type.equals("color")){
                first.get().setColor(Color.valueOf(modArg));
            }
            return first;
        }
        return Optional.empty();
    }

    private Car saveCar(Car car) {
        repository.addCar(car);
        return car;
    }
}
