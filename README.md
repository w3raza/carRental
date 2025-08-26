# Car Rental System

Projekt Car Rental System to aplikacja webowa napisana w języku Java z użyciem Spring Boot, służąca do zarządzania wypożyczalnią samochodów.

## Funkcje
- Przeglądanie dostępnych samochodów
- Rezerwacja samochodu
- Zarządzanie rezerwacjami
- Inicjalizacja przykładowych danych

## Technologie
- Java 21
- Spring Boot
- Maven

## Struktura projektu
```
carRental-master/
├── mvnw, mvnw.cmd         # Wrapper Maven
├── pom.xml               # Konfiguracja Maven
├── src/
│   ├── main/
│   │   ├── java/com/carrental/
│   │   │   ├── CarRentalApplication.java      # Punkt wejścia
│   │   │   ├── config/DataInitializer.java    # Inicjalizacja danych
│   │   │   ├── controller/CarRentalController.java # Kontroler REST
│   │   │   ├── model/                        # Modele danych
│   │   │   ├── repository/                   # Repozytoria JPA
│   │   │   └── service/CarRentalService.java # Logika biznesowa
│   │   └── resources/application.properties  # Konfiguracja aplikacji
│   └── test/java/com/carrental/CarRentalApplicationTests.java # Testy
```

## Uruchomienie
1. Zainstaluj JDK 21 oraz Maven.
2. W katalogu projektu uruchom:
   ```pwsh
   ./mvnw spring-boot:run
   ```
3. Aplikacja będzie dostępna pod adresem `http://localhost:8080`.