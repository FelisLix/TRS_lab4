package trs.souvenir.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import trs.souvenir.data.Product;
import trs.souvenir.data.Type;
import trs.souvenir.repos.CategoryRepository;
import trs.souvenir.repos.ProductRepository;
import trs.souvenir.repos.TypeRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final TypeRepository typeRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(int productId, String name, double price, Integer quantity,
                              String size, String description,
                              int lifespan, String origin) {
        Optional<Product> product = productRepository.findById(productId);
        product.ifPresent(p -> {
            p.setName(name);
            p.setPrice(BigDecimal.valueOf(price));
            p.setQuantity(quantity);
            p.setSize(size);
            p.setDescription(description);
            p.setLifespan(lifespan);
            p.setOrigin(origin);
            productRepository.save(p);
        });
    }

    public void addProduct(String name, int typeId, double price, Integer quantity,
                           String size, String description, Timestamp createdAt,
                           int lifespan, String origin) {
        Type type = typeRepository.findById(typeId).orElseThrow(() -> new IllegalArgumentException("Invalid type ID"));
        Product product = new Product(
                null,
                name,
                type,
                BigDecimal.valueOf(price),
                quantity,
                size,
                description,
                createdAt.toInstant(),
                lifespan,
                origin
        );
        productRepository.save(product);
    }

}
