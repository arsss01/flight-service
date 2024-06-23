package air_astana.flight_service.service;

import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FlightService {
    void createFlight(FlightDto flight);

    void updateFlight(Integer id, Status status);

    Page<FlightDto> getAllFlights(String searchQuery, Pageable pageable);
}
