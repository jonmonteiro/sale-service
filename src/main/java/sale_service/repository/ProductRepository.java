package sale_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sale_service.entities.SaleProduct;

public interface ProductRepository extends JpaRepository<SaleProduct, Long> {

}
