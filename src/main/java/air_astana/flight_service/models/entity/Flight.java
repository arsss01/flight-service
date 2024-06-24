package air_astana.flight_service.models.entity;

import air_astana.flight_service.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "flights_id_seq")
    @SequenceGenerator(name = "flights_id_seq", sequenceName = "flights_id_seq", allocationSize = 1)
    private Integer id;
    private String origin;
    private String destination;
    private OffsetDateTime departure;
    private OffsetDateTime arrival;
    @Enumerated(EnumType.STRING)
    private Status status;
}
