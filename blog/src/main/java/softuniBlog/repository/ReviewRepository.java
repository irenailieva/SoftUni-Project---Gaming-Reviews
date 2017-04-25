package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
