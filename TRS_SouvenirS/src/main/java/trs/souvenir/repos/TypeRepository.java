package trs.souvenir.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import trs.souvenir.data.Type;

public interface TypeRepository extends JpaRepository<Type, Integer> {
}