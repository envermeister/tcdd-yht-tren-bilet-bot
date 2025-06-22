# TCDD Tren Bileti Takip Botu

TCDD YHT tren biletlerinde boş koltuk takibi yapan gelişmiş bot uygulaması. Hem web arayüzü hem de terminal (headless) modunda çalışabilir. Boş koltuk bulunduğunda Telegram üzerinden anlık bildirim gönderir.

![TCDD Bot](img.png)

## 🚀 Yeni Özellikler

### ✅ Headless Terminal Modu
- Komut satırından `--headless` parametresi ile çalıştırılabilir
- Interaktif menü sistemi
- Terminal üzerinden tam kontrol
- Sunucu ortamlarında çalıştırılabilir

### ✅ Telegram Bildirimleri
- Boş koltuk bulunduğunda anlık Telegram mesajı
- Hata durumlarında bildirim
- Kolay bot kurulumu
- Hem web hem terminal modunda aktif

### ✅ Gelişmiş Özellikler
- Çoklu çalışma modu (Web UI + Terminal)
- Gelişmiş hata yönetimi
- Detaylı loglama sistemi
- Kolay konfigürasyon

## 📋 Gereksinimler

- Java 17 veya üzeri
- İnternet bağlantısı
- Telegram Bot (isteğe bağlı)

## 🔧 Kurulum

### 1. JAR Dosyasını İndirin
En son sürümü [Releases](https://github.com/envermeister/tcdd-yht-tren-bilet-bot/releases) bölümünden indirin.

### 2. Telegram Bot Kurulumu (İsteğe Bağlı)

#### Bot Oluşturma
1. Telegram'da [@BotFather](https://t.me/BotFather) ile konuşun
2. `/newbot` komutunu gönderin
3. Bot adını ve kullanıcı adını belirleyin
4. Verilen bot token'ını kaydedin

#### Chat ID Öğrenme
1. [@userinfobot](https://t.me/userinfobot) ile konuşun
2. `/start` komutunu gönderin
3. Verilen Chat ID'yi kaydedin

#### Konfigürasyon
JAR dosyasının bulunduğu dizinde `application.properties` dosyası oluşturun:

```properties
# Telegram Bot Ayarları
telegram.bot.token=YOUR_BOT_TOKEN_HERE
telegram.chat.id=YOUR_CHAT_ID_HERE
```

## 🖥️ Kullanım

### Web Arayüzü Modu (Varsayılan)
```bash
java -jar Release-v1.jar
```
Tarayıcınızda [http://localhost:9090](http://localhost:9090) adresini açın.

### Terminal Modu (Headless)
```bash
java -jar Release-v1.jar --headless
```

#### Terminal Menüsü
```
=== TCDD Tren Bileti Takip Botu - Terminal Modu ===
Telegram bildirimleri aktif!

=== MENÜ ===
1. Takip başlat
2. Takip durdur
3. İstasyon listesi göster
4. Çıkış
Seçiminiz (1-4):
```

## 📱 Telegram Bildirimleri

Bot boş koltuk bulduğunda şu şekilde bildirim gönderir:

```
🚄 BOŞ KOLTUK BULUNDU!

🚉 Ankara → İstanbul
📅 25-12
⏰ 14:30 Av. Seats: 3

🔗 https://ebilet.tcddtasimacilik.gov.tr/sefer-listesi
```

## ⚙️ Konfigürasyon

### application.properties Dosyası
```properties
# Sunucu Ayarları
server.port=9090
spring.application.name=YHT Ticket Alarm

# Telegram Bot Ayarları (İsteğe Bağlı)
telegram.bot.token=YOUR_BOT_TOKEN
telegram.chat.id=YOUR_CHAT_ID

# TCDD API Token (Değiştirmeyin)
app.bearer-token=...
```

## 🔍 Özellikler

### Web Arayüzü
- Görsel istasyon seçimi
- Tarih ve saat aralığı belirleme
- Gerçek zamanlı log görüntüleme
- Alarm ayarları
- Özel istasyon ekleme

### Terminal Modu
- Interaktif menü sistemi
- Numara ile istasyon seçimi
- Gerçek zamanlı konsol çıktısı
- Kolay kontrol (başlat/durdur)

### Telegram Entegrasyonu
- Anlık bildirimler
- Hata raporlama
- Bağlantı testi
- Kolay kurulum

## 🚨 Önemli Notlar

- Bearer token uygulamada mevcuttur, değiştirmenize gerek yoktur
- Yüksek koltuk sayılarında küçük marjinal hatalar olabilir
- Düşük sayılarda bu sorun yaşanmaz
- 5 saniye aralıklarla kontrol yapar

## 🛠️ Geliştirme

### Kaynak Koddan Derleme
```bash
git clone https://github.com/envermeister/tcdd-yht-tren-bilet-bot.git
cd tcdd-yht-tren-bilet-bot
mvn clean package -DskipTests
```

### Gerekli Bağımlılıklar
- Spring Boot 3.2.3
- Vaadin 24.3.7
- RestAssured 5.4.0
- Java 17+

## 📄 Lisans

Bu proje olduğu gibi sağlanmaktadır, herhangi bir garanti verilmez. Kendi sorumluluğunuzda kullanın.

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request açın

---

**Not:** Bu araç kişisel ihtiyaçlar için geliştirilmiştir. Ticari bir proje değildir.

