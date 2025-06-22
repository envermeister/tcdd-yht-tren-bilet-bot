package ui.views;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import ui.VaadinApplication;
import io.restassured.response.Response;
import models.Station;
import services.AlertService;
import services.CRUDBase;
import services.StationService;
import services.TelegramService;
import org.springframework.boot.SpringApplication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import com.vaadin.flow.component.html.Span;

@Route("")
@JavaScript("./src/text-area-util.js")
@CssImport("./styles/styles.css")
@Uses(Dialog.class)
public class MainView extends VerticalLayout {
    private final TextArea resultsArea = new TextArea("Results");
    private final AtomicBoolean monitoring = new AtomicBoolean(false);
    private final AtomicInteger logLineCounter = new AtomicInteger(1);
    private final int MAX_LOG_LINES = 30;
    private ScheduledExecutorService scheduler;
    private final AlertService alertService = new AlertService(true);
    private final CRUDBase crudBase = new CRUDBase();
    private final StationService stationService = new StationService();
    private final TelegramService telegramService = new TelegramService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final List<String> logLines = new ArrayList<>();
    private final TextField maxTimeField = new TextField("Maximum departure time");
    private final AtomicBoolean alarmsEnabled = new AtomicBoolean(true);

    public MainView() {
        setAlignItems(Alignment.CENTER);

        Button killButton = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        killButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        killButton.getStyle().set("position", "absolute");
        killButton.getStyle().set("top", "10px");
        killButton.getStyle().set("right", "10px");
        killButton.addClickListener(e -> {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            } catch (Exception ex) {
                logToUI("Error: " + ex.getMessage());
            }
            SpringApplication.exit(VaadinApplication.getContext(), () -> 0);
            System.exit(0);
        });

        add(killButton);

        H2 title = new H2("YHT Ticket Alarm");

        // Station Selection
        List<Station> stations = stationService.getStations();

        ComboBox<Station> departureBox = new ComboBox<>("Departure Station");
        departureBox.setItems(stations);
        departureBox.setItemLabelGenerator(Station::getCityName);

        ComboBox<Station> arrivalBox = new ComboBox<>("Arrival Station");
        arrivalBox.setItemLabelGenerator(Station::getCityName);

        // Custom Station Button
        Button addCustomStationBtn = createCustomStationDialog(departureBox, arrivalBox);

        // Create a layout for the stations and custom station button
        HorizontalLayout stationLayout = new HorizontalLayout(departureBox, arrivalBox);
        stationLayout.setAlignItems(Alignment.BASELINE);

        HorizontalLayout stationControlLayout = new HorizontalLayout(stationLayout, addCustomStationBtn);
        stationControlLayout.setAlignItems(Alignment.BASELINE);

        departureBox.addValueChangeListener(event -> {
            Station selectedDeparture = event.getValue();
            if (selectedDeparture != null) {
                arrivalBox.setItems(stations.stream()
                        .filter(station -> !station.equals(selectedDeparture))
                        .toList());
            } else {
                arrivalBox.clear();
                arrivalBox.setItems(stations);
            }
        });

        DatePicker datePicker = new DatePicker("Select Date");
        datePicker.setPlaceholder("DD-MM");
        datePicker.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                String formattedDate = event.getValue().format(DateTimeFormatter.ofPattern("dd-MM"));
                logToUI("Selected date: " + formattedDate);
            }
        });

        TextField timeField = new TextField("Minimum departure time");
        timeField.setValue("06:00");
        maxTimeField.setValue("23:59");

        Checkbox logCheckbox = new Checkbox("Log all available trains");
        logCheckbox.setValue(true);

        Checkbox alarmToggle = new Checkbox("Enable Alarms");
        alarmToggle.setValue(true);
        alarmToggle.addValueChangeListener(event -> alarmsEnabled.set(event.getValue()));

        Button startButton = new Button("Start Monitoring");
        startButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button stopButton = new Button("Stop");
        stopButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        stopButton.setEnabled(false);

        resultsArea.setReadOnly(true);
        resultsArea.setHeight("500px");
        resultsArea.setWidth("100%");
        resultsArea.addClassName("results-area");
        resultsArea.setId("log-text-area");

        // Disclaimer label below the results area
        Span disclaimer = new Span("Disclaimer: At high values, available seat count may be incorrect by a small margin. At low numbers, this should not be a problem.");
        disclaimer.getStyle().set("font-size", "12px").set("color", "gray").set("margin-top", "4px");

        Button clearLogsButton = new Button("Clear Logs");
        clearLogsButton.addClickListener(e -> {
            resultsArea.clear();
            logLines.clear();
            logLineCounter.set(1);
            logToUI("Logs cleared at " + LocalDateTime.now().format(timeFormatter));
        });

        Button linkButton = new Button("Open TCDD Ticket Page", event -> getUI().ifPresent(ui ->
                ui.getPage().open("https://ebilet.tcddtasimacilik.gov.tr/sefer-listesi", "_blank")
        ));
        linkButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        startButton.addClickListener(e -> {
            if (!monitoring.getAndSet(true)) {
                Station departure = departureBox.getValue();
                Station arrival = arrivalBox.getValue();
                String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM"));
                String minTime = timeField.getValue();
                String maxTime = maxTimeField.getValue();
                boolean logAllTrains = logCheckbox.getValue();

                if (departure == null || arrival == null) {
                    Notification.show("Please select both departure and arrival cities.", 3000, Notification.Position.MIDDLE);
                    monitoring.set(false);
                    return;
                }

                scheduler = Executors.newScheduledThreadPool(1);
                startMonitoring(date, departure, arrival, minTime, maxTime, logAllTrains);

                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                logToUI("Monitoring started at " + LocalDateTime.now().format(timeFormatter));
            }
        });

        stopButton.addClickListener(e -> {
            if (monitoring.getAndSet(false)) {
                if (scheduler != null && !scheduler.isShutdown()) {
                    scheduler.shutdownNow();
                }
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                logToUI("Monitoring stopped at " + LocalDateTime.now().format(timeFormatter));
            }
        });

        add(
                title,
                stationControlLayout,
                datePicker,
                timeField,
                maxTimeField,
                logCheckbox,
                alarmToggle,
                startButton,
                stopButton,
                clearLogsButton,
                linkButton,
                resultsArea,
                disclaimer // add disclaimer just below resultsArea
        );
    }

    private void startMonitoring(String date, Station departure, Station arrival, String minTime, String maxTime, boolean logAllTrains) {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
        }

        scheduler.scheduleAtFixedRate(() -> {
            if (!monitoring.get()) {
                return;
            }

            try {
                logToUI("Checking trips from " + departure.getCityName() + " to " + arrival.getCityName() + " at " + LocalDateTime.now().format(timeFormatter));

                Response response = crudBase.getAllTrips(date, departure, arrival);

                if (response == null || response.getStatusCode() != 200) {
                    logToUI("Warning: Invalid response from server", true);
                    return;
                }

                List<String> logs = alertService.checkAndAlertForAvailability(response.getBody(), minTime, maxTime, alarmsEnabled.get());

                if (!logs.isEmpty()) {
                    boolean hasAlerts = false;
                    for (String log : logs) {
                        if (log.startsWith("ALERT:")) {
                            hasAlerts = true;
                            logToUI(log, true);
                        } else if (log.startsWith("INFO:") && logAllTrains) {
                            logToUI(log, false);
                        }
                    }

                    if (hasAlerts) {
                        getUI().ifPresent(ui -> ui.access(() -> Notification.show("Empty seats found! Check results.", 5000, Notification.Position.TOP_CENTER)));
                        
                        // Telegram bildirimi gönder
                        if (telegramService.isConfigured()) {
                            String telegramMessage = "🚄 BOŞ KOLTUK BULUNDU!\n\n" +
                                                   "🚉 " + departure.getCityName() + " → " + arrival.getCityName() + "\n" +
                                                   "📅 " + date + "\n" +
                                                   "⏰ Detaylar web arayüzünde\n\n" +
                                                   "🔗 https://ebilet.tcddtasimacilik.gov.tr/sefer-listesi";
                            telegramService.sendMessage(telegramMessage);
                        }
                    }
                } else if (logAllTrains) {
                    logToUI("No available seats found at this time");
                }
            } catch (Exception ex) {
                logToUI("Error: " + ex.getMessage());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void logToUI(String message) {
        logToUI(message, false);
    }

    private void logToUI(String message, boolean isAlert) {
        getUI().ifPresent(ui -> ui.access(() -> {
            String timestamp = "[" + LocalDateTime.now().format(timeFormatter) + "] ";
            int lineNumber = logLineCounter.getAndIncrement();
            String formattedMessage;

            if (isAlert) {
                formattedMessage = String.format("%04d: %s%s", lineNumber, timestamp, message);
                logLines.add(formattedMessage + " [ALERT]");
            } else {
                formattedMessage = String.format("%04d: %s%s", lineNumber, timestamp, message);
                logLines.add(formattedMessage);
            }

            if (logLines.size() > MAX_LOG_LINES) {
                logLines.clear();
                String cleanupMessage = String.format("%04d: %s%s",
                    logLineCounter.getAndIncrement(),
                    "[" + LocalDateTime.now().format(timeFormatter) + "] ",
                    "Cleaned old log entries to prevent memory issues");
                logLines.add(cleanupMessage);
            }

            StringBuilder sb = new StringBuilder();
            for (String line : logLines) {
                sb.append(line).append("\n");
            }
            resultsArea.setValue(sb.toString());

            ui.getPage().executeJs("setTimeout(function() { document.getElementById($0).scrollTop = document.getElementById($0).scrollHeight; }, 100);", resultsArea.getId().orElse("log-text-area"));

            ui.push();
        }));
    }



    // Custom Stations
    private Button createCustomStationDialog(ComboBox<Station> departureBox, ComboBox<Station> arrivalBox) {
        Button addCustomStationBtn = new Button("Add Custom Station");
        addCustomStationBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        addCustomStationBtn.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Add Custom Station");

            TextField stationIdField = new TextField("Station ID");
            stationIdField.setHelperText("Enter numeric station ID");

            TextField stationNameField = new TextField("Station Name");
            stationNameField.setHelperText("Official station name (e.g., KONYA YHT)");

            TextField cityNameField = new TextField("City Name");
            cityNameField.setHelperText("Display name (e.g., Konya)");

            VerticalLayout dialogLayout = new VerticalLayout(
                    stationIdField, stationNameField, cityNameField);
            dialogLayout.setPadding(true);
            dialogLayout.setSpacing(true);

            dialog.add(dialogLayout);

            Button saveButton = new Button("Save", event -> {
                try {
                    int stationId = Integer.parseInt(stationIdField.getValue().trim());
                    String stationName = stationNameField.getValue().trim();
                    String cityName = cityNameField.getValue().trim();

                    if (stationName.isEmpty() || cityName.isEmpty()) {
                        Notification.show("All fields are required", 3000, Notification.Position.MIDDLE);
                        return;
                    }

                    Station newStation = new Station(stationId, stationName, cityName);
                    stationService.addStation(newStation);

                    // Update ComboBoxes with new station
                    List<Station> updatedStations = stationService.getStations();
                    Station selectedDeparture = departureBox.getValue();
                    departureBox.setItems(updatedStations);

                    if (selectedDeparture != null) {
                        arrivalBox.setItems(updatedStations.stream()
                                .filter(station -> !station.equals(selectedDeparture))
                                .toList());
                    } else {
                        arrivalBox.setItems(updatedStations);
                    }

                    dialog.close();
                    logToUI("Custom station added: " + cityName);

                } catch (NumberFormatException ex) {
                    Notification.show("Please enter a valid numeric ID", 3000, Notification.Position.MIDDLE);
                }
            });

            Button cancelButton = new Button("Cancel", event -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            dialog.getFooter().add(cancelButton, saveButton);
            dialog.open();
        });

        return addCustomStationBtn;
    }


}
