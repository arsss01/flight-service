package air_astana.flight_service.service.impl;

import air_astana.flight_service.exceptions.GlobalException;
import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import air_astana.flight_service.models.entity.Flight;
import air_astana.flight_service.repository.FlightRepository;
import air_astana.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper mapper;

    @Override
    public void createFlight(FlightDto flight) {
        if (flight == null) {
            throw new GlobalException("flight is null");
        }
        flightRepository.save(mapper.map(flight, Flight.class));
    }

    @Override
    public void updateFlight(Integer id, Status status) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Flight by id not found",
                        Map.of("id", id)));
        flight.setStatus(status);
        flightRepository.save(mapper.map(flight, Flight.class));
    }

    @Override
    public Page<FlightDto> getAllFlights(String searchQuery, Pageable pageable) {
        Page<Flight> flights;
        if (searchQuery != null) {
            Specification<Flight> spec = flightRepository.buildSpecification(searchQuery);
            flights= flightRepository.findAll(spec, pageable);
        } else {
            flights = flightRepository.findAll(pageable);
        }
        List<FlightDto> flightDtos = flights.stream()
                .map(flight -> mapper.map(flight, FlightDto.class))
                .toList();
        return new PageImpl<>(flightDtos, pageable, flights.getTotalElements());
    }
}
