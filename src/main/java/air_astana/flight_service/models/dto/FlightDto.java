package air_astana.flight_service.models.dto;

import air_astana.flight_service.models.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Integer id;
    private String origin;
    private String destination;
    private Date departure;
    private Date arrival;
    private Status status;
}
