package org.example.recrutement.Controller;

import org.example.recrutement.DTO.RegisterRequest;
import org.example.recrutement.Entity.Category;
import org.example.recrutement.Entity.User;
import org.example.recrutement.Service.ServiceImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recrutement")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/alluser")
    public List<User> all()
    {
        return userService.all();
    }
    @GetMapping("/getuser/{id}")
    public User getById(@PathVariable Long id)
    {
        return userService.getById(id);
    }
    @PostMapping("/adduser")
    public User save(@RequestBody User user)
    {
        return userService.add(user);
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id)
    {
        if( userService.existById(id))
        {
            User user1=userService.getById(id);
            user1.setCreatedAt(user.getCreatedAt());
            user1.setClient(user.getClient());
            user1.setEmail(user.getEmail());
            user1.setUsername(user.getUsername());
            user1.setLastLogin(user.getLastLogin());
            user1.setPassword(user.getPassword());
            user1.setRole(user.getRole());
            userService.add(user1);
            return ResponseEntity.ok().body(user1);
        }
        else {
            HashMap<String,String> map=new HashMap<>();
            map.put("error","error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<?> delete(@RequestBody User user,@PathVariable Long id) {
        if (userService.existById(id))
        {
            userService.delete(id);
            HashMap<User,String> map=new HashMap<>();
            map.put(user,"deleted with success");
            return
                    ResponseEntity.ok().body(map);
        }
        else{
            HashMap<String,String>map=new HashMap<>();
            map.put("error","in deleting");
            return    ResponseEntity.ok().body(map);

        }
    }
    @PutMapping("assignClientToUser/{idclient}/{iduser}")
    public ResponseEntity<User> assignClientToUser(@PathVariable Long idclient,@PathVariable Long iduser)
    {
        User user=userService.assignClientToUser(idclient,iduser);
        return ResponseEntity.ok(user);
    }

    @PutMapping("assignAgencyToUser/{idagency}/{iduser}")
    public ResponseEntity<User> assignAgencyToUser(@PathVariable Long idagency,@PathVariable Long iduser)
    {
        User user=userService.assignAgencyToUser(idagency,iduser);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null || request.getUsername() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Email, password, and username are required")
            );
        }

        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Email is already registered")
            );
        }

        try {
            User registeredUser = userService.registerUser(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "User registered successfully",
                            "userId", registeredUser.getId(),
                            "email", registeredUser.getEmail(),
                            "role", registeredUser.getRole()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Registration failed: " + e.getMessage())
            );
        }
    }

}
