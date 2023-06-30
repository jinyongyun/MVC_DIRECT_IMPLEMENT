package org.example.mvc.controller;

import org.example.mvc.model.User;
import org.example.mvc.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCreateController implements Controller {


    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // User 추가해주는 코드
        UserRepository.save(new User(request.getParameter("userId"), request.getParameter("name")));
        return "redirect:/users";
    }
}
