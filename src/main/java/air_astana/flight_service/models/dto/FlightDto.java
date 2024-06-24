package air_astana.flight_service.models.dto;

import air_astana.flight_service.models.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Integer id;
    private String origin;
    private String destination;
    @NotNull
    private OffsetDateTime departure;
    @NotNull
    private OffsetDateTime arrival;
    @NotNull
    private Status status;
}
