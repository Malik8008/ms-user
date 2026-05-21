package az.msuser.service.impl;

import az.msuser.Enum.UserRole;
import az.msuser.configuration.security.JWTUtil;
import az.msuser.dto.AuthDTOS.LoginDTO;
import az.msuser.dto.userDTOS.GetUserDto;
import az.msuser.dto.userDTOS.PostUserDto;
import az.msuser.dto.userDTOS.PutUserDto;
import az.msuser.exception.IdNotFoundException;
import az.msuser.exception.PhoneNotFoundException;
import az.msuser.exception.UserAlreadyExistException;
import az.msuser.model.User;
import az.msuser.repository.UserRepository;
import az.msuser.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public ResponseEntity<List<GetUserDto>> getUsers() {
        List<GetUserDto> users = userRepository.findAllByDeletedFalse().stream()
                .map(user -> modelMapper.map(user, GetUserDto.class)).toList();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<GetUserDto> findById(Long id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()->new IdNotFoundException("User id not found"));
        return ResponseEntity.ok().body(modelMapper.map(user, GetUserDto.class));
    }

    @Override
    public ResponseEntity<String> loginUser(LoginDTO loginDTO) {
        User existUser = userRepository.findByPhone(loginDTO.getPhone())
                .orElseThrow(()->new PhoneNotFoundException("User with phone " + loginDTO.getPhone() + " not found"));
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
            throw new UserAlreadyExistException("User already exists");
        }
        User newUser= modelMapper.map(postUserDto, User.class);
        newUser.setPhone(postUserDto.getPhone());
        newUser.setPassword(passwordEncoder.encode(postUserDto.getPassword()));
        newUser.setEmail(postUserDto.getEmail());
        newUser.setName(postUserDto.getName());
        newUser.setRole(UserRole.USER);
        userRepository.save(newUser);
        return modelMapper.map(newUser, GetUserDto.class);
    }

    @Override
    public ResponseEntity<GetUserDto> updateUser(Long id, PutUserDto putUserDto, Authentication auth) {
        User existUser = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()-> new IdNotFoundException("User not found"));

        boolean isUser = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_USER"));

        if (isUser) {
            String currentPhone = auth.getName();

            if (!existUser.getPhone().equals(currentPhone)) {
                throw new AccessDeniedException("You can only update yourself");
            }
        }
        existUser.setName(putUserDto.getName());
        existUser.setEmail(putUserDto.getEmail());
        existUser.setPhone(putUserDto.getPhone());
        existUser.setPassword(passwordEncoder.encode(putUserDto.getPassword()));

        User newUser= userRepository.save(existUser);

        GetUserDto response = new GetUserDto();
        response.setId(newUser.getId());
        response.setName(newUser.getName());
        response.setEmail(newUser.getEmail());
        response.setPhone(newUser.getPhone());
        response.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        User existUser = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        existUser.setDeleted(true);
        userRepository.save(existUser);
        return ResponseEntity.noContent().build();
    }
}
