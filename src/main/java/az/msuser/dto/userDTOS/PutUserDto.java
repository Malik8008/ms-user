package az.msuser.dto.userDTOS;

import az.msuser.Enum.UserRole;
import lombok.Data;

@Data
public class PutUserDto {
    String name;
    String email;
    String password;
    String phone;
    UserRole role;
}
