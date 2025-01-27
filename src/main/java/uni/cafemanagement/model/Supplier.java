package uni.cafemanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Supplier {
    @Id
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;

}