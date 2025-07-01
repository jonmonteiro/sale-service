package sale_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import sale_service.dto.ProductDTO;
import sale_service.entities.SaleProduct;
import sale_service.repository.ProductRepository;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public SaleProduct createProduct(ProductDTO productDTO) throws JsonProcessingException {
        SaleProduct savedProduct = productRepository.save(new SaleProduct(productDTO));
        
        String productJson = objectMapper.writeValueAsString(savedProduct);
        kafkaTemplate.send("auth-topic", productJson);
        
        return savedProduct;
    }

    public void deleteProduct(Long id) throws JsonProcessingException {
        String delete = objectMapper.writeValueAsString(new ProductDTO(id, null, null, null));
        kafkaTemplate.send("auth-topic", delete);
        
        productRepository.deleteById(id);
    }

    public SaleProduct updateProduct(Long id, ProductDTO productDTO) throws JsonProcessingException {
        return productRepository.findById(id)
            .map(product -> {
                product.setName(productDTO.name());
                product.setPrice(productDTO.price());
                product.setQuantity(productDTO.quantity());
                SaleProduct updatedProduct = productRepository.save(product);
                try {
                    String productJson = objectMapper.writeValueAsString(updatedProduct);
                    kafkaTemplate.send("auth-topic", productJson);
                    return updatedProduct;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public List<SaleProduct> getAllProducts() {
        return productRepository.findAll();
    }

    public SaleProduct getProduct(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }
}
