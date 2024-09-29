package kr.co.polycube.backendtest.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.co.polycube.backendtest.model.User;
import kr.co.polycube.backendtest.service.UserService;
import kr.co.polycube.backendtest.view.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //@Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public SetUserView postUser(@RequestParam String name) {
        User user = userService.saveUser(name);
        return new SetUserView(user.getId());
    }

    @GetMapping("/{id}")
    public GetUserView getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return new GetUserView(user.getId(), user.getName());
    }

    @PutMapping("/{id}")
    public GetUserView putUser(@PathVariable("id") Long id, @RequestParam String name) {
        User user = userService.putUser(id, name);
        return new GetUserView(user.getId(), user.getName());
    }
}