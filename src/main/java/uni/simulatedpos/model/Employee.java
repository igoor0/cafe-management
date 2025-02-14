package uni.simulatedpos.model;

import jakarta.persistence.Entity;
import lombok.*;
import uni.cafemanagement.model.Role;
import uni.cafemanagement.model.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee extends User {
    private String position;
    private String phoneNumber;

    public Employee(String username, String password, String firstName, String lastName, String position, String phoneNumber) {
        super(username, password, firstName, lastName);
        this.setRole(Role.USER);
        this.position = position;
        this.phoneNumber = phoneNumber;
    }

}