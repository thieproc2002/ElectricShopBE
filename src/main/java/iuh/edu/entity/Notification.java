
package iuh.edu.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Date time;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean status;

}
