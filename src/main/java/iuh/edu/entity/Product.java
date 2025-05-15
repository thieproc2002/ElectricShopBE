
package iuh.edu.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private String normalizedName;
    private int quantity;
    private Double price;
    private int discount;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate enteredDate;
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean status;
    private int sold;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    public boolean isStatus() {
        return Boolean.TRUE.equals(this.status);
    }
    @Override
    public String toString() {
        return "Product [productId=" + productId + ", name=" + name + ", normalizedname=" + normalizedName + ", quantity=" + quantity + ", price=" + price
                + ", discount=" + discount + ", image=" + image + ", description=" + description + ", enteredDate="
                + enteredDate + ", status=" + status + ", sold=" + sold + ", category=" + category + "]";
    }

}
