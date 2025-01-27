package uni.simulatedpos.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.cafemanagement.model.Role;
import uni.cafemanagement.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employee extends User {
    private String position;

    public Employee(String username, String password, String firstName, String lastName, String position) {
        super(username, password, firstName, lastName);
        this.setRole(Role.USER);
        this.position = position;
    }
}