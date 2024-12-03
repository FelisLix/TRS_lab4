package trs.souvenir.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import trs.souvenir.data.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}