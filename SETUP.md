# TCDD Bot - Hızlı Başlangıç Rehberi

## 🚀 5 Dakikada Başlayın!

### 1. Java Kurulumu Kontrol Edin
```bash
java -version
```
Java 17+ gereklidir. Yoksa [buradan](https://adoptium.net/) indirin.

### 2. Bot'u Çalıştırın

#### Web Arayüzü İçin:
```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```
Tarayıcıda: http://localhost:9090

#### Terminal Modu İçin:
```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar --headless
```

### 3. Telegram Bildirimleri (İsteğe Bağlı)

1. [@BotFather](https://t.me/BotFather) ile bot oluşturun
2. [@userinfobot](https://t.me/userinfobot) ile Chat ID öğrenin
3. `application.properties.example` dosyasını `application.properties` olarak kopyalayın
4. Token ve Chat ID'yi dosyaya yazın

### 4. Kullanım

1. Kalkış ve varış istasyonunu seçin
2. Tarih ve saat aralığını belirleyin
3. "Start Monitoring" / "Takip Başlat" seçin
4. Boş koltuk bulunduğunda bildirim alın!

## 📱 Telegram Kurulum Detayları

### Bot Oluşturma:
1. Telegram'da [@BotFather](https://t.me/BotFather) açın
2. `/newbot` yazın
3. Bot adı girin (örn: "TCDD Takip Botum")
4. Kullanıcı adı girin (örn: "tcdd_takip_bot")
5. Verilen token'ı kaydedin

### Chat ID Öğrenme:
1. [@userinfobot](https://t.me/userinfobot) açın
2. `/start` yazın
3. Verilen ID'yi kaydedin

### Konfigürasyon:
```properties
telegram.bot.token=1234567890:ABCdefGHIjklMNOpqrsTUVwxyz
telegram.chat.id=123456789
```

## 🔧 Sorun Giderme

**Java bulunamıyor:** Java 17+ kurun
**Port kullanımda:** `server.port=8080` ile farklı port kullanın
**Telegram çalışmıyor:** Token ve Chat ID'yi kontrol edin
**Bağlantı hatası:** İnternet bağlantısını kontrol edin

## 📞 Yardım

Detaylı bilgi için `TCDD-Bot-Kullanim-Kilavuzu.md` dosyasını okuyun.

---
**İyi kullanımlar! 🚄**

