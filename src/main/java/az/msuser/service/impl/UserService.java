package az.msuser.service.impl;

import az.msuser.configuration.security.JWTUtil;
import az.msuser.dto.AuthDTOS.LoginDTO;
import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.dto.userDTOS.PostUserDto;
import az.msuser.dto.userDTOS.PutUserDto;
import az.msuser.model.User;
import az.msuser.repository.UserRepository;
import az.msuser.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder  passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<List<GetUserDto>> GetUsers() {
        List<GetUserDto> users = userRepository.findAllByDeletedFalse().stream()
                .map(user -> modelMapper.map(user, GetUserDto.class)).toList();
        return ResponseEntity.ok().body(users);
    }

    @Override
    public ResponseEntity<GetUserDto> findById(Long id) {
        User user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(()->new EntityNotFoundException("User not found"));
        return ResponseEntity.ok().body(modelMapper.map(user, GetUserDto.class));
    }

    @Override
    public ResponseEntity<String> loginUser(LoginDTO loginDTO) {
        User existUser = userRepository.findByPhone(loginDTO.getPhone()).orElseThrow(()->new EntityNotFoundException("User with phone " + loginDTO.getPhone() + " not found"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), existUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtil.generateToken(existUser);
        return ResponseEntity.ok(token);
    }

    @Override
    public GetUserDto registerUser(PostUserDto postUserDto) {
        Optional<User> existUser =
                userRepository.findByPhone(postUserDto.getPhone());

        if (existUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User newUser= modelMapper.map(postUserDto, User.class);
        newUser.setPhone(postUserDto.getPhone());
        newUser.setPassword(passwordEncoder.encode(postUserDto.getPassword()));
        newUser.setEmail(postUserDto.getEmail());
        newUser.setName(postUserDto.getName());
        userRepository.save(newUser);
        return modelMapper.map(newUser, GetUserDto.class);
    }

    @Override
    public ResponseEntity<GetUserDto> updateUser(Long id, PutUserDto putUserDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        return null;
    }
}
