# Karavan Ateş — Android tablet

İnternetsiz çalışan yerel Android uygulaması. Sağ 1–20, Sol 1–20 ve Orta 1–10 olmak üzere 50 alan bulunur. 1–7 saat başlatma, mevcut bitişe 1–7 saat ekleme, kalan süre, bitiş saati ve onaylı alan boşaltma desteklenir. Süresi dolan alanda uzatma şu andan başlar. Kayıtlar cihazda tutulur. Uygulama kaldırılırsa silinir; cihazlar arasında eşitleme yoktur.

Bitime 5 dakika kala ve bitişte Android sesli bildirimi planlanır. Android 13+ bildirim iznini ve Android 12+ kesin alarm iznini açın. Alarm sesini açık tutun. Uygulama içindeki “Ses ve zamanlama ayarları” ile test edin. Sistem bildirimleri, Rahatsız Etmeyin ve üretici pil kısıtlamaları sesi etkileyebilir. Uygulamayı zorla durdurmak alarmları durdurur; tekrar açın. Yeniden başlatmada gelecek alarmlar yeniden planlanır. Uygulama açıkken ekran açık tutulur.

## Derleme

JDK 21, Android SDK platform 35 ve build-tools 35.0.0 gerekir.

```sh
ANDROID_HOME=/path/to/android-sdk scripts/build-apk.sh
```

Bu ortamda JDK derleyicisi yerine `ECJ_JAR=/workspace/android-tools/ecj.jar`, `ANDROID_HOME=/workspace/android-tools/sdk` ve `ANDROID_DEBUG_KEYSTORE=/workspace/android-tools/debug.keystore` ile aynı betik çalıştırılmıştır.

Çıktı: `build/karavan-ates.apk`. Geliştirme anahtarıyla imzalanır; mağaza yayını için uygun değildir. Aynı tablete güncelleme yaparken aynı anahtar korunmalıdır. Alternatif olarak Android Studio ile Gradle projesini açın.

## Cihaz doğrulaması

APK'yı tablete aktarın, dosya yöneticisinden açarak o kaynak için kurulum izni verin. Bildirim ve kesin alarm izinlerini açın. Bir alanı 1 saat başlatın, uygulamayı kapatıp açarak kalan sürenin korunduğunu kontrol edin; 1 saat uzatın ve bitişin bir saat ilerlediğini kontrol edin. Alan boşaltmada onayı ve sayaç özetini kontrol edin. Gerçek cihazda 5 dakika kala ve bitiş bildirimlerini, ekran kapalıyken de ayrıca doğrulayın. Emülatör/gerçek cihaz testi henüz yapılmamıştır.

## Bilgisayarda düzenleme

Kaynak klasörünü VS Code veya Visual Studio ile açabilirsiniz. Yerel Android Java projesidir; derleme ve Android emülatörü için Android Studio önerilir. Android Studio’da proje kökünü açın, SDK 35 ve JDK 21 ayarlayın, Gradle eşitlemesini tamamlayıp bir sanal cihaz seçerek Run düğmesini kullanın. Aktif/boş filtreleri Sağ/Sol/Orta seçimiyle birlikte çalışır; süresi dolan alan, personel boşaltana kadar aktif bölümünde kalır.

## 1.1 — Tablet kontrol paneli

Koyu renkli yuvarlatılmış kartlar, aktif/boş/son 5 dakika/süresi dolan özetleri, anlık saat ve tarih, seçili filtre vurgusu, yalnızca uyarılar filtresi ve bitiş zamanına göre sıralama eklendi. Alan sırasına geçmek için Sıralama düğmesini kullanın. Kartlar ekran genişliğine göre 1–4 sütuna yerleşir. Her kartta kalan süre çubuğu bulunur. Hızlı 1/2 saat başlatma ve +30 dakika uzatma işlemleri onay ister; normal süre seçimi 1–7 saat olarak korunur. Ekran döndüğünde seçili filtreler korunur.

APK: `downloads/karavan-ates.apk` (geliştirme imzası, Android 8+). APK derlemesi, imza doğrulaması, 9 süre hesabı kontrolü ve 50 alanın isim/sınır kontrolleri geçti. Fiziksel cihaz/emülatör üzerinde görsel ve sesli bildirim doğrulaması yapılmadı.

## 1.2 — Kahve ve köz tasarımı

Kahve/bakır renk paleti, cihazda çizilen geçişli arka plan ve köz detayları, markalı üst panel, gölgeli kartlar, durum rozetleri ve dokunma efektli düğmeler eklendi. Arka plan için internet veya resim dosyası gerekmez. APK derlendi, imzası doğrulandı. Gerçek cihazda görsel kontrol henüz yapılmadı.
