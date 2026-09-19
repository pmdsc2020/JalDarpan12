# जलदर्पण — Android app (APK)

गावाचा पाण्याचा ताळेबंद. संपूर्ण ॲप एका HTML फाइलमध्ये आहे
(`app/src/main/assets/index.html`) आणि ते इंटरनेटशिवाय चालते.

हे साधन तयार केले: **जितेंद्र सोनवणे, कृषी अभियंता · ७७२००४५८०८**

---

## APK कसा तयार करायचा — तीन मार्ग

### मार्ग १ — GitHub वर (संगणकावर काहीही इन्स्टॉल न करता) ⭐ सर्वात सोपा

1. github.com वर नवीन repository तयार करा (Private चालेल).
2. या फोल्डरमधील सर्व फाइल्स तिथे अपलोड करा (Add file → Upload files → संपूर्ण फोल्डर ड्रॅग करा).
3. वरच्या **Actions** टॅबवर जा → `Build Jaldarpan APK` → **Run workflow**.
4. ५–८ मिनिटांनी त्याच पानावर **Artifacts → jaldarpan-debug-apk** दिसेल. तो डाउनलोड करा,
   झिप उघडा — आत `app-debug.apk` आहे. तो मोबाईलवर पाठवा आणि इन्स्टॉल करा.

### मार्ग २ — Android Studio मध्ये

1. Android Studio उघडा → **Open** → हा फोल्डर निवडा.
2. पहिल्यांदा Gradle sync होईल (इंटरनेट लागेल, १०–१५ मिनिटे).
3. **Build → Build Bundle(s)/APK(s) → Build APK(s)**.
4. APK इथे मिळेल: `app/build/outputs/apk/debug/app-debug.apk`.

### मार्ग ३ — कमांड लाइनवर (Android SDK असल्यास)

```bash
# local.properties मध्ये SDK चा मार्ग लिहा, उदा.
# sdk.dir=/home/user/Android/Sdk
gradle assembleDebug          # किंवा wrapper असल्यास ./gradlew assembleDebug
```

---

## मोबाईलवर इन्स्टॉल करताना

- Play Store बाहेरील ॲप असल्याने "अज्ञात स्रोत / Install unknown apps" ला एकदा परवानगी द्यावी लागते.
- Android 7.0 (2016) व त्यापुढील सर्व फोनवर चालते.

## प्रसिद्ध करण्यासाठी (Play Store किंवा स्वाक्षरी केलेला APK)

डीबग APK वाटण्यासाठी पुरेसा आहे. स्वाक्षरी केलेला (release) APK हवा असल्यास:

1. Android Studio → **Build → Generate Signed Bundle / APK → APK**.
2. नवीन keystore तयार करा आणि तो **सुरक्षित ठेवा** — हरवल्यास पुढील अपडेट देता येत नाही.
3. `app/build/outputs/apk/release/app-release.apk` तयार होईल.
4. Play Store साठी `playstore-icon-512.png` (५१२×५१२) सोबत जोडलेला आहे.

---

## ॲपमध्ये काय समाविष्ट आहे

| गोष्ट | तपशील |
|---|---|
| ऑफलाइन | संपूर्ण साधन ॲपमध्येच; इंटरनेट लागत नाही |
| माहिती जतन | WebView च्या localStorage मध्ये — ॲप बंद केले तरी राहते |
| छपाई | ॲपमधील "छापा / PDF" बटण Android च्या प्रिंट डायलॉगला जोडलेले आहे (A3 आडवा डिफॉल्ट); तिथून PDF म्हणून जतन किंवा Wi-Fi प्रिंटरवर छपाई |
| Back बटण | आधी ॲपमध्ये मागे, मग ॲप बंद |
| परवानग्या | फक्त INTERNET (फॉन्टसाठी). कॅमेरा, लोकेशन, संपर्क — काहीही नाही |

## साधन अपडेट करायचे असल्यास

नवीन HTML फाइल `app/src/main/assets/index.html` या नावाने बदला,
`app/build.gradle` मध्ये `versionCode` एकने वाढवा आणि पुन्हा बिल्ड करा.

## टीप — फॉन्ट

HTML मध्ये Google Fonts ची लिंक आहे. इंटरनेट नसल्यास फोनचा स्वतःचा देवनागरी
फॉन्ट वापरला जातो आणि ॲप व्यवस्थित चालते. पूर्णपणे ऑफलाइन फॉन्ट हवा असल्यास
`Mukta` व `Martel` च्या `.woff2` फाइल्स `assets/fonts/` मध्ये ठेवून HTML मधील
`<link>` ऐवजी `@font-face` वापरा.
