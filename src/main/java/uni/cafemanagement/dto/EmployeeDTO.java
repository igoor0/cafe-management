package uni.cafemanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EmployeeDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String position;
    private String phoneNumber;
}