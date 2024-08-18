package org.example.app.servlet;




import org.example.app.aspects.Log;
import org.example.app.config.ConfigDb;
import org.example.app.dto.UserDTO;
import org.example.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.app.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import org.example.app.persistence.DbUserRepository;
@WebServlet(name = "UserServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        // Инициализация UserService
        ConfigDb configDb = new ConfigDb();
        UserRepository userRepository = new DbUserRepository(configDb);
        userService = new UserService(userRepository);
    }

    @Override
    @Log
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Получить всех пользователей
            List<UserDTO> users = userService.getAllUsers();
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(users));
        } else {
            // Получить пользователя по ID
            String[] pathParts = pathInfo.split("/");
            int userId = Integer.parseInt(pathParts[1]);
            UserDTO user = userService.getUserById(userId);
            if (user != null) {
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(user));
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO userDTO = mapper.readValue(req.getInputStream(), UserDTO.class);
        userService.createUser(userDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        int userId = Integer.parseInt(pathParts[1]);

        UserDTO userDTO = mapper.readValue(req.getInputStream(), UserDTO.class);
        userDTO.setId(userId);
        userService.updateUser(userDTO);

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        int userId = Integer.parseInt(pathParts[1]);

        userService.deleteUser(userId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
