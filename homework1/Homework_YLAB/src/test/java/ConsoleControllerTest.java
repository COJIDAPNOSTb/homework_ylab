

import org.example.app.model.Role;
import org.example.app.service.*;
import org.example.in.ConsoleController;
import org.example.out.ConsoleView;
import org.example.in.UserInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ConsoleControllerTest {

    private UserService userService;
    private CarService carService;
    private OrderService orderService;
    private ConsoleView consoleView;
    private ConsoleController consoleController;
    private UserInput userInput;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        carService = Mockito.mock(CarService.class);
        orderService = Mockito.mock(OrderService.class);
        userInput = Mockito.mock(UserInput.class);
        consoleView = new ConsoleView(userInput);
        consoleController = new ConsoleController(userService, carService, orderService, consoleView);
    }

    @Test
    public void testRegisterUser() {
        Mockito.when(userInput.getIntInput()).thenAnswer(new Answer<Integer>() {
            private int count = 0;

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                if (count == 0) {
                    count++;
                    return 1;
                } else if (count == 1) {
                    count++;
                    return 1;
                }
                return 0;
            }
        });

        Mockito.when(userInput.getStringInput())
                .thenReturn("newUser")
                .thenReturn("newPassword");

        Thread controllerThread = new Thread(() -> {
            consoleController.run();
        });
        controllerThread.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        consoleController.stop();

        try {
            controllerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Mockito.verify(userService).register("newUser", "newPassword", Role.ADMIN);
    }
}