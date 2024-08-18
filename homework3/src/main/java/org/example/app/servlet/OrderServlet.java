package org.example.app.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.example.app.config.ConfigDb;
import org.example.app.dto.OrderDTO;
import org.example.app.mapper.CarMapper;
import org.example.app.mapper.OrderMapper;
import org.example.app.mapper.UserMapper;
import org.example.app.model.Car;
import org.example.app.model.Order;
import org.example.app.model.User;
import org.example.app.persistence.DbCarRepository;
import org.example.app.persistence.DbOrderRepository;
import org.example.app.persistence.DbUserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.example.app.repository.CarRepository;
import org.example.app.repository.OrderRepository;
import org.example.app.repository.UserRepository;
import org.example.app.service.OrderService;

@WebServlet(name = "OrderServlet", urlPatterns = "/orders/*")
public class OrderServlet extends HttpServlet {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private ObjectMapper mapper = new ObjectMapper();

    private OrderMapper orderMapper = OrderMapper.INSTANCE;
    private UserMapper userMapper = UserMapper.INSTANCE;
    private CarMapper carMapper = CarMapper.INSTANCE;
    private OrderService orderService;
    @Override
    public void init() throws ServletException {
        super.init();
        ConfigDb configDb = new ConfigDb();
        mapper.registerModule(new JavaTimeModule());
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }

        userRepository = new DbUserRepository(configDb);
        carRepository = new DbCarRepository(configDb);
        orderRepository = new DbOrderRepository(userRepository, carRepository, configDb);
        orderService = new OrderService( orderRepository,orderMapper);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            OrderDTO orderDTO = mapper.readValue(req.getInputStream(), OrderDTO.class);


            orderService.saveOrder(orderDTO);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error processing order\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Вызов бизнес-логики для получения всех заказов
            List<OrderDTO> orders = orderService.getAllOrders();

            // Возврат результата в виде JSON
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String jsonResponse = mapper.writeValueAsString(orders);
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error retrieving orders\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is missing");
            return;
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID");
            return;
        }

        try {
            int orderId = Integer.parseInt(pathParts[1]);
            orderService.deleteOrder(orderId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID must be an integer");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error deleting order\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is missing");
            return;
        }

        try {
            int orderId = Integer.parseInt(pathInfo.substring(1));
            OrderDTO orderDTO = mapper.readValue(req.getInputStream(), OrderDTO.class);
            orderDTO.setId(orderId);


           orderService.updateOrder(orderDTO.getId(),orderDTO);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Order ID");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error updating order\"}");
            e.printStackTrace();
        }
    }
}