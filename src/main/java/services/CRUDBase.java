package services;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import models.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import helpers.HeaderBuilder;

import static helpers.DateFormat.updateDateFormat;

@Service
public class CRUDBase {

    @Autowired
    private HeaderBuilder headerBuilder;

    private static final String BASE_URL = "https://web-api-prod-ytp.tcddtasimacilik.gov.tr";
    private static final String SEARCH_TRIP_URL = BASE_URL + "/tms/train/train-availability?environment=dev&userId=1";
    private static final String LOAD_TRAIN_URL = BASE_URL + "/tms/seat-maps/load-by-train-id";
    private static final String SELECT_SEAT_URL = BASE_URL + "/tms/inventory/select-seat";


public Response getAllTrips(String chosenDate, Station departureStation, Station arrivalStation) {
    chosenDate = updateDateFormat(chosenDate);
    Headers headers = headerBuilder.getHeaders(251);

    return RestAssured.given()
            .headers(headers)
            .body("{\n" +
                    "  \"searchRoutes\": [\n" +
                    "    {\n" +
                    "      \"departureStationId\": " + departureStation.getId() + ",\n" +
                    "      \"departureStationName\": \"" + departureStation.getName() + "\",\n" +
                    "      \"arrivalStationId\": " + arrivalStation.getId() + ",\n" +
                    "      \"arrivalStationName\": \"" + arrivalStation.getName() + "\",\n" +
                    "      \"departureDate\": \"" + chosenDate + "-2025 21:00:00\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"passengerTypeCounts\": [\n" +
                    "    {\n" +
                    "      \"id\": 0,\n" +
                    "      \"count\": 1\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"searchReservation\": false\n" +
                    "}")
            .post(SEARCH_TRIP_URL)
            .andReturn();
}

    public Response loadTrainByTrainId(long trainId) {
        Headers headers = headerBuilder.getHeaders(66);

        return RestAssured.given()
                .headers(headers)
                .body("{\n" +
                        "  \"fromStationId\": 48,\n" +
                        "  \"toStationId\": 98,\n" +
                        "  \"trainId\": "+trainId+",\n" +
                        "  \"legIndex\": 0\n" +
                        "}")
                .post(LOAD_TRAIN_URL)
                .andReturn();
    }

    public Response selectSeat(String seatNumber) {
        Headers headers = headerBuilder.getHeaders(150); // Adjust the content length as needed

        return RestAssured.given()
                .headers(headers)
                .body("{\n" +
                        "  \"trainCarId\": 283169,\n" +
                        "  \"fromStationId\": 48,\n" +
                        "  \"toStationId\": 98,\n" +
                        "  \"gender\": \"M\",\n" +
                        "  \"seatNumber\": \"" + seatNumber + "\",\n" +
                        "  \"passengerTypeId\": 0,\n" +
                        "  \"totalPassengerCount\": 1,\n" +
                        "  \"fareFamilyId\": 0\n" +
                        "}")
                .post(SELECT_SEAT_URL)
                .andReturn();
    }

}
