package az.msuser.controller;

import az.msuser.dto.AuthDTOS.LoginDTO;
import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.dto.userDTOS.PostUserDto;
import az.msuser.dto.userDTOS.PutUserDto;
import az.msuser.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GetUserDto> registerUser(@RequestBody PostUserDto postUserDto) {
        return ResponseEntity.ok(userService.registerUser(postUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.loginUser(loginDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<GetUserDto>> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Long id){
        return userService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDto> update(@PathVariable Long id, @RequestBody PutUserDto putUserDto, Authentication auth){
        return userService.updateUser(id,putUserDto,auth);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
