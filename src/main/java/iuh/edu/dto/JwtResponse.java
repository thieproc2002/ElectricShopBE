
package iuh.edu.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Boolean gender;
    private Boolean status;
    private String image;
    private LocalDate registerDate;

    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String name, String email, String password, String phone,
                       String address, Boolean gender, Boolean status, String image, LocalDate registerDate, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.name = name;
        this.email = email;
        this.registerDate = registerDate;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.status = status;
        this.image = image;
        this.roles = roles;
    }

}
