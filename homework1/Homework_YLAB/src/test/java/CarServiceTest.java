

import org.example.app.model.Car;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.example.app.repository.CarRepository;
import org.example.app.service.CarService;
import org.example.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CarServiceTest {

    private CarService carService;
    private CarRepository carRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        carRepository = Mockito.mock(CarRepository.class);
        userService = Mockito.mock(UserService.class);
        carService = new CarService(carRepository, userService);
    }

    @Test
    public void testAddCar() {
        User user = new User("admin", "password", Role.ADMIN);
        Car car = new Car("Toyota", "Camry", 2020, 30000.0, "New");

        carService.addCar(user, car.getBrand(), car.getModel(), car.getYear(), car.getPrice(), car.getCondition());

        Mockito.verify(carRepository).save(Mockito.any(Car.class));
    }

    @Test
    public void testEditCar() {
        User user = new User("admin", "password", Role.ADMIN);
        Car car = new Car("Toyota", "Camry", 2020, 30000.0, "New");
        Mockito.when(carRepository.findById(car.getId())).thenReturn(car);

        carService.editCar(user, car.getId(), "Honda", "Civic", 2021, 25000.0, "Used");

        Mockito.verify(carRepository).update(Mockito.any(Car.class));
        assertThat(car.getBrand()).isEqualTo("Honda");
        assertThat(car.getModel()).isEqualTo("Civic");
        assertThat(car.getYear()).isEqualTo(2021);
        assertThat(car.getPrice()).isEqualTo(25000.0);
        assertThat(car.getCondition()).isEqualTo("Used");
    }

    @Test
    public void testDeleteCar() {
        User user = new User("admin", "password", Role.ADMIN);
        Car car = new Car("Toyota", "Camry", 2020, 30000.0, "New");
        Mockito.when(carRepository.findById(car.getId())).thenReturn(car);

        carService.deleteCar(user, car.getId());

        Mockito.verify(carRepository).delete(car.getId());
    }

    @Test
    public void testListCars() {
        List<Car> cars = List.of(
                new Car("Toyota", "Camry", 2020, 30000.0, "New"),
                new Car("Honda", "Civic", 2019, 20000.0, "Used")
        );
        Mockito.when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.listCars();

        assertThat(result).isEqualTo(cars);
    }

    @Test
    public void testSearchCars() {
        List<Car> cars = List.of(
                new Car("Toyota", "Camry", 2020, 30000.0, "New"),
                new Car("Honda", "Civic", 2019, 20000.0, "Used")
        );
        Mockito.when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.searchCars("Toyota", null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrand()).isEqualTo("Toyota");
    }
}
