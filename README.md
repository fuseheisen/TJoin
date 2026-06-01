# TJoin - Advanced Folia Dungeon System

![Version](https://img.shields.io/badge/Version-1.0.0-blue.svg)
![API](https://img.shields.io/badge/Paper_API-1.21+-green.svg)
![Folia](https://img.shields.io/badge/Folia-Supported-success.svg)

**TJoin** is a highly optimized, Folia-compatible dungeon access and management system. Developed specifically for the TurkeyNW network, this plugin is engineered to operate efficiently on high-performance infrastructure. By utilizing asynchronous operations, it prevents main-thread latency and ensures maximum stability across advanced hardware configurations.

The system provides a comprehensive in-game Graphical User Interface (GUI), allowing server administrators to manage all dungeon properties without requiring manual configuration file modifications.

## Features
* **Folia & Paper 1.21+ Compatibility:** Implements asynchronous teleportation, database querying, and region-based scheduling to maintain zero performance degradation.
* **In-Game GUI Management:** Enables the creation, modification, and deletion of dungeons through an intuitive in-game menu system.
* **Dynamic Access Requirements:** Dungeon entry parameters can be configured as unconditional (free), economy-based (Vault integration), or item-based (custom keys).
* **Custom Key Integration:** Provides native support for CustomModelData (CMD), facilitating seamless integration with resource management plugins such as ItemsAdder and Oraxen.
* **Standardized Text Formatting:** Automatically intercepts and removes default client italics, ensuring a professional and uniform typography across all menus and messages.
* **Multi-Database Architecture:** Includes built-in support for multiple storage solutions, including YAML, MySQL, MariaDB, and MongoDB.

## Installation and Configuration

### Deployment
1. Transfer the `TJoin-1.0.0.jar` file to the `plugins` directory of your server.
2. If economy-based requirements will be utilized, ensure that **Vault** and a compatible economy provider are installed.
3. Restart the server to initialize the plugin.

### Dungeon Creation Process
1. Execute the `/tjoin admin` command to access the Dungeon Management interface.
2. Select the **New Dungeon** button.
3. Input a unique identification string for the new dungeon into the chat.
4. Upon successful creation, the dungeon will appear in the management interface for further configuration.

### Dungeon Configuration
Selecting an existing dungeon from the `/tjoin admin` menu grants access to the following configuration parameters:
* **Display Name Modification:** Prompts the administrator to input a new formatted display name via chat.
* **Requirement Modification:** Toggles the entry prerequisite between Free, Money, and Key options.
* **Cooldown Configuration:** Prompts the administrator to define the entry restriction period in seconds.
* **Location Assignment:** Issues a configuration item (Location Pearl) to the administrator. Right-clicking this item records the administrator's exact spatial coordinates, including yaw and pitch, as the dungeon's entry point.
* **Deletion:** Permanently removes the dungeon profile and associated data from the system.

## Commands and Permissions

| Command | Description | Permission Node |
| :--- | :--- | :--- |
| `/tjoin` | Accesses the primary dungeon selection interface. | *None* |
| `/tjoin admin` | Accesses the administrative management interface. | `tjoin.admin` |
| `/tjoin getkey <id>` | Generates and provisions the access key for a specified dungeon. | `tjoin.admin` |
| `/tjoin reload` | Reinitializes all configuration files and cache data. | `tjoin.admin` |

**Bypass Privilege:** Accounts possessing the `tjoin.bypass.cooldown` permission node are exempt from configured entry restriction periods.

---

<br>

# TJoin - Gelişmiş Folia Zindan Sistemi

**TJoin**, yüksek düzeyde optimize edilmiş ve Folia altyapısıyla tam uyumlu bir zindan erişim ve yönetim sistemidir. TurkeyNW ağı için özel olarak geliştirilen bu eklenti, yüksek performanslı altyapılarda verimli çalışmak üzere tasarlanmıştır. Asenkron işlem yetenekleri sayesinde ana iş parçacığında (main-thread) gecikme yaratmaz ve gelişmiş donanım yapılandırmalarında maksimum stabilite sağlar.

Sistem, sunucu yöneticilerinin manuel dosya düzenlemelerine gerek duymadan tüm zindan özelliklerini yönetmelerine olanak tanıyan kapsamlı bir oyun içi Grafiksel Kullanıcı Arayüzü (GUI) sunar.

## Özellikler
* **Folia ve Paper 1.21+ Uyumluluğu:** Performans kaybını önlemek amacıyla asenkron ışınlanma, veritabanı sorgulama ve bölge tabanlı (RegionScheduler) görevlendirme sistemlerini uygular.
* **Oyun İçi Arayüz Yönetimi:** Zindanların oluşturulması, düzenlenmesi ve silinmesi işlemlerinin tamamen oyun içi menü sistemi üzerinden gerçekleştirilmesini sağlar.
* **Dinamik Giriş Gereksinimleri:** Zindan giriş parametreleri koşulsuz (ücretsiz), ekonomi tabanlı (Vault entegrasyonu) veya eşya tabanlı (özel anahtarlar) olarak yapılandırılabilir.
* **Özel Anahtar Entegrasyonu:** CustomModelData (CMD) için yerel destek sağlayarak ItemsAdder ve Oraxen gibi kaynak yönetim eklentileriyle sorunsuz entegrasyonu kolaylaştırır.
* **Standartlaştırılmış Metin Biçimlendirme:** İstemcinin varsayılan eğik yazı (italik) formatını otomatik olarak kaldırarak tüm menü ve mesajlarda profesyonel ve tek tip bir tipografi sağlar.
* **Çoklu Veritabanı Mimarisi:** YAML, MySQL, MariaDB ve MongoDB dâhil olmak üzere çeşitli depolama çözümleri için dâhili destek içerir.

## Kurulum ve Yapılandırma

### Dağıtım
1. `TJoin-1.0.0.jar` dosyasını sunucunuzun `plugins` dizinine aktarın.
2. Ekonomi tabanlı gereksinimler kullanılacaksa, **Vault** ve uyumlu bir ekonomi sağlayıcısının kurulu olduğundan emin olun.
3. Eklentiyi başlatmak için sunucuyu yeniden başlatın.

### Zindan Oluşturma İşlemi
1. Zindan Yönetim arayüzüne erişmek için `/tjoin admin` komutunu yürütün.
2. **Yeni Zindan Oluştur** butonunu seçin.
3. Yeni zindan için benzersiz bir kimlik dizesini (ID) sohbete girin.
4. Başarılı oluşturma işleminin ardından zindan, daha fazla yapılandırma için yönetim arayüzünde görünecektir.

### Zindan Yapılandırması
`/tjoin admin` menüsünden mevcut bir zindanı seçmek, aşağıdaki yapılandırma parametrelerine erişim sağlar:
* **Görünen İsim Değişikliği:** Yöneticiden sohbet yoluyla yeni bir biçimlendirilmiş isim girmesini ister.
* **Gereksinim Değişikliği:** Giriş ön koşulunu Ücretsiz, Para ve Anahtar seçenekleri arasında değiştirir.
* **Bekleme Süresi Yapılandırması:** Yöneticiden giriş kısıtlama süresini saniye cinsinden tanımlamasını ister.
* **Lokasyon Ataması:** Yöneticiye bir yapılandırma eşyası (Lokasyon İncisi) tahsis eder. Bu eşyaya sağ tıklanması, yöneticinin bakış açısı (yaw, pitch) dâhil olmak üzere kesin mekânsal koordinatlarını zindanın giriş noktası olarak kaydeder.
* **Silme İşlemi:** Zindan profilini ve ilişkili verileri sistemden kalıcı olarak kaldırır.

## Komutlar ve Yetkiler

| Komut | Açıklama | Yetki Düğümü |
| :--- | :--- | :--- |
| `/tjoin` | Birincil zindan seçim arayüzüne erişim sağlar. | *Yok* |
| `/tjoin admin` | Yönetimsel yönetim arayüzüne erişim sağlar. | `tjoin.admin` |
| `/tjoin getkey <id>` | Belirtilen bir zindan için erişim anahtarı oluşturur ve temin eder. | `tjoin.admin` |
| `/tjoin reload` | Tüm yapılandırma dosyalarını ve önbellek verilerini yeniden başlatır. | `tjoin.admin` |

**Kısıtlama Muafiyeti:** `tjoin.bypass.cooldown` yetki düğümüne sahip hesaplar, yapılandırılmış giriş kısıtlama sürelerinden muaftır.
