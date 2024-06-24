package air_astana.flight_service.service;

import air_astana.flight_service.exceptions.GlobalException;
import org.threeten.bp.ZoneId;
import java.util.Set;

public class Time4JTimezoneService {
    public String getTimezone(String city) {
        try {
            Set<String> allZoneIds = ZoneId.getAvailableZoneIds();

            for (String zoneId : allZoneIds) {
                if (zoneId.toLowerCase().contains(city.toLowerCase())) {
                    return zoneId;
                }
            }

            throw new GlobalException("Unknown time-zone ID for city: " + city);
        } catch (GlobalException e) {
            throw new GlobalException("Error while fetching timezone for city: " + city);
        }
    }
}
