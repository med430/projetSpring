package gl2.example.salles.controller;

import gl2.example.salles.dto.UserRequest;
import gl2.example.salles.dto.UserResponse;
import gl2.example.salles.model.User;
import gl2.example.salles.model.enums.Role;
import gl2.example.salles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponse> userResponses = users.stream().map(UserResponse::new).toList();
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        UserResponse userResponse = new UserResponse(userService.findById(id));
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest userRequest) {
        User user = userService.createUser(userRequest);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        User user = userService.findById(id);
        userService.updateUser(id, userRequest);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/{id}/revokeAdminRights")
    public ResponseEntity<UserResponse> makeUser(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setRole(Role.User);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}/makeAdmin")
    public ResponseEntity<UserResponse> makeAdmin(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setRole(Role.ADMIN);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
