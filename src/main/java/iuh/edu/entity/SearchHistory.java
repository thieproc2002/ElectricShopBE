package iuh.edu.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchHistory {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String keyword;

    private LocalDateTime searchedAt;
}