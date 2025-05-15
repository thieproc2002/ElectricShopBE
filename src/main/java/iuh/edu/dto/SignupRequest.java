
package iuh.edu.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean gender;
    private Boolean status;
    private String image;
    private LocalDate registerDate;

    private Set<String> role;

}
