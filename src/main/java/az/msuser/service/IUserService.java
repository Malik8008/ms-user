package az.msuser.service;

import az.msuser.dto.AuthDTOS.LoginDTO;
import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.dto.userDTOS.PostUserDto;
import az.msuser.dto.userDTOS.PutUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService {
    ResponseEntity<List<GetUserDto>> getUsers();
    ResponseEntity<GetUserDto> findById(Long id);
    ResponseEntity<String> loginUser(LoginDTO loginDTO);
    GetUserDto registerUser(PostUserDto postUserDto);
    ResponseEntity<GetUserDto> updateUser(Long id, PutUserDto putUserDto);
    ResponseEntity<Void>  deleteUser(Long id);
}
