package services;

import models.Station;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {
    private final List<Station> stations;

    public StationService() {
        stations = new ArrayList<>();
        stations.add(new Station(48, "İSTANBUL(PENDİK)", "Istanbul-Pendik"));
        stations.add(new Station(98, "ANKARA GAR", "Ankara"));
        stations.add(new Station(1306, "ERYAMAN YHT", "Ankara-Eryaman"));
        stations.add(new Station(93, "ESKİŞEHİR", "Eskisehir")); // New city
    }

    public List<Station> getStations() {
        return stations;
    }

    public void addStation(Station station) {
        stations.add(station);
    }
}