🧾Stok Takip Sistemi (Backend)

📌 Proje Tanımı

Stok Takip Sistemi, firmalar, ürünler, satışlar, ödemeler ve cariler üzerinden stok, bakiye ve satış takibini yapmayı sağlayan bir sistemdir.
Amaç, tüm stok hareketlerini ve finansal işlemleri merkezi bir REST API üzerinden yönetmektir.

⚙️ Teknolojiler

Backend Framework	Spring Boot

ORM	JPA + Hibernate

Veritabanı	PostgreSQL

Mapping	MapStruct

RabbitMQ

Güvenlik	JWT + Spring Security

Loglama	SLF4J / Logback

Test JUnit 5 + Mockito 

Mimari	Layered + Clean Architecture

🔐 Kimlik Doğrulama (JWT)

Projede kimlik doğrulama için JWT (JSON Web Token) kullanılmıştır.
Kullanıcı, /api/auth/login endpoint’ine kullanıcı adı ve şifre ile istek atar.
Başarılı giriş sonrası sistem bir token döner ve sonraki isteklerde Authorization: Bearer <token> başlığıyla bu token kullanılır.

🗂️ Ana Modüller

🏷️ Kategori Yönetimi

Ürünlerin ait olduğu kategorilerin oluşturulmasını ve yönetimini sağlar.
Endpoint: /api/categories

📦 Stok (Ürün) Yönetimi

Ürün ekleme, güncelleme, silme, arama ve stok durumlarının takibi yapılır.
Ayrıca stok ürünler raporlanabilir ve Excel formatında dışa aktarılabilir.
Endpoint: /api/products

🧾 Cari (Firma) Yönetimi

Firmaların kayıt, güncelleme, silme ve bakiye hesaplamalarını yönetir.
Endpoint: /api/firms

💰 Satış & Ödeme Yönetimi

Satış oluşturma, ürün ekleme ve ödeme işlemleri tek akışta yönetilir.
Ödemeye göre satış durumu (PENDING, PARTIAL_PAID, PAID) otomatik güncellenir.
Endpointler: /api/sales, /api/payments

📊 Cari Takip Raporu

Bu sayfa, tüm carilerin borç, alacak ve bakiye durumlarının anlık olarak takip edilmesini sağlar.
Ayrıca Excel formatında dışa aktarım desteklenir.
Endpoint: /api/firms/balances

🧠 Genel Özellikler

JWT tabanlı güvenlik

Soft delete yapısı

Global exception handling

Excel raporlama desteği

SLF4J + Logback ile loglama

Katmanlı ve temiz mimari yaklaşımı
