package air_astana.flight_service.service.impl;

import air_astana.flight_service.exceptions.GlobalException;
import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import air_astana.flight_service.models.entity.Flight;
import air_astana.flight_service.repository.FlightRepository;
import air_astana.flight_service.service.FlightService;
import air_astana.flight_service.service.Time4JTimezoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final Time4JTimezoneService time4JTimezoneService = new Time4JTimezoneService();
    private final FlightRepository flightRepository;
    private final ModelMapper mapper;

    @Override
    public void createFlight(FlightDto flight) {
        log.info("The flight creation process starts");

        try {
            String originTimezone = time4JTimezoneService.getTimezone(flight.getOrigin());
            String destinationTimezone = time4JTimezoneService.getTimezone(flight.getDestination());

            OffsetDateTime departureInZone = OffsetDateTime.ofInstant(flight.getDeparture().toInstant(), ZoneId.of(originTimezone));
            OffsetDateTime arrivalInZone = OffsetDateTime.ofInstant(flight.getArrival().toInstant(), ZoneId.of(destinationTimezone));

            flight.setDeparture(departureInZone);
            flight.setArrival(arrivalInZone);

            Flight mappedFlight = mapper.map(flight, Flight.class);
            flightRepository.save(mappedFlight);

            log.info("The flight creation process has been successfully completed");
        } catch (GlobalException e) {
            log.error("Error creating flight: " + e.getMessage(), e);
        }
    }



    @Override
    public void updateFlightStatus(Integer id, Status status) {
        log.info("Process changing status starts");

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Flight by id not found", Map.of("id", id)));
        flight.setStatus(status);
        flightRepository.save(mapper.map(flight, Flight.class));

        log.info("Status changing successfully completed");
    }

    @Override
    public Page<FlightDto> getAllFlights(String origin, String destination, Pageable pageable) {
        log.info("The process of receiving flights will begin");

        Page<Flight> flights;
        if (origin != null && destination != null) {
            Specification<Flight> spec = flightRepository.buildSpecification(origin, destination);
            flights= flightRepository.findAll(spec, pageable);
        } else if (origin != null) {
            Specification<Flight> spec = flightRepository.buildSpecificationOrigin(origin);
            flights= flightRepository.findAll(spec, pageable);
        } else if (destination != null) {
            Specification<Flight> spec = flightRepository.buildSpecificationDestination(destination);
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
