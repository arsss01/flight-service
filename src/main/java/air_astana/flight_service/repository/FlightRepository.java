package air_astana.flight_service.repository;

import air_astana.flight_service.models.entity.Flight;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer>, JpaSpecificationExecutor<Flight> {
    default Specification<Flight> buildSpecification(String searchQuery) {
        return (root, query, builder) -> {
            String likePattern = "%" + searchQuery.toLowerCase() + "%";
            Predicate originPredicate = builder.like(builder.lower(root.get("origin")), likePattern);
            Predicate destinationPredicate = builder.like(builder.lower(root.get("destination")), likePattern);

            return builder.or(originPredicate, destinationPredicate);
        };
    }
}
