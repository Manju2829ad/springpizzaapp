package basepackage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String size;
    private String type;
    private Double price;

    @JsonBackReference(value = "prices-pizza")  // Prevents recursion by not serializing the back reference to Pizza
    @ManyToOne
    private Pizza pizza;
    
    
 // In Price.java
    @Override
    public String toString() {
        return "Price{" +
               "id=" + pid +
               ", size='" + size + '\'' +
               ", price=" + price +
               // Avoid calling pizza's toString to prevent recursion
               '}';
    }
    
}
