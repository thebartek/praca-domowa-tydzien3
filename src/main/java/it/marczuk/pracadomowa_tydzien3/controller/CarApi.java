package it.marczuk.pracadomowa_tydzien3.controller;

import it.marczuk.pracadomowa_tydzien3.entity.Car;
import it.marczuk.pracadomowa_tydzien3.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/cars", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CarApi {

    private CarService carService;

    @Autowired
    public CarApi(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Car>> getCars(@RequestParam(required = false) String color) {
        if(color == null) {
            List<Car> carList = carService.getAllCars();
            carList.forEach(this::addLinkToCar);
            return new ResponseEntity<>(CollectionModel.of(carList, linkTo(CarApi.class).withSelfRel()), HttpStatus.OK);
        } else {
            List<Car> colorCarList = carService.getCarsByColor(color);
            if(!colorCarList.isEmpty()) {
                colorCarList.forEach(this::addLinkToCar);
                return new ResponseEntity<>(CollectionModel.of(colorCarList, linkTo(CarApi.class).withSelfRel()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> carById = carService.getCarById(id);
        return carById.map(car -> {
            addLinkToCar(car);
            return ResponseEntity.ok(car);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Optional<Car> newCar = carService.addCar(car);
        return newCar.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping
    public ResponseEntity<Car> modCar(@RequestBody Car car) {
        Optional<Car> carUpdatedOptional = Optional.of(carService.modCar(car));
        return carUpdatedOptional.map(editCar -> {
            addLinkToCar(editCar);
            return ResponseEntity.ok(editCar);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> modSneakerParameter(@PathVariable long id, @RequestBody String modify) {
        Optional<Car> car = carService.modCarParameter(id, modify);
        return car.map(editCar -> {
            addLinkToCar(editCar);
            return ResponseEntity.ok(editCar);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable Long id) {
        Optional<Car> car = carService.removeCar(id);
        return car.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private void addLinkToCar(Car car) {
        car.addIf(!car.hasLinks(), () -> linkTo(CarApi.class).slash(car.getId()).withSelfRel());
    }
}
