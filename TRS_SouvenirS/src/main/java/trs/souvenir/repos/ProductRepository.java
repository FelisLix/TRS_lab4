package trs.souvenir.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import trs.souvenir.data.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}