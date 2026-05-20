package az.msuser.controller;

import az.msuser.dto.AuthDTOS.LoginDTO;
import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.dto.userDTOS.PostUserDto;
import az.msuser.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}
