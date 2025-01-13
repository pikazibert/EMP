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
  
---

## Povezava do repozitorija na GitHubu
[GitHub Repository](https://github.com/pikazibert/EMP)

---

## Kratek opis implementiranih funkcionalnosti in primerov uporabe

### 1. **MainActivity - generiranje barvnih palet**
- Uporabnik vnese HEX barvno kodo ali uporabi barvni izbirnik, da začne z generiranjem barvne palete.
- Izbira med različnimi barvnimi shemami (privzeta je komplementarna):
  - Komplementarne,
  - Monokromatske,
  - Analogne,
  - Triadične,
  - Tetradične.
- Vizualizacija generirane barvne palete se posodobi ob vsaki sprembi barve ali izbire druge barvne sheme.
- Uporabnik ima možnost shranjevanja palet za kasnejšo uporabo.
- Aplikacija omogoča kopiranje HEX kode barve v odložišče z enim klikom na barvo.
- Na dnu strani je zgodovina zadnjih 5 izbranih barv prek barvenga izbirnika ali HEX kode, uporabnik lahko kopira kodo in to barvo ponovno uporabi za generiranje palet.

### 2. **SavedPalettesActivity - pregled shrajenih palet**
- Prikaz vseh shranjenih barvnih palet.
- Gumb za deljenje barvne palete (kopiranje v odložiče ali prek drugih aplikacij)
- Gumb za dodajanje barvne palete med priljubljene
- Možnost filtriranja palet na več načinov:
  - All - prikaže vse barvne  palete (privzeta opcija),
  - 2 Colors - prikaže komplementarne barvne palete,
  - 3 colors - prikaže monokromatske, analogne in triadične barvne palete,
  - 4 colors - prikaže tetrdične barrvne palete
- Kopiranje barve v odložiče s pritiskom na barvo.

### 3. **SettingsInfoActivity - nastavitve**
- Kratek opis aplikacije in infografika barvnih shem.
- Možnost spremembe teme:
  - Auto (zazna preferenco naprave, privzeta),
  - Day (oz. light mode, belo ozaadje)
  - Night (oz. dark mode, temno sivo ozadje)
- Možnost brisanja podatov - vseh barvih shem
  
---

## Kratek opis arhitekturne zasnove aplikacije
- **Glavne aktivnosti**:
  - `MainActivity`: Glavna dejavnost za generiranje in vizualizacijo barvnih palet.
  - `SavedPalettesActivity`: Aktivnost za pregled shranjenih barvnih palet.
  - `SettingsInfoActivity`: Aktivnost za upravljanje nastavitev, kot so tema aplikacije in brisanje podatkov.
- **Shranjevanje podatkov**:
  - Uporaba `SharedPreferences` za trajno shranjevanje barvnih palet, zgodovine in nastavitev teme.
- **Logika generiranja**:
  - Implementirana v objektu `ColorUtils`, ki vsebuje metode za izračun barvnih shem.

---

## Kratek opis življenjskega cikla aktivnosti
- **`onCreate`**: Inicializira uporabniški vmesnik in nastavitve.
- **`onPause`**: Shrani trenutno stanje barvne palete in temo.
- **`onResume`**: Obnovi zadnje stanje aplikacije (barve, tema).
- **`onDestroy`**: Čiščenje virov in priprava na zaprtje aplikacije.
- 
---

## Uporaba ViewModel, UI State in StateFlow
- / 

---

## Uporaba zunanjih API-jev
- TODO

---

## Podatkovni model (shema)
### Shema podatkov
- **Barvna paleta**:
  - ID (unikaten identifikator)
  - Seznam barv (HEX kode)
  - Označeno kot priljubljeno (boolean)
- **Zgodovina**:
  - Zadnjih 5 izbranih barv
- **Tema**:
  - 

### Opis podatkovnega modela
Podatkovni model temelji na enostavni shrambi barvnih palet in zgodovine v `SharedPreferences`. Vsaka paleta vsebuje seznam barv, status priljubljenosti in čas shranjevanja. Zgodovina beleži zadnjih 5 izbranih barv.



