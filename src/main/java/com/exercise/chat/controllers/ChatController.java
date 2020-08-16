package com.exercise.chat.controllers;

import com.exercise.chat.entities.Color;
import com.exercise.chat.entities.Message;
import com.exercise.chat.entities.User;
import com.exercise.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(
        ChatService chatService
    ) {
        this.chatService = chatService;
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(User user, Model model, HttpServletRequest request) {
        try {
            User savedUser = chatService.addUser(user);
            request.getSession().setAttribute("userName", savedUser.getName());
            request.getSession().setAttribute("color", savedUser.getColor());
            model.addAttribute("users", chatService.findAllUsers());
        } catch (Exception e) {
            model.addAttribute("error", "That user name already exist, please choose another!");
            return "add-user";
        }

        return "redirect:/";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String textMessage, HttpServletRequest request) {
        Message message = new Message();
        message.setTextString(textMessage);

        message.setUserName(String.valueOf(request.getSession().getAttribute("userName")));
        message.setColor((Color) request.getSession().getAttribute("color"));
        chatService.sendMessage(message);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", chatService.findAllUsers());
        model.addAttribute("messages", chatService.findAllMessages());
        return "index";
    }
}
