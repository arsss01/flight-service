package air_astana.flight_service.controller;

import air_astana.flight_service.models.Status;
import air_astana.flight_service.models.dto.FlightDto;
import air_astana.flight_service.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/flights")
@RequiredArgsConstructor
public class FlightControllerAdmin {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<?> createFlight(@RequestBody FlightDto flight) {
        flightService.createFlight(flight);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlightStatus(@PathVariable Integer id, @RequestParam Status status) {
        flightService.updateFlightStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
