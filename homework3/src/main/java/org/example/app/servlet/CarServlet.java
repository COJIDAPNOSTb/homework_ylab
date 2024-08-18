package org.example.app.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.app.aspects.Log;
import org.example.app.config.ConfigDb;
import org.example.app.dto.CarDTO;
import org.example.app.mapper.CarMapper;
import org.example.app.model.Car;

import org.example.app.persistence.DbCarRepository;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.example.app.repository.CarRepository;
import org.example.app.service.CarService;
import org.mapstruct.factory.Mappers;


@WebServlet(name = "CarServlet", urlPatterns = "/cars/*")
public class CarServlet extends HttpServlet {
    private CarService carService;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        ConfigDb configDb = new ConfigDb();
        CarRepository carRepository = new DbCarRepository(configDb);
        CarMapper carMapper = Mappers.getMapper(CarMapper.class);
        carService = new CarService(carRepository, carMapper);
    }

    @Override
    @Log
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CarDTO> cars = carService.getAllCars();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String jsonResponse = mapper.writeValueAsString(cars);
        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CarDTO carDTO = mapper.readValue(req.getInputStream(), CarDTO.class);
        carService.saveCar(carDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Car ID is missing");
            return;
        }

        // Получаем ID пользователя из URL
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Car ID");
            return;
        }

        int carId;
        try {
            carId = Integer.parseInt(pathParts[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Car ID must be an integer");
            return;
        }


        carService.deleteCar(carId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // HTTP 204 No Content
    }
@Override
protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.equals("/")) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Car ID is missing");
        return;
    }

    int carId;
    try {
        carId = Integer.parseInt(pathInfo.substring(1));
    } catch (NumberFormatException e) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Car ID");
        return;
    }

    CarDTO updatedCar = mapper.readValue(req.getInputStream(), CarDTO.class);
    updatedCar.setId(carId);
    carService.updateCar(carId, updatedCar);

    resp.setContentType("application/json");
    resp.getWriter().write(mapper.writeValueAsString(updatedCar));
}

}
