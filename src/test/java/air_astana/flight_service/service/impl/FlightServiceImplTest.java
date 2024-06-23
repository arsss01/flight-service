package air_astana.flight_service.service.impl;

import air_astana.flight_service.exceptions.GlobalException;
import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import air_astana.flight_service.models.entity.Flight;
import air_astana.flight_service.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFlight_Success() {
        FlightDto flightDto = new FlightDto();
        Flight flight = new Flight();

        when(mapper.map(flightDto, Flight.class)).thenReturn(flight);
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        flightService.createFlight(flightDto);

        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    public void testCreateFlight_NullFlight() {
        Exception exception = assertThrows(GlobalException.class, () -> flightService.createFlight(null));
        assertThat(exception.getMessage()).isEqualTo("flight is null");
    }

    @Test
    public void testUpdateFlight_Success() {
        Integer flightId = 1;
        Status newStatus = Status.IN_TIME;
        Flight existingFlight = new Flight();
        existingFlight.setId(flightId);

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));
        when(mapper.map(existingFlight, Flight.class)).thenReturn(existingFlight);

        flightService.updateFlight(flightId, newStatus);

        ArgumentCaptor<Flight> flightCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository, times(1)).save(flightCaptor.capture());
        assertThat(flightCaptor.getValue().getStatus()).isEqualTo(newStatus);
    }

    @Test
    public void testUpdateFlight_FlightNotFound() {
        Integer flightId = 1;
        Status newStatus = Status.IN_TIME;

        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(GlobalException.class, () -> flightService.updateFlight(flightId, newStatus));
        assertThat(exception.getMessage()).isEqualTo("Flight by id not found");
    }

    @Test
    public void testGetAllFlights_WithSearchQuery() {
        String searchQuery = "test";
        Pageable pageable = mock(Pageable.class);
        Page<Flight> flightPage = new PageImpl<>(Collections.emptyList());
        Specification<Flight> spec = mock(Specification.class);

        when(flightRepository.buildSpecification(searchQuery)).thenReturn(spec);
        when(flightRepository.findAll(spec, pageable)).thenReturn(flightPage);

        Page<FlightDto> result = flightService.getAllFlights(searchQuery, pageable);

        assertThat(result).isEmpty();
        verify(flightRepository, times(1)).findAll(spec, pageable);
    }

    @Test
    public void testGetAllFlights_WithoutSearchQuery() {
        Pageable pageable = mock(Pageable.class);
        Page<Flight> flightPage = new PageImpl<>(Collections.emptyList());

        when(flightRepository.findAll(pageable)).thenReturn(flightPage);

        Page<FlightDto> result = flightService.getAllFlights(null, pageable);

        assertThat(result).isEmpty();
        verify(flightRepository, times(1)).findAll(pageable);
    }
}
