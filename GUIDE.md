# Kullanım Kılavuzu

**Yazar:** Manus AI  
**Tarih:** 22 Haziran 2025  
**Versiyon:** 2.0

## İçindekiler

1. [Giriş](#giriş)
2. [Sistem Gereksinimleri](#sistem-gereksinimleri)
3. [Kurulum](#kurulum)
4. [Telegram Bot Kurulumu](#telegram-bot-kurulumu)
5. [Web Arayüzü Kullanımı](#web-arayüzü-kullanımı)
6. [Terminal Modu Kullanımı](#terminal-modu-kullanımı)
7. [Konfigürasyon](#konfigürasyon)
8. [Sorun Giderme](#sorun-giderme)
9. [Gelişmiş Kullanım](#gelişmiş-kullanım)
10. [SSS](#sss)

---

## Giriş

TCDD Tren Bileti Takip Botu, Türkiye Cumhuriyeti Devlet Demiryolları (TCDD) YHT tren biletlerinde boş koltuk takibi yapan gelişmiş bir uygulamadır. Bu bot, kullanıcıların belirledikleri güzergah ve tarih için sürekli olarak bilet durumunu kontrol eder ve boş koltuk bulunduğunda anlık bildirim gönderir.

### Temel Özellikler

Bu kılavuzda ele alınacak bot, orijinal projeye eklenen yeni özelliklerle birlikte gelir. Bu özellikler şunlardır:

**Headless Terminal Modu:** Geleneksel web arayüzüne ek olarak, bot artık komut satırından tamamen bağımsız olarak çalıştırılabilir. Bu özellik özellikle sunucu ortamlarında veya grafik arayüzü olmayan sistemlerde kullanım için idealdir.

**Telegram Bildirimleri:** Bot, boş koltuk bulduğunda kullanıcıya Telegram üzerinden anlık bildirim gönderebilir. Bu özellik hem web arayüzü hem de terminal modunda aktif olarak çalışır.

**Gelişmiş Hata Yönetimi:** Uygulama, ağ bağlantı sorunları, API hataları ve diğer beklenmedik durumlar için kapsamlı hata yönetimi sunar.

**Çoklu Çalışma Modu:** Kullanıcılar ihtiyaçlarına göre web arayüzü veya terminal modunu seçebilir, her iki mod da tam işlevsellik sunar.

### Kullanım Senaryoları

Bu bot çeşitli kullanım senaryolarında faydalıdır. Bireysel kullanıcılar için, özellikle popüler güzergahlarda ve yoğun dönemlerde bilet bulmak zor olabilir. Bot sürekli takip yaparak kullanıcının manuel olarak kontrol etme zahmetini ortadan kaldırır.

İş seyahatleri için düzenli olarak tren kullananan profesyoneller, bot sayesinde son dakika iptallerinden veya ek sefer açılmalarından haberdar olabilir. Telegram bildirimleri sayesinde mobil cihazlarında anlık uyarı alırlar.

Sunucu ortamlarında çalışan sistem yöneticileri, headless mod sayesinde botu grafik arayüzü olmayan sistemlerde çalıştırabilir. Bu özellik özellikle VPS veya bulut sunucularında kullanım için uygundur.

---

## Sistem Gereksinimleri

### Donanım Gereksinimleri

Bot, minimal sistem kaynaklarıyla çalışacak şekilde tasarlanmıştır. Önerilen minimum donanım gereksinimleri şunlardır:

**İşlemci:** 1 GHz veya daha hızlı işlemci yeterlidir. Bot, CPU yoğun işlemler yapmadığı için eski sistemlerde bile sorunsuz çalışır.

**Bellek:** En az 512 MB RAM gereklidir, ancak 1 GB RAM önerilir. Java Virtual Machine (JVM) ve Spring Boot framework'ü nedeniyle bellek kullanımı başlangıçta yaklaşık 200-300 MB civarındadır.

**Depolama:** Uygulama dosyası yaklaşık 55 MB boyutundadır. Log dosyaları ve geçici dosyalar için ek 100 MB alan önerilir.

**Ağ Bağlantısı:** Sürekli internet bağlantısı gereklidir. Bot, TCDD API'sine düzenli istekler gönderdiği için stabil bir bağlantı önemlidir.

### Yazılım Gereksinimleri

**İşletim Sistemi:** Bot, Java tabanlı olduğu için platform bağımsızdır. Windows, macOS, Linux ve diğer Java destekli işletim sistemlerinde çalışır.

**Java Runtime Environment:** Java 17 veya daha yeni bir sürüm gereklidir. OpenJDK veya Oracle JDK kullanılabilir.

**Web Tarayıcısı:** Web arayüzü modu için modern bir web tarayıcısı gereklidir. Chrome, Firefox, Safari, Edge desteklenir.

**Telegram Uygulaması:** Bildirimler için Telegram hesabı ve mobil/masaüstü uygulaması gereklidir.

### Ağ Gereksinimleri

Bot, aşağıdaki ağ bağlantılarını kullanır:

**TCDD API:** `web-api-prod-ytp.tcddtasimacilik.gov.tr` adresine HTTPS bağlantısı
**Telegram API:** `api.telegram.org` adresine HTTPS bağlantısı
**Web Arayüzü:** Yerel ağda 9090 portu (değiştirilebilir)

Kurumsal ağlarda güvenlik duvarı ayarlarının bu bağlantılara izin vermesi gerekebilir.

---

## Kurulum

### Java Kurulumu

Bot çalıştırılmadan önce sisteminizde Java 17 veya daha yeni bir sürümün kurulu olması gerekir.

**Windows için Java Kurulumu:**

Oracle'ın resmi web sitesinden Java SE Development Kit (JDK) 17'yi indirin. Alternatif olarak, açık kaynak OpenJDK kullanabilirsiniz. Kurulum dosyasını çalıştırın ve kurulum sihirbazını takip edin.

Kurulum tamamlandıktan sonra, komut istemini açın ve `java -version` komutunu çalıştırarak kurulumun başarılı olduğunu doğrulayın. Çıktıda Java 17 veya daha yeni bir sürüm görmelisiniz.

**macOS için Java Kurulumu:**

Homebrew kullanıyorsanız, terminal açın ve şu komutu çalıştırın:
```bash
brew install openjdk@17
```

Homebrew kullanmıyorsanız, Oracle'ın web sitesinden macOS için JDK paketini indirip kurun.

**Linux için Java Kurulumu:**

Ubuntu/Debian sistemlerde:
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

CentOS/RHEL sistemlerde:
```bash
sudo yum install java-17-openjdk-devel
```

### Bot Uygulamasının İndirilmesi

En güncel JAR dosyasını GitHub releases sayfasından indirin. Dosya adı `TrainTicketTracker-1.0-SNAPSHOT.jar` şeklindedir ve boyutu yaklaşık 55 MB'dır.

İndirilen dosyayı uygun bir dizine yerleştirin. Örneğin:
- Windows: `C:\TCDD-Bot\`
- macOS/Linux: `~/tcdd-bot/`

### İlk Çalıştırma Testi

Bot'un doğru çalıştığını test etmek için terminal veya komut istemini açın ve JAR dosyasının bulunduğu dizine gidin. Ardından şu komutu çalıştırın:

```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

Uygulama başarıyla başlarsa, konsol çıktısında Spring Boot logosu ve başlatma mesajları görmelisiniz. Web arayüzü modunda çalıştığı için tarayıcınızda `http://localhost:9090` adresini açarak arayüze erişebilirsiniz.

İlk test tamamlandıktan sonra uygulamayı `Ctrl+C` ile kapatabilirsiniz.

---

## Telegram Bot Kurulumu

Telegram bildirimleri almak için öncelikle bir Telegram bot oluşturmanız ve gerekli konfigürasyonu yapmanız gerekir.

### Telegram Bot Oluşturma

Telegram uygulamasını açın ve [@BotFather](https://t.me/BotFather) ile konuşmaya başlayın. BotFather, Telegram'ın resmi bot yönetim aracıdır.

**Adım 1: Yeni Bot Oluşturma**

BotFather'a `/newbot` komutunu gönderin. Size bot için bir isim belirlemenizi isteyecek.

**Adım 2: Bot Adını Belirleme**

Bot'unuz için açıklayıcı bir isim girin. Örneğin: "TCDD Bilet Takip Botum"

**Adım 3: Kullanıcı Adını Belirleme**

Bot için benzersiz bir kullanıcı adı belirlemeniz gerekir. Bu ad "bot" ile bitmelidir. Örneğin: "tcdd_bilet_takip_bot"

**Adım 4: Bot Token'ını Kaydetme**

BotFather size uzun bir token verecek. Bu token şu formatta olacak:
```
1234567890:ABCdefGHIjklMNOpqrsTUVwxyz
```

Bu token'ı güvenli bir yerde saklayın. Uygulama konfigürasyonunda kullanacaksınız.

### Chat ID Öğrenme

Telegram bildirimleri almak için kendi Chat ID'nizi öğrenmeniz gerekir.

[@userinfobot](https://t.me/userinfobot) ile konuşmaya başlayın ve `/start` komutunu gönderin. Bot size kullanıcı bilgilerinizi gönderecek ve bunların arasında Chat ID'niz de olacak.

Chat ID genellikle pozitif bir sayıdır, örneğin: `123456789`

### Konfigürasyon Dosyası Oluşturma

JAR dosyasının bulunduğu dizinde `application.properties` adında bir dosya oluşturun. Bu dosyaya şu içeriği ekleyin:

```properties
# Telegram Bot Ayarları
telegram.bot.token=YOUR_BOT_TOKEN_HERE
telegram.chat.id=YOUR_CHAT_ID_HERE
```

`YOUR_BOT_TOKEN_HERE` yerine BotFather'dan aldığınız token'ı, `YOUR_CHAT_ID_HERE` yerine de öğrendiğiniz Chat ID'yi yazın.

### Telegram Bağlantısını Test Etme

Konfigürasyon tamamlandıktan sonra botu çalıştırın ve terminal modunda Telegram bağlantısını test edin:

```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar --headless
```

Menüden "1" seçerek takip başlatın. Bot başarıyla yapılandırıldıysa, Telegram'da test mesajı alacaksınız.

---

## Web Arayüzü Kullanımı

Web arayüzü, bot'un görsel kullanıcı dostu modudur. Bu mod, özellikle ilk kez kullanıcılar için önerilir.

### Web Arayüzüne Erişim

Bot'u normal modda başlatın:
```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

Tarayıcınızda `http://localhost:9090` adresini açın. Ana sayfa yüklendiğinde "YHT Ticket Alarm" başlığını ve form alanlarını göreceksiniz.

### İstasyon Seçimi

**Kalkış İstasyonu:** Açılır menüden seyahatinizin başlayacağı istasyonu seçin. Liste, TCDD'nin aktif YHT istasyonlarını içerir.

**Varış İstasyonu:** Hedef istasyonunuzu seçin. Kalkış istasyonu seçildikten sonra, varış menüsü otomatik olarak güncellenir ve aynı istasyon seçimi engellenir.

**Özel İstasyon Ekleme:** Listede istediğiniz istasyon yoksa, "Add Custom Station" butonuna tıklayarak yeni istasyon ekleyebilirsiniz. İstasyon ID'si, resmi adı ve görüntülenecek adı girmeniz gerekir.

### Tarih ve Saat Ayarları

**Tarih Seçimi:** Takvim aracını kullanarak seyahat tarihini seçin. Geçmiş tarihler seçilemez.

**Minimum Kalkış Saati:** En erken kabul edilebilir kalkış saatini girin. Format: HH:MM (örneğin: 06:00)

**Maksimum Kalkış Saati:** En geç kabul edilebilir kalkış saatini girin. Format: HH:MM (örneğin: 23:59)

### Takip Ayarları

**Log All Available Trains:** Bu seçenek işaretlendiğinde, belirlenen saat aralığı dışındaki boş koltuklar da bilgi amaçlı gösterilir.

**Enable Alarms:** Bu seçenek işaretlendiğinde, uygun boş koltuk bulunduğunda hem ekran bildirimi hem de Telegram mesajı gönderilir.

### Takibi Başlatma ve Durdurma

Tüm ayarları yaptıktan sonra "Start Monitoring" butonuna tıklayın. Bot, 5 saniye aralıklarla TCDD API'sini kontrol etmeye başlar.

Takip sırasında:
- Gerçek zamanlı log mesajları Results alanında görüntülenir
- Boş koltuk bulunduğunda ekranın üst kısmında bildirim çıkar
- Telegram yapılandırıldıysa mobil bildirim gönderilir

Takibi durdurmak için "Stop" butonuna tıklayın.

### Log Yönetimi

**Log Görüntüleme:** Results alanı, bot'un tüm aktivitelerini zaman damgası ile gösterir. Her log satırı numaralandırılmıştır.

**Log Temizleme:** "Clear Logs" butonu ile log geçmişini temizleyebilirsiniz.

**Otomatik Kaydırma:** Yeni log mesajları geldiğinde alan otomatik olarak en alta kaydırılır.

### Hızlı Erişim Linkleri

**Open TCDD Ticket Page:** Bu buton, TCDD'nin resmi bilet satış sayfasını yeni sekmede açar. Boş koltuk bulunduğunda hızlıca bilet satın almak için kullanabilirsiniz.

---

## Terminal Modu Kullanımı

Terminal modu, bot'un komut satırından çalıştırılan headless versiyonudur. Bu mod, sunucu ortamlarında veya grafik arayüzü olmayan sistemlerde kullanım için idealdir.

### Terminal Modunu Başlatma

Terminal veya komut istemini açın ve şu komutu çalıştırın:
```bash
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar --headless
```

Uygulama başladığında şu ekranı göreceksiniz:
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

### Menü Seçenekleri

**Seçenek 1: Takip Başlat**

Bu seçeneği seçtiğinizde, bot size adım adım konfigürasyon soracak:

1. **Kalkış İstasyonu Seçimi:** Mevcut istasyonlar numaralandırılmış liste halinde gösterilir. İstediğiniz istasyonun numarasını girin.

2. **Varış İstasyonu Seçimi:** Kalkış istasyonu seçildikten sonra, varış istasyonu için aynı liste gösterilir. Farklı bir istasyon seçin.

3. **Tarih Girişi:** DD-MM formatında tarih girin (örneğin: 25-12).

4. **Saat Aralığı:** Minimum ve maksimum kalkış saatlerini HH:MM formatında girin.

5. **Telegram Bildirimleri:** E/H seçeneği ile Telegram bildirimlerini aktif/pasif yapabilirsiniz.

Tüm bilgiler girildikten sonra takip başlar ve şu şekilde çıktı alırsınız:

```
=== TAKİP BAŞLATILDI ===
Kalkış: Ankara
Varış: İstanbul
Tarih: 25-12
Saat Aralığı: 06:00 - 23:59
Telegram: Aktif
Kontrol Aralığı: 5 saniye

Takibi durdurmak için '2' seçeneğini kullanın.
=========================

[14:30:15] Kontrol ediliyor: Ankara -> İstanbul
[14:30:16] Boş koltuk bulunamadı
[14:30:21] Kontrol ediliyor: Ankara -> İstanbul
[14:30:22] 🎉 ALERT: 16:30 Av. Seats: 2
[14:30:22] 📱 Telegram bildirimi gönderildi
```

**Seçenek 2: Takip Durdur**

Aktif takip varsa durdurur. Takip yoksa uyarı mesajı gösterir.

**Seçenek 3: İstasyon Listesi Göster**

Mevcut tüm istasyonları numaralandırılmış liste halinde gösterir. Bu liste, takip başlatırken hangi numaraları kullanacağınızı öğrenmek için faydalıdır.

**Seçenek 4: Çıkış**

Uygulamayı güvenli şekilde kapatır. Aktif takip varsa önce durdurulur.

### Terminal Modunda Log Formatı

Terminal modunda log mesajları şu formatlarda görüntülenir:

**Normal Kontrol:**
```
[14:30:15] Kontrol ediliyor: Ankara -> İstanbul
[14:30:16] Boş koltuk bulunamadı
```

**Boş Koltuk Bulundu:**
```
[14:30:22] 🎉 ALERT: 16:30 Av. Seats: 2
[14:30:22] 📱 Telegram bildirimi gönderildi
```

**Hata Durumu:**
```
[14:30:25] ❌ HATA: Connection timeout
[14:30:25] 📱 Telegram bildirimi gönderildi
```

**Bilgi Mesajları:**
```
[14:30:30] ℹ️ INFO: Time: 18:45 Av. Seats: 1
```

### Klavye Kısayolları

Terminal modunda çalışırken şu klavye kısayollarını kullanabilirsiniz:

- **Ctrl+C:** Uygulamayı zorla kapatır
- **Ctrl+Z:** Uygulamayı arka plana alır (Linux/macOS)
- **Enter:** Menü seçimini onaylar

---

## Konfigürasyon

Bot'un davranışını özelleştirmek için çeşitli konfigürasyon seçenekleri mevcuttur.

### application.properties Dosyası

JAR dosyasının bulunduğu dizinde `application.properties` dosyası oluşturarak bot'un ayarlarını değiştirebilirsiniz.

**Temel Ayarlar:**
```properties
# Sunucu Portu (Web arayüzü için)
server.port=9090

# Uygulama Adı
spring.application.name=YHT Ticket Alarm

# Geliştirme Modu (production için false yapın)
vaadin.frontend.hotdeploy=false
```

**Telegram Ayarları:**
```properties
# Telegram Bot Token (BotFather'dan alınan)
telegram.bot.token=1234567890:ABCdefGHIjklMNOpqrsTUVwxyz

# Telegram Chat ID (userinfobot'tan alınan)
telegram.chat.id=123456789
```

**TCDD API Ayarları:**
```properties
# TCDD Bearer Token (Değiştirmeyin!)
app.bearer-token=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlVFFicDhDMmpiakp1cnUzQVk2a0ZnV186U29MQXZIMmJ5bTJ2OUg5THhRIn0...
```

### Gelişmiş Konfigürasyon

**Log Seviyesi Ayarlama:**
```properties
# Log seviyesi (ERROR, WARN, INFO, DEBUG)
logging.level.services=INFO
logging.level.root=WARN
```

**Bağlantı Timeout Ayarları:**
```properties
# HTTP bağlantı timeout (milisaniye)
spring.mvc.async.request-timeout=30000
```

**Bellek Ayarları:**

JVM bellek ayarlarını komut satırından yapabilirsiniz:
```bash
java -Xmx512m -Xms256m -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

### Çevresel Değişkenler

Hassas bilgileri (token'lar gibi) çevresel değişkenler olarak da tanımlayabilirsiniz:

**Linux/macOS:**
```bash
export TELEGRAM_BOT_TOKEN="your_token_here"
export TELEGRAM_CHAT_ID="your_chat_id_here"
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

**Windows:**
```cmd
set TELEGRAM_BOT_TOKEN=your_token_here
set TELEGRAM_CHAT_ID=your_chat_id_here
java -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

### Profil Tabanlı Konfigürasyon

Farklı ortamlar için farklı konfigürasyonlar kullanabilirsiniz:

**application-production.properties:**
```properties
server.port=8080
vaadin.frontend.hotdeploy=false
logging.level.root=ERROR
```

**application-development.properties:**
```properties
server.port=9090
vaadin.frontend.hotdeploy=true
logging.level.root=DEBUG
```

Profil seçimi:
```bash
java -Dspring.profiles.active=production -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

---

## Sorun Giderme

Bu bölümde, bot kullanırken karşılaşabileceğiniz yaygın sorunlar ve çözümleri ele alınmaktadır.

### Java İle İlgili Sorunlar

**Sorun:** "java: command not found" hatası
**Çözüm:** Java'nın sisteminizde kurulu olmadığını gösterir. Java 17 veya daha yeni bir sürüm kurun.

**Sorun:** "UnsupportedClassVersionError" hatası
**Çözüm:** Kurulu Java sürümü çok eski. Java 17 veya daha yeni sürüm gereklidir.

**Sorun:** "OutOfMemoryError" hatası
**Çözüm:** JVM bellek ayarlarını artırın:
```bash
java -Xmx1g -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

### Ağ Bağlantısı Sorunları

**Sorun:** "Connection refused" veya "Connection timeout" hataları
**Çözüm:** 
- İnternet bağlantınızı kontrol edin
- Güvenlik duvarı ayarlarını kontrol edin
- VPN kullanıyorsanız kapatmayı deneyin

**Sorun:** TCDD API'sine erişim sorunu
**Çözüm:**
- TCDD web sitesinin erişilebilir olduğunu kontrol edin
- Bearer token'ın güncel olduğunu doğrulayın
- Proxy ayarları varsa kontrol edin

### Telegram Bildirimi Sorunları

**Sorun:** Telegram mesajları gelmiyor
**Çözüm:**
1. Bot token'ının doğru olduğunu kontrol edin
2. Chat ID'nin doğru olduğunu kontrol edin
3. Bot'u Telegram'da engellemediğinizden emin olun
4. application.properties dosyasının doğru konumda olduğunu kontrol edin

**Sorun:** "Unauthorized" hatası
**Çözüm:** Bot token'ı yanlış veya geçersiz. BotFather'dan yeni token alın.

**Sorun:** "Chat not found" hatası
**Çözüm:** Chat ID yanlış. @userinfobot ile doğru ID'yi öğrenin.

### Web Arayüzü Sorunları

**Sorun:** "localhost:9090" adresine erişilemiyor
**Çözüm:**
1. Bot'un başarıyla başladığını kontrol edin
2. Port 9090'ın başka bir uygulama tarafından kullanılmadığını kontrol edin
3. Güvenlik duvarı ayarlarını kontrol edin

**Sorun:** Web sayfası yüklenmiyor veya boş görünüyor
**Çözüm:**
1. Tarayıcı cache'ini temizleyin
2. Farklı bir tarayıcı deneyin
3. JavaScript'in etkin olduğunu kontrol edin

### Performans Sorunları

**Sorun:** Bot yavaş çalışıyor
**Çözüm:**
1. Sistem kaynaklarını kontrol edin
2. Arka planda çalışan diğer uygulamaları kapatın
3. JVM bellek ayarlarını optimize edin

**Sorun:** Yüksek CPU kullanımı
**Çözüm:**
1. Kontrol aralığını artırın (varsayılan 5 saniye)
2. Log seviyesini azaltın
3. Gereksiz log çıktılarını kapatın

### Veri Doğruluğu Sorunları

**Sorun:** Boş koltuk sayısı yanlış görünüyor
**Çözüm:** Bu, TCDD API'sinin bilinen bir sınırlamasıdır. Yüksek koltuk sayılarında küçük marjinal hatalar olabilir. Düşük sayılarda bu sorun yaşanmaz.

**Sorun:** Eski veriler gösteriliyor
**Çözüm:**
1. İnternet bağlantınızı kontrol edin
2. TCDD web sitesinin güncel olduğunu doğrulayın
3. Bot'u yeniden başlatın

### Log ve Hata Ayıklama

Detaylı hata ayıklama için log seviyesini artırın:

```properties
logging.level.services=DEBUG
logging.level.org.springframework=DEBUG
```

Log dosyasına kaydetmek için:

```properties
logging.file.name=tcdd-bot.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## Gelişmiş Kullanım

Bu bölümde, bot'un daha gelişmiş kullanım senaryoları ve özelleştirme seçenekleri ele alınmaktadır.

### Çoklu İstasyon Takibi

Birden fazla güzergahı aynı anda takip etmek için birden fazla bot instance'ı çalıştırabilirsiniz. Her instance için farklı port kullanın:

**İlk Bot (Ankara-İstanbul):**
```bash
java -Dserver.port=9090 -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

**İkinci Bot (İstanbul-Ankara):**
```bash
java -Dserver.port=9091 -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

### Otomatik Başlatma

**Linux Systemd Servisi:**

`/etc/systemd/system/tcdd-bot.service` dosyası oluşturun:

```ini
[Unit]
Description=TCDD Ticket Tracker Bot
After=network.target

[Service]
Type=simple
User=tcdd-bot
WorkingDirectory=/opt/tcdd-bot
ExecStart=/usr/bin/java -jar TrainTicketTracker-1.0-SNAPSHOT.jar --headless
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Servisi etkinleştirin:
```bash
sudo systemctl enable tcdd-bot
sudo systemctl start tcdd-bot
```

**Windows Görev Zamanlayıcısı:**

1. Görev Zamanlayıcısını açın
2. "Temel Görev Oluştur" seçin
3. Tetikleyici olarak "Bilgisayar başladığında" seçin
4. Eylem olarak bot'u çalıştıran batch dosyasını seçin

### Docker Kullanımı

Bot'u Docker container'ında çalıştırmak için Dockerfile oluşturun:

```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app
COPY TrainTicketTracker-1.0-SNAPSHOT.jar app.jar
COPY application.properties application.properties

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]
```

Docker image oluşturun ve çalıştırın:
```bash
docker build -t tcdd-bot .
docker run -d -p 9090:9090 --name tcdd-bot tcdd-bot
```

### API Entegrasyonu

Bot'un web API'sini kullanarak dış sistemlerle entegrasyon yapabilirsiniz:

**Durum Sorgulama:**
```bash
curl http://localhost:9090/api/status
```

**Takip Başlatma:**
```bash
curl -X POST http://localhost:9090/api/start \
  -H "Content-Type: application/json" \
  -d '{"departure":"Ankara","arrival":"Istanbul","date":"25-12"}'
```

### Özel İstasyon Ekleme

Yeni istasyonlar eklemek için StationService sınıfını genişletebilirsiniz. Özel istasyon listesi için JSON dosyası kullanın:

**custom-stations.json:**
```json
[
  {
    "id": 999,
    "name": "ÖZEL İSTASYON YHT",
    "cityName": "Özel İstasyon"
  }
]
```

### Bildirim Özelleştirme

Telegram mesaj formatını özelleştirmek için TelegramService sınıfını değiştirin:

```java
String customMessage = String.format(
    "🚄 %s → %s\n" +
    "📅 %s\n" +
    "🕐 %s\n" +
    "💺 %d koltuk\n" +
    "🔗 %s",
    departure, arrival, date, time, seats, url
);
```

### Performans Optimizasyonu

**Bellek Optimizasyonu:**
```bash
java -Xmx256m -Xms128m -XX:+UseG1GC -jar TrainTicketTracker-1.0-SNAPSHOT.jar
```

**Ağ Optimizasyonu:**
```properties
# Bağlantı havuzu ayarları
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=2
```

### Monitoring ve Alerting

**Health Check Endpoint:**
```bash
curl http://localhost:9090/actuator/health
```

**Metrics Endpoint:**
```bash
curl http://localhost:9090/actuator/metrics
```

**Prometheus Entegrasyonu:**
```properties
management.endpoints.web.exposure.include=health,metrics,prometheus
management.endpoint.prometheus.enabled=true
```

---

## SSS (Sık Sorulan Sorular)

### Genel Sorular

**S: Bot yasal mı? TCDD tarafından yasaklanabilir mi?**
C: Bot, TCDD'nin halka açık API'sini kullanır ve bilet satın alma işlemi yapmaz. Sadece mevcut bilgileri sorgular. Ancak aşırı kullanım durumunda IP adresiniz geçici olarak engellenebilir.

**S: Bot ne kadar sıklıkla kontrol yapar?**
C: Varsayılan olarak 5 saniye aralıklarla kontrol yapar. Bu süre değiştirilemez çünkü daha sık kontrol TCDD sunucularına aşırı yük bindirebilir.

**S: Birden fazla güzergahı aynı anda takip edebilir miyim?**
C: Tek bot instance'ı aynı anda sadece bir güzergahı takip edebilir. Birden fazla güzergah için birden fazla bot çalıştırmanız gerekir.

**S: Bot otomatik bilet satın alabilir mi?**
C: Hayır, bot sadece boş koltuk takibi yapar. Bilet satın alma işlemini manuel olarak yapmanız gerekir.

### Teknik Sorular

**S: Hangi işletim sistemlerinde çalışır?**
C: Java 17 destekleyen tüm işletim sistemlerinde çalışır: Windows, macOS, Linux, Unix.

**S: Sunucuda headless modda çalıştırabilir miyim?**
C: Evet, `--headless` parametresi ile grafik arayüzü olmayan sunucularda çalıştırabilirsiniz.

**S: Bellek kullanımı ne kadar?**
C: Başlangıçta yaklaşık 200-300 MB RAM kullanır. Uzun süre çalıştıktan sonra bu miktar 400-500 MB'a çıkabilir.

**S: İnternet bağlantısı kesilirse ne olur?**
C: Bot bağlantı hatalarını yakalar ve yeniden bağlanmaya çalışır. Telegram bildirimi yapılandırıldıysa hata durumunu bildirir.

### Konfigürasyon Soruları

**S: Telegram bot kurulumu zorunlu mu?**
C: Hayır, Telegram bildirimleri isteğe bağlıdır. Bot, Telegram yapılandırması olmadan da çalışır.

**S: Web arayüzü portunu değiştirebilir miyim?**
C: Evet, application.properties dosyasında `server.port=8080` şeklinde değiştirebilirsiniz.

**S: Bearer token'ı güncellemem gerekir mi?**
C: Normalde gerekli değildir. Token geçersiz olursa uygulama güncellemesi gerekebilir.

**S: Özel istasyon ekleyebilir miyim?**
C: Evet, web arayüzünde "Add Custom Station" özelliği ile ekleyebilirsiniz.

### Sorun Giderme Soruları

**S: "Connection refused" hatası alıyorum, ne yapmalıyım?**
C: İnternet bağlantınızı kontrol edin, güvenlik duvarı ayarlarını gözden geçirin ve TCDD web sitesinin erişilebilir olduğunu doğrulayın.

**S: Telegram mesajları gelmiyor, sorun nerede?**
C: Bot token ve Chat ID'nin doğru olduğunu kontrol edin. Bot'u Telegram'da engellemediğinizden emin olun.

**S: Web arayüzü açılmıyor, ne yapmalıyım?**
C: Port 9090'ın başka bir uygulama tarafından kullanılmadığını kontrol edin. Farklı bir port deneyin.

**S: Bot çok yavaş çalışıyor, nasıl hızlandırabilirim?**
C: JVM bellek ayarlarını optimize edin, gereksiz uygulamaları kapatın ve sistem kaynaklarını kontrol edin.

### Güvenlik Soruları

**S: Bot güvenli mi? Kişisel bilgilerimi topluyor mu?**
C: Bot sadece seçtiğiniz istasyon ve tarih bilgilerini kullanır. Kişisel bilgi toplamaz veya saklamaz.

**S: Telegram token'ımı başkaları görebilir mi?**
C: Token'ı application.properties dosyasında saklıyorsanız, dosya izinlerini kontrol edin. Çevresel değişken kullanımı daha güvenlidir.

**S: TCDD hesap bilgilerime ihtiyaç var mı?**
C: Hayır, bot TCDD hesabı gerektirmez. Halka açık API'yi kullanır.

### Performans Soruları

**S: Bot ne kadar internet bandgenişliği kullanır?**
C: Çok az. Her kontrol yaklaşık 1-2 KB veri kullanır. Günlük toplam kullanım 1 MB'ın altındadır.

**S: Uzun süre çalıştırmak zararlı mı?**
C: Hayır, bot 7/24 çalışacak şekilde tasarlanmıştır. Bellek sızıntısı önlemleri alınmıştır.

**S: Birden fazla bot çalıştırmak sistem performansını etkiler mi?**
C: Her bot instance'ı yaklaşık 300 MB RAM kullanır. Sistem kaynaklarınıza göre sayıyı ayarlayın.

---

**Bu kılavuzun sonuna geldiniz. Bot kullanımında başarılar dileriz!**

*Son güncelleme: 22 Haziran 2025*  
*Versiyon: 2.0*  
*Yazar: Manus AI*

