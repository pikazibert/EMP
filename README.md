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
  - **AppCompatDelegate**: Omogoča upravljanje temne in svetle teme aplikacije.
  - **ClipData in ClipboardManager**: Omogočata kopiranje podatkov v odložišče.
  
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
- Vizualizacija generirane barvne palete se posodobi ob vsaki spremembi barve ali izbire druge barvne sheme.
- Uporabnik ima možnost shranjevanja palet za kasnejšo uporabo.
- Aplikacija omogoča kopiranje HEX kode barve v odložišče z enim klikom na barvo.
- Na dnu strani je zgodovina zadnjih 5 izbranih barv prek barvenga izbirnika ali HEX kode, uporabnik lahko kopira kodo in to barvo ponovno uporabi za generiranje palet.

### 2. **SavedPalettesActivity - pregled shranjenih palet**
- Prikaz vseh shranjenih barvnih palet.
- Gumb za deljenje barvne palete (kopiranje v odložiče ali prek drugih aplikacij)
- Gumb za dodajanje barvne palete med priljubljene
- Možnost filtriranja palet na več načinov:
  - All - prikaže vse barvne  palete (privzeta opcija),
  - 2 Colors - prikaže komplementarne barvne palete,
  - 3 colors - prikaže monokromatske, analogne in triadične barvne palete,
  - 4 colors - prikaže tetradične barrvne palete
- Kopiranje barve v odložišče s pritiskom na barvo.

### 3. **SettingsInfoActivity - nastavitve**
- Kratek opis aplikacije in infografika barvnih shem.
- Možnost spremembe teme:
  - Auto (zazna preferenco naprave, privzeta),
  - Day (oz. light mode, belo ozaadje)
  - Night (oz. dark mode, temno sivo ozadje)
- Možnost brisanja podatkov s pritiskom na gumb in potrditev izbrisa vseh barvih shem.
  
---

## Kratek opis arhitekturne zasnove aplikacije
- **Glavne aktivnosti**:
  - `MainActivity`: Glavna dejavnost za generiranje in vizualizacijo barvnih palet.
  - `SavedPalettesActivity`: Aktivnost za pregled shranjenih barvnih palet.
  - `SettingsInfoActivity`: Aktivnost za upravljanje nastavitev, kot so tema aplikacije in brisanje podatkov.
  - `OnlinePallets`: Aktivnost, ki omogoča nalaganje in prikaz barvnih palet iz spletne storitve **ColourLovers** preko zunanjih API-jev.
    
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

## **Uporaba zunanjih API-jev**

Aplikacija **MyPalette** uporablja zunanji API za pridobivanje barvnih palet iz spletne storitve **ColourLovers**. API omogoča dostop do različnih barvnih palet, ki so razvrščene v več kategorij, kot so **popularne**, **nove** in **naključne**. Aplikacija uporablja te podatke za prikazovanje barvnih palet v uporabniškem vmesniku.

### **Uporaba API-ja**:
- API klici se izvajajo ob izbiri kategorije v spustnem seznamu, kjer uporabnik izbere vrsto barvnih palet (npr. "Popular", "New", "Random").
- Klic API-ja se izvede z uporabo knjižnice **Volley**, ki omogoča asinhrono nalaganje podatkov iz API-ja in obdelavo rezultatov.
- API vrača podatke v obliki **JSON**, ki jih aplikacija obdeluje in prikazuje uporabniku v obliki barvnih palet.

### **Pridobivanje podatkov iz API-ja**:
Aplikacija uporablja naslednje API konce:
- **Popularne palete**: `https://www.colourlovers.com/api/palettes/top?format=json&numResults=10`
- **Nove palete**: `https://www.colourlovers.com/api/palettes/new?format=json&numResults=10`
- **Naključne palete**: `https://www.colourlovers.com/api/palettes/random?format=json&numResults=10`

### **Uporabniški vmesnik**:
- Ko uporabnik izbere eno od kategorij (npr. "Popular"), aplikacija pokliče ustrezen API in prikaže seznam barvnih palet.
- Vsaka paleta vsebuje seznam barv (HEX kode), ki se prikažejo uporabniku skupaj z naslovom palete.


---

## **Podatkovni model (shema)**

### **Shema podatkov**
- **Barvna paleta**:
  - **ID**: Unikaten identifikator.
  - **Seznam barv**: HEX kode barv v paleti.
  - **Priljubljena**: Boolean, označuje, ali je paleta označena kot priljubljena.
- **Zgodovina**:
  - Zadnjih 5 izbranih barv (HEX kode).
- **Tema**:
  - Izbrana tema aplikacije (Auto, Day, Night).

### **Opis podatkovnega modela**
- Podatkovni model temelji na enostavni uporabi `SharedPreferences` za trajno shranjevanje barvnih palet in zgodovine.
- **Barvna paleta**:
  - Vsebuje seznam HEX kod, status priljubljenosti in čas shranjevanja.
  - Shranjena v obliki ključ-vrednost, kjer ključ predstavlja ID palete, vrednost pa seznam barv.
- **Zgodovina**:
  - Beleži zadnjih 5 izbranih barv in se posodablja z vsakim novim izborom barve.
- **Tema**:
  - Shranjena kot niz (Auto, Day, Night) in se uporablja za nastavitev videza aplikacije.




