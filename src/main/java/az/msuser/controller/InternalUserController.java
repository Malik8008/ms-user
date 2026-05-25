package az.msuser.controller;

import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<GetUserDto> getUserByIdInternal(@PathVariable Long id){
        return userService.findByIdForInternal(id);
    }
}
