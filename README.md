# Pil Sağlığı (Android)

Lityum-iyon pil sağlığını korumaya yardımcı olan, eşik değerlerine göre uyarı veren basit bir Android uygulaması. Uygulama, anlık pil yüzdesini, şarj voltajını, akımı (cihaz destekliyorsa), sıcaklığı ve şarj durumunu gösterir. Belirlediğiniz düşük/yüksek eşiklere gelindiğinde alarm sesi çalar.

## Özellikler
- Anlık pil yüzdesi görüntüleme
- Şarj bilgileri: voltaj, akım, sıcaklık, durum
- Düşük ve yüksek eşik kaydırıcıları (kalıcı olarak saklanır)
- Eşik takibi için foreground servis ve bildirim kanalı
- Eşik ihlalinde sistem alarm sesi çalma

## Ekran Görüntüsü

## Mimarî Özet
- `MainActivity`: UI, pil yayınlarını dinleyerek bilgileri günceller; eşik kaydırıcılarını ve Başlat/Durdur düğmelerini yönetir.
- `BatteryMonitorService`: `ACTION_BATTERY_CHANGED` yayınını foreground olarak izler; pil yüzdesi eşik altına düştüğünde veya şarj olurken eşik üstüne çıktığında alarm sesini çalar.
- Eşikler `SharedPreferences` ile `battery_prefs` içinde saklanır (`low_threshold`, `high_threshold`).

## Kurulum
1. Depoyu klonlayın:
```bash
git clone <repo-url>
cd pil
```
2. Android Studio ile açın (Giraffe+ önerilir).
3. Gradle senkronizasyonunu tamamlayın.
4. Bir fiziksel cihazda veya emülatörde çalıştırın.

## Derleme Ortamı
- Kotlin, Android SDK 36 (compile/target)
- Min SDK 24
- Kütüphaneler: AndroidX, Material 3, ConstraintLayout

## İzinler
- `FOREGROUND_SERVICE`: Eşik takibi için servis ön planda çalışır.
- `POST_NOTIFICATIONS` (Android 13+): Foreground bildirimini göstermek için gerekir.

Android 13+ sürümlerinde ilk açılışta bildirim izni istenebilir. Reddedilirse servis çalışsa bile bildirim gösterilemeyebilir.

## Kullanım
1. Uygulamayı açın.
2. "Düşük Eşik" ve "Yüksek Eşik" değerlerini kaydırıcılarla belirleyin.
3. "Başlat" ile izlemeyi başlatın. Şartlar sağlandığında alarm sesi çalar.
4. "Durdur" ile izlemeyi durdurun.

> Mantık: Pil yüzdesi ≤ düşük eşik olduğunda veya şarj olurken ≥ yüksek eşik olduğunda alarm çalar.

## Teknik Notlar ve Sınırlamalar
- Akım değeri: `BatteryManager.BATTERY_PROPERTY_CURRENT_NOW` her cihazda/ROM’da mevcut olmayabilir; sağlanmayan cihazlarda "-" gösterilir.
- Voltaj yayınlarda mV cinsindedir; uygulama V cinsine çevirir.
- Arka planda uzun süreli izleme için üretici güç tasarrufu ayarları (MIUI, EMUI, vb.) servisi kısıtlayabilir. Gerekirse uygulamayı korumalı listeye ekleyin.

## Proje Yapısı
- `app/src/main/java/com/example/pilsagligi/MainActivity.kt`
- `app/src/main/java/com/example/pilsagligi/BatteryMonitorService.kt`
- `app/src/main/res/layout/activity_main.xml`
- `app/src/main/AndroidManifest.xml`

## Geliştirme Yol Haritası (Öneriler)
- Özel alarm tonu seçimi
- Koyu/açık tema iyileştirmeleri
- Bildirimde voltaj/akım bilgisini canlı gösterme
- Eşik ihlalinde titreşim ve sessiz modda uyarı seçenekleri

