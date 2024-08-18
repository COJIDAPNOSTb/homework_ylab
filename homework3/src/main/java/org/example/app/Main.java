package org.example.app;
import org.example.app.aspects.Log;
import org.example.app.config.ConfigDb;
import org.example.app.dto.OrderDTO;
import org.example.app.mapper.OrderMapper;
import org.example.app.model.Order;
import org.example.app.persistence.DbCarRepository;
import org.example.app.persistence.DbOrderRepository;
import org.example.app.persistence.DbUserRepository;
import org.example.app.repository.CarRepository;
import org.example.app.repository.OrderRepository;
import org.example.app.repository.UserRepository;
import org.example.app.service.OrderService;


public class Main {

    public static void main(String[] args)  {
        ConfigDb configDb = new ConfigDb();

        UserRepository userRepository = new DbUserRepository(configDb);
        CarRepository carRepository = new DbCarRepository(configDb);
        OrderDTO order = new OrderDTO();
        OrderMapper orderMapper =  OrderMapper.INSTANCE;
        order.setCarId(24);
        order.setCustomerId(107);
        order.setType("Purchase");
        order.setStatus("New");
        OrderRepository orderRepository = new DbOrderRepository(userRepository, carRepository , configDb);
        OrderService orderService = new OrderService(orderRepository, orderMapper);
        orderService.saveOrder(order);

    }}


          
            
        