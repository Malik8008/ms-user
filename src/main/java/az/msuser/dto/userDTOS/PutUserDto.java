package az.msuser.dto.userDTOS;

import lombok.Data;

@Data
public class PutUserDto {
    Long id;
    String name;
    String email;
    String password;
    String phone;
}
