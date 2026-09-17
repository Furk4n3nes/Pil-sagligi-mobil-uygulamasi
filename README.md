<h1 align="center">🔋 Pil Sağlığı — Android</h1>

<p align="center">
  Lityum-iyon pil ömrünüzü korumak için tasarlanmış, eşik bazlı uyarı ve izleme uygulaması.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Target%20SDK-36-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"/>
</p>

---

## 📖 Hakkında

**Pil Sağlığı**, Android cihazınızdaki lityum-iyon pilin uzun ömürlü kalmasına yardımcı olmak için geliştirilmiş hafif ve kullanışlı bir izleme uygulamasıdır.

Piller %20'nin altında tamamen boşaldığında veya %80'in üzerinde uzun süre tam dolu kaldığında ömürleri hızla kısalır. Bu uygulama; belirlediğiniz **düşük** ve **yüksek şarj eşiklerine** ulaşıldığında sizi sesli alarm ile uyararak pilinizi ideal aralıkta tutmanıza yardımcı olur.

---

## ✨ Özellikler

| Özellik | Açıklama |
|--------|----------|
| 🔋 **Anlık Pil Yüzdesi** | Gerçek zamanlı olarak güncel şarj yüzdesini gösterir |
| ⚡ **Voltaj Bilgisi** | Anlık pil voltajını mV cinsinden görüntüler |
| 🌡️ **Sıcaklık İzleme** | Pil sıcaklığını °C cinsinden takip eder |
| 🔌 **Şarj Durumu** | Şarjda / Şarjda değil / Tam dolu bilgisini gösterir |
| 📊 **Akım Bilgisi** | Destekleyen cihazlarda anlık akımı (µA) görüntüler |
| 🎚️ **Ayarlanabilir Eşikler** | Kaydırıcılarla düşük ve yüksek eşik değerleri belirlenebilir |
| 💾 **Kalıcı Ayarlar** | Belirlenen eşikler uygulama kapatılsa da saklanır |
| 🔔 **Foreground Servis** | Uygulama arka planda çalışırken bile izleme sürer |
| 🔊 **Sesli Alarm** | Eşik ihlalinde sistem alarm sesi çalar |

---

## 📸 Ekran Görüntüleri

<p align="center">
  <img src="https://github.com/user-attachments/assets/2c19170d-2bcd-4368-a667-7439f8ea89ea" width="300" alt="Uygulama Ana Ekranı"/>
</p>

<p align="center"><i>Uygulama ana ekranı — pil bilgileri ve eşik kaydırıcıları</i></p>

---

## 🏗️ Mimari

Uygulama iki temel bileşenden oluşmaktadır:

```
app/
├── MainActivity.kt          # UI katmanı, pil yayınlarını dinler
└── BatteryMonitorService.kt # Foreground servis, eşik kontrolü yapar
```

### `MainActivity`
- `ACTION_BATTERY_CHANGED` broadcast'ini dinleyerek pil bilgilerini (yüzde, voltaj, akım, sıcaklık, durum) anlık günceller.
- Düşük ve yüksek eşik değerleri için **SeekBar** kaydırıcıları sunar.
- **Başlat / Durdur** butonlarıyla foreground servisi kontrol eder.

### `BatteryMonitorService`
- Android Foreground Service olarak çalışır; ekran kapalı ve uygulama arka planda olsa dahi izleme devam eder.
- Pil yüzdesi **düşük eşiğin altına** düştüğünde veya şarj olurken **yüksek eşiğin üstüne** çıktığında sistem alarm sesini çalar.

### `SharedPreferences`
- Eşik değerleri `battery_prefs` isimli dosyada kalıcı olarak saklanır.
  - `low_threshold` — Düşük pil eşiği (varsayılan: %20)
  - `high_threshold` — Yüksek şarj eşiği (varsayılan: %80)

---

## 🔧 Gereksinimler

| Gereksinim | Versiyon |
|-----------|---------|
| Android Studio | Giraffe veya üzeri |
| Kotlin | 1.9+ |
| Minimum SDK | API 24 (Android 7.0) |
| Hedef SDK | API 36 |
| Fiziksel cihaz | Akım bilgisi için önerilir (emülatörde sınırlı Projede kullanılan telefon: Xiaomi 11T Pro) |

---

## 🚀 Kurulum

### 1. Depoyu Klonlayın

```bash
git clone https://github.com/Furk4n3nes/Pil-sagligi-mobil-uygulamasi.git
cd Pil-sagligi-mobil-uygulamasi
```

### 2. Android Studio ile Açın

- Android Studio'yu başlatın.
- **File → Open** menüsünden klonladığınız klasörü seçin.

### 3. Gradle Senkronizasyonu

Android Studio Gradle dosyalarını otomatik olarak senkronize edecektir. Herhangi bir hata alırsanız:

```
Build → Clean Project
Build → Rebuild Project
```

### 4. Çalıştırın

- Bir **fiziksel Android cihazı** USB ile bağlayın (Geliştirici Modu açık olmalı) ya da bir **emülatör** başlatın.
- Toolbar'dan **Run ▶** butonuna tıklayın.

> ⚠️ **Not:** Akım (µA) bilgisi, tüm cihazlarda desteklenmeyebilir. Emülatörlerde bu değer her zaman gösterilmeyebilir.

---

## 📋 İzinler

Uygulama aşağıdaki Android izinlerini kullanır:

```xml
<!-- Pil durumu takibi (izin gerektirmez, broadcast ile alınır) -->
android.intent.action.BATTERY_CHANGED

<!-- Foreground servis için -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<!-- Alarm sesi için -->
<uses-permission android:name="android.permission.VIBRATE" />
```

---

## 💡 Kullanım Kılavuzu

1. Uygulamayı açın — pil bilgileri otomatik olarak yüklenecektir.
2. **Düşük Eşik** kaydırıcısını ayarlayın (örn. %20).
3. **Yüksek Eşik** kaydırıcısını ayarlayın (örn. %80).
4. **"Servisi Başlat"** butonuna basın.
5. Pil belirlenen eşiklere ulaştığında alarm çalar ve bildirim gelir.
6. İzlemeyi durdurmak için **"Servisi Durdur"** butonuna basın.

---

## 🤝 Katkıda Bulunma

Katkılarınızı memnuniyetle karşılıyorum!

1. Bu repoyu **fork** edin.
2. Yeni bir branch oluşturun: `git checkout -b feature/yeni-ozellik`
3. Değişikliklerinizi commit edin: `git commit -m 'feat: yeni özellik eklendi'`
4. Branch'inizi push edin: `git push origin feature/yeni-ozellik`
5. **Pull Request** açın.

---

