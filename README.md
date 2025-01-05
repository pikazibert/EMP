# MyPalette

## Člani skupine
- **Jaša Kert Pust** (63220139)
- **Pika Žibert** (63220381)

---

## Kratek opis projekta
MyPalette je mobilna aplikacija, namenjena generiranju barvnih palet na podlagi izbrane začetne barve. Uporabniki lahko vnesejo HEX barvno kodo ali uporabijo barvni izbirnik za izbiro začetne barve. Aplikacija ponuja različne barvne sheme, kot so 
komplementarne, monokromatske, analogne, triadične in tetradične. Generirane barvne palete so vizualizirane v uporabniško prijaznem vmesniku, kjer jih uporabnik lahko shrani, deli ali označi kot priljubljene za kasnejšo uporabo. 

---

## Tehnologije
- **Android Studio**: Razvojno okolje za mobilne aplikacije.
- **Programski jezik**: Kotlin.
- **Knjižnice**:
  - **AmbilWarna**: Barvni izbirnik za enostavno izbiro barv.
  - **Android Jetpack Components**: ViewModel, StateFlow, in LiveData za upravljanje stanja.
  
---

## Povezava do repozitorija na GitHubu
[GitHub Repository](https://github.com/pikazibert/EMP)

---

## Kratek opis implementiranih funkcionalnosti in primerov uporabe

### 1. Generiranje barvne palete
- Uporabnik vnese HEX barvno kodo ali uporabi barvni izbirnik.
- Izbira med različnimi barvnimi shemami, kot so komplementarne, analogne itd.
- Vizualizacija ustvarjene barvne palete.
- Možnost shranjevanja palet za kasnejšo uporabo.

### 2. Pregled shranjenih palet
- Uporabnik lahko pregleda vse shranjene barvne palete.
- Dodajanje palet med priljubljene in deljenje.

### 3. Dodatne funkcionalnosti
- Kopiranje HEX kode barve v odložišče z enim klikom.
- Prikaz zgodovine izbranih barv.
- Brisanje podatkov.
  
---

## Kratek opis arhitekturne zasnove aplikacije
- **MVVM Arhitektura**:
  - **Model**: Upravljanje podatkovnih struktur (barvne palete, zgodovina).
  - **ViewModel**: Upravljanje stanja UI in povezava z uporabniškim vmesnikom.
  - **View**: Prikaz podatkov in interakcija z uporabnikom.

- **Komponente Jetpack**:
  - **ViewModel**: Za upravljanje življenjskega cikla aktivnosti.
  - **StateFlow**: Za spremljanje stanja v realnem času.

---

## Kratek opis življenjskega cikla aktivnosti
- **OnCreate**: Inicializacija uporabniškega vmesnika, nastavitev privzetih barvnih shem in funkcionalnosti.
- **OnPause**: Shrani trenutno stanje barvne palete.
- **OnResume**: Obnovi zadnje stanje aplikacije.

---

## Uporaba ViewModel, UI State in StateFlow
- **ViewModel**: Upravljanje stanja barvne palete in shranjenih podatkov.
- **UI State**: Zajema trenutno izbrano barvno shemo in barvo.
- **StateFlow**: Posodablja uporabniški vmesnik v realnem času ob spremembi barvne sheme ali vnosa HEX kode.

---

## Uporaba zunanjih API-jev
- **AmbilWarna**: Knjižnica za izbiro barv, ki omogoča intuitiven barvni izbirnik.
- **ClipboardManager**: Omogoča kopiranje HEX kod barv v odložišče.

---

## Podatkovni model (shema)
### Shema podatkov
- **Barvna paleta**:
  - ID (unikaten identifikator)
  - Seznam barv (HEX kode)
  - Označeno kot priljubljeno (boolean)
- **Zgodovina**:
  - Zadnjih 5 izbranih barv

### Opis podatkovnega modela
Podatkovni model temelji na enostavni shrambi barvnih palet in zgodovine v `SharedPreferences`. Vsaka paleta vsebuje seznam barv, status priljubljenosti in čas shranjevanja. Zgodovina beleži zadnjih 5 izbranih barv.



