package air_astana.flight_service.service.impl;

import air_astana.flight_service.exceptions.GlobalException;
import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import air_astana.flight_service.models.entity.Flight;
import air_astana.flight_service.repository.FlightRepository;
import air_astana.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper mapper;

    @Override
    public void createFlight(FlightDto flight) {
        log.info("The flight creation process starts");
        if (flight == null) {
            throw new GlobalException("flight is null");
        }
        flightRepository.save(mapper.map(flight, Flight.class));
        log.info("The flight creation process has been successfully completed");
    }

    @Override
    public void updateFlight(Integer id, Status status) {
        log.info("The status change process starts");
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Flight by id not found",
                        Map.of("id", id)));
        flight.setStatus(status);
        flightRepository.save(mapper.map(flight, Flight.class));
        log.info("The status change process has been completed successfully");
    }

    @Override
    public Page<FlightDto> getAllFlights(String searchQuery, Pageable pageable) {
        log.info("The process of receiving flights will begin");
        Page<Flight> flights;
        if (searchQuery != null) {
            Specification<Flight> spec = flightRepository.buildSpecification(searchQuery);
            flights= flightRepository.findAll(spec, pageable);
        } else {
            flights = flightRepository.findAll(pageable);
        }
        log.info("The process of obtaining flights has been successfully completed");
        List<FlightDto> flightDtos = flights.stream()
                .map(flight -> mapper.map(flight, FlightDto.class))
                .toList();
        return new PageImpl<>(flightDtos, pageable, flights.getTotalElements());
    }
}
