package services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TelegramService {
    
    private static final Logger logger = Logger.getLogger(TelegramService.class.getName());
    
    @Value("${telegram.bot.token:}")
    private String botToken;
    
    @Value("${telegram.chat.id:}")
    private String chatId;
    
    public void sendMessage(String message) {
        if (botToken.isEmpty() || chatId.isEmpty()) {
            logger.log(Level.WARNING, "Telegram bot token veya chat ID tanımlanmamış. Mesaj gönderilemiyor.");
            System.out.println("⚠️  Telegram ayarları eksik - mesaj gönderilemiyor");
            return;
        }
        
        try {
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            
            String requestBody = "{\n" +
                    "  \"chat_id\": \"" + chatId + "\",\n" +
                    "  \"text\": \"" + escapeJson(message) + "\",\n" +
                    "  \"parse_mode\": \"HTML\"\n" +
                    "}";
            
            var response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post(url);
            
            if (response.getStatusCode() == 200) {
                logger.log(Level.INFO, "Telegram mesajı başarıyla gönderildi");
                System.out.println("📱 Telegram bildirimi gönderildi");
            } else {
                logger.log(Level.WARNING, "Telegram mesajı gönderilemedi. Status: " + response.getStatusCode());
                System.out.println("⚠️  Telegram mesajı gönderilemedi: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Telegram mesajı gönderilirken hata: " + e.getMessage(), e);
            System.err.println("❌ Telegram hatası: " + e.getMessage());
        }
    }
    
    public boolean isConfigured() {
        return !botToken.isEmpty() && !chatId.isEmpty();
    }
    
    public void testConnection() {
        if (!isConfigured()) {
            System.out.println("❌ Telegram ayarları eksik!");
            System.out.println("application.properties dosyasına şunları ekleyin:");
            System.out.println("telegram.bot.token=YOUR_BOT_TOKEN");
            System.out.println("telegram.chat.id=YOUR_CHAT_ID");
            return;
        }
        
        sendMessage("🤖 TCDD Bot test mesajı - Bağlantı başarılı!");
    }
    
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}

