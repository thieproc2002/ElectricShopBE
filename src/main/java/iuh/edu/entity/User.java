
package iuh.edu.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean gender;
    private String image;
    private LocalDate registerDate;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean status;
    private String token;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles = new HashSet<>();

    public User(String name, String email, String password, String phone, String address, Boolean gender,
                Boolean status, String image, LocalDate registerDate, String token) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.status = status;
        this.image = image;
        this.registerDate = registerDate;
        this.token = token;
    }

}
