package services;

import models.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import io.restassured.response.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class HeadlessRunner implements CommandLineRunner {

    @Autowired
    private AlertService alertService;

    @Autowired
    private CRUDBase crudBase;

    @Autowired
    private StationService stationService;

    @Autowired
    private TelegramService telegramService;

    private final AtomicBoolean monitoring = new AtomicBoolean(false);
    private ScheduledExecutorService scheduler;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void run(String... args) throws Exception {
        // Eğer headless argümanı varsa terminal modunda çalış
        if (args.length > 0 && "--headless".equals(args[0])) {
            System.out.println("=== TCDD Tren Bileti Takip Botu - Terminal Modu ===");
            System.out.println("Telegram bildirimleri aktif!");
            System.out.println();
            
            runHeadlessMode();
        }
    }

    private void runHeadlessMode() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("\n=== MENÜ ===");
                System.out.println("1. Takip başlat");
                System.out.println("2. Takip durdur");
                System.out.println("3. İstasyon listesi göster");
                System.out.println("4. Çıkış");
                System.out.print("Seçiminiz (1-4): ");
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        if (!monitoring.get()) {
                            startMonitoringHeadless(scanner);
                        } else {
                            System.out.println("Takip zaten aktif!");
                        }
                        break;
                    case "2":
                        stopMonitoring();
                        break;
                    case "3":
                        showStations();
                        break;
                    case "4":
                        System.out.println("Çıkış yapılıyor...");
                        if (scheduler != null && !scheduler.isShutdown()) {
                            scheduler.shutdownNow();
                        }
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Geçersiz seçim! Lütfen 1-4 arası bir sayı girin.");
                }
            } catch (Exception e) {
                System.err.println("Hata: " + e.getMessage());
            }
        }
    }

    private void startMonitoringHeadless(Scanner scanner) {
        try {
            // İstasyon seçimi
            List<Station> stations = stationService.getStations();
            
            System.out.println("\n=== KALKIŞ İSTASYONU SEÇİMİ ===");
            showStationsWithNumbers(stations);
            System.out.print("Kalkış istasyonu numarası: ");
            int departureIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (departureIndex < 0 || departureIndex >= stations.size()) {
                System.out.println("Geçersiz istasyon numarası!");
                return;
            }
            
            Station departure = stations.get(departureIndex);
            
            System.out.println("\n=== VARIŞ İSTASYONU SEÇİMİ ===");
            showStationsWithNumbers(stations);
            System.out.print("Varış istasyonu numarası: ");
            int arrivalIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            
            if (arrivalIndex < 0 || arrivalIndex >= stations.size() || arrivalIndex == departureIndex) {
                System.out.println("Geçersiz istasyon numarası!");
                return;
            }
            
            Station arrival = stations.get(arrivalIndex);
            
            // Tarih seçimi
            System.out.print("\nTarih (DD-MM formatında, örn: 25-12): ");
            String date = scanner.nextLine().trim();
            
            // Saat aralığı
            System.out.print("Minimum kalkış saati (örn: 06:00): ");
            String minTime = scanner.nextLine().trim();
            if (minTime.isEmpty()) minTime = "06:00";
            
            System.out.print("Maksimum kalkış saati (örn: 23:59): ");
            String maxTime = scanner.nextLine().trim();
            if (maxTime.isEmpty()) maxTime = "23:59";
            
            // Telegram ayarları
            System.out.print("Telegram bildirimleri aktif mi? (E/H) [E]: ");
            String telegramChoice = scanner.nextLine().trim().toUpperCase();
            boolean telegramEnabled = telegramChoice.isEmpty() || telegramChoice.equals("E");
            
            // Takibi başlat
            monitoring.set(true);
            scheduler = Executors.newScheduledThreadPool(1);
            
            // Lambda expression için final değişkenler
            final Station finalDeparture = departure;
            final Station finalArrival = arrival;
            final String finalDate = date;
            final String finalMinTime = minTime;
            final String finalMaxTime = maxTime;
            final boolean finalTelegramEnabled = telegramEnabled;
            
            System.out.println("\n=== TAKİP BAŞLATILDI ===");
            System.out.println("Kalkış: " + departure.getCityName());
            System.out.println("Varış: " + arrival.getCityName());
            System.out.println("Tarih: " + date);
            System.out.println("Saat Aralığı: " + minTime + " - " + maxTime);
            System.out.println("Telegram: " + (telegramEnabled ? "Aktif" : "Pasif"));
            System.out.println("Kontrol Aralığı: 5 saniye");
            System.out.println("\nTakibi durdurmak için '2' seçeneğini kullanın.");
            System.out.println("=========================");
            
            scheduler.scheduleAtFixedRate(() -> {
                if (!monitoring.get()) {
                    return;
                }
                
                try {
                    String timestamp = LocalDateTime.now().format(timeFormatter);
                    System.out.println("[" + timestamp + "] Kontrol ediliyor: " + 
                                     finalDeparture.getCityName() + " -> " + finalArrival.getCityName());
                    
                    Response response = crudBase.getAllTrips(finalDate, finalDeparture, finalArrival);
                    
                    if (response == null || response.getStatusCode() != 200) {
                        System.out.println("[" + timestamp + "] UYARI: Sunucudan geçersiz yanıt!");
                        return;
                    }
                    
                    List<String> logs = alertService.checkAndAlertForAvailability(
                        response.getBody(), finalMinTime, finalMaxTime, true);
                    
                    boolean foundSeats = false;
                    for (String log : logs) {
                        if (log.startsWith("ALERT:")) {
                            foundSeats = true;
                            System.out.println("[" + timestamp + "] 🎉 " + log);
                            
                            // Telegram bildirimi gönder
                            if (finalTelegramEnabled) {
                                String message = "🚄 BOŞ KOLTUK BULUNDU!\n\n" +
                                               "🚉 " + finalDeparture.getCityName() + " → " + finalArrival.getCityName() + "\n" +
                                               "📅 " + finalDate + "\n" +
                                               "⏰ " + log.substring(7) + "\n\n" +
                                               "🔗 https://ebilet.tcddtasimacilik.gov.tr/sefer-listesi";
                                telegramService.sendMessage(message);
                            }
                        } else if (log.startsWith("INFO:")) {
                            System.out.println("[" + timestamp + "] ℹ️  " + log);
                        }
                    }
                    
                    if (!foundSeats && logs.isEmpty()) {
                        System.out.println("[" + timestamp + "] Boş koltuk bulunamadı");
                    }
                    
                } catch (Exception ex) {
                    String timestamp = LocalDateTime.now().format(timeFormatter);
                    System.err.println("[" + timestamp + "] HATA: " + ex.getMessage());
                    
                    // Hata durumunda da Telegram bildirimi
                    if (finalTelegramEnabled) {
                        telegramService.sendMessage("❌ TCDD Bot Hatası: " + ex.getMessage());
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
            
        } catch (Exception e) {
            System.err.println("Takip başlatılırken hata: " + e.getMessage());
            monitoring.set(false);
        }
    }

    private void stopMonitoring() {
        if (monitoring.getAndSet(false)) {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }
            System.out.println("Takip durduruldu.");
        } else {
            System.out.println("Aktif takip bulunamadı.");
        }
    }

    private void showStations() {
        List<Station> stations = stationService.getStations();
        System.out.println("\n=== İSTASYON LİSTESİ ===");
        showStationsWithNumbers(stations);
    }

    private void showStationsWithNumbers(List<Station> stations) {
        for (int i = 0; i < stations.size(); i++) {
            System.out.println((i + 1) + ". " + stations.get(i).getCityName());
        }
    }
}

