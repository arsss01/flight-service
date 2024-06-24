package air_astana.flight_service.repository;

import air_astana.flight_service.models.entity.Flight;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer>, JpaSpecificationExecutor<Flight> {
    default Specification<Flight> buildSpecification(String origin, String destination) {
        return (root, query, builder) -> {
            String originVal = "%" + origin.toLowerCase() + "%";
            String destinationVal = "%" + destination.toLowerCase() + "%";
            Predicate originPredicate = builder.like(builder.lower(root.get("origin")), originVal);
            Predicate destinationPredicate = builder.like(builder.lower(root.get("destination")), destinationVal);

            return builder.or(originPredicate, destinationPredicate);
        };
    }

    default Specification<Flight> buildSpecificationOrigin(String origin) {
        return (root, query, builder) -> {
            String originVal = "%" + origin.toLowerCase() + "%";
            Predicate originPredicate = builder.like(builder.lower(root.get("origin")), originVal);

            return builder.or(originPredicate);
        };
    }

    default Specification<Flight> buildSpecificationDestination(String destination) {
        return (root, query, builder) -> {
            String destinationVal = "%" + destination.toLowerCase() + "%";
            Predicate destinationPredicate = builder.like(builder.lower(root.get("destination")), destinationVal);

            return builder.or(destinationPredicate);
        };
    }
}
