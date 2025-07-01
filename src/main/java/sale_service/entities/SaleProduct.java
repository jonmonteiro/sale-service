package sale_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sale_service.dto.ProductDTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sale_products") 
public class SaleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    private String name;
    private Double price;
    private Integer quantity;

    public SaleProduct(ProductDTO productDTO) {
        this.name = productDTO.name();
        this.price = productDTO.price();
        this.quantity = productDTO.quantity();
    }
}
