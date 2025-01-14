# MyPalette

## Člani skupine
- **Jaša Kert Pust** (63220139)
- **Pika Žibert** (63220381)

---

## Kratek opis projekta
MyPalette je mobilna aplikacija, namenjena generiranju barvnih palet na podlagi izbrane začetne barve. Uporabniki lahko vnesejo HEX barvno kodo ali uporabijo barvni izbirnik za izbiro začetne barve. Aplikacija ponuja različne barvne sheme, kot so 
komplementarne, monokromatske, analogne, triadične in tetradične. Generirane barvne palete so vizualizirane v uporabniško prijaznem vmesniku, kjer jih uporabnik lahko shrani, deli ali označi kot priljubljene za kasnejšo uporabo. 

---

## **Tehnologije**
- **Android Studio**: Razvojno okolje za mobilne aplikacije.
- **Programski jezik**: **Kotlin**.
- **Knjižnice**:
  - **AmbilWarna**: Barvni izbirnik za enostavno izbiro barv.
  - **AppCompatDelegate**: Omogoča upravljanje temne in svetle teme aplikacije.
  - **ClipData in ClipboardManager**: Omogočata kopiranje podatkov v odložišče.
- **Zunanji API**:
  - **ColourLovers API**: Uporablja se za pridobivanje barvnih palet iz spletne storitve **ColourLovers**
  - **PhotoColor API**: Uporablja se za izločanje pogostosti barv iz fotografije.

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
  - 5 colors - shranjene palete z interneta
- Kopiranje barve v odložišče s pritiskom na barvo.

### 3. **SettingsInfoActivity - nastavitve**
- Kratek opis aplikacije in infografika barvnih shem.
- Možnost spremembe teme:
  - Auto (zazna preferenco naprave, privzeta),
  - Day (oz. light mode, belo ozaadje)
  - Night (oz. dark mode, temno sivo ozadje)
- Možnost brisanja podatkov s pritiskom na gumb in potrditev izbrisa vseh barvih shem.

### 4. **OnlinePallets - Nalaganje barvnih palet iz spletnih virov**
- Aktivnost, ki omogoča nalaganje barvnih palet iz spletne storitve **ColourLovers** preko zunanjih API-jev.
- Uporabnik lahko izbira med različnimi kategorijami barvnih palet:
  - **Popular**: Prikazuje najbolj priljubljene barvne palete.
  - **New**: Prikazuje nove barvne palete.
  - **Random**: Prikazuje naključne barvne palete.
- Aplikacija uporablja knjižnico **Volley** za asinhrono nalaganje podatkov iz API-ja.
- Ko uporabnik izbere kategorijo, aplikacija pokliče ustrezen API in prikaže seznam barvnih palet, vsaka z naborom barv v HEX formatu.
- Vsaka barvna paleta je prikazana z možnostjo:
  - **Shranjevanja**: Uporabnik lahko shrani paleto v svoje shranjene palete.
  - **Deljenja**: Omogočeno je deljenje barvnih palet prek drugih aplikacij.
- Funkcionalnosti vključujejo tudi kopiranje HEX kode barve v odložišče.

### 5. **FromPhotoActivity - Izvoz pogostih barv iz fotografije**
 - Aktivnost, ki omogoča analizo barv iz fotografije z uporabo spletne storitve PhotoColor API.
 - Uporabnik lahko fotografijo naloži:
   - **Iz galerije** z izbiro obstoječe slike.
   - **S kamero**, kjer fotografijo posname neposredno z aplikacijo.
 - Aplikacija analizira fotografijo in iz nje pridobi 12 najpogostejših barv, ki so prikazane v obliki barvnih kvadratov.
 - Funkcionalnosti vsakega barvnega kvadrata:
   - Kopiranje HEX kode: Uporabnik lahko z dotikom kopira HEX kodo barve v odložišče.
 - Uporabljene knjižnice in tehnologije:
   - **OkHttp** za pošiljanje slik na API.
   - **MediaStore** za izbiro slik iz galerije.
   - **GradientDrawable** za oblikovanje barvnih kvadratov z zaobljenimi robovi.
   - **ClipboardManager** za kopiranje barvne kode v odložišče.
   - **GridLayout** za dinamično prikazovanje barvnih kvadratov. 
---

## Kratek opis arhitekturne zasnove aplikacije
- **Glavne aktivnosti**:
  - `MainActivity`: Glavna dejavnost za generiranje in vizualizacijo barvnih palet.
  - `SavedPalettesActivity`: Aktivnost za pregled shranjenih barvnih palet.
  - `SettingsInfoActivity`: Aktivnost za upravljanje nastavitev, kot so tema aplikacije in brisanje podatkov.
  - `OnlinePallets`: Aktivnost, ki omogoča nalaganje in prikaz barvnih palet iz spletne storitve **ColourLovers** preko zunanjih API-jev.
  - `FromPhotoActivity`: Aktivnost, ki omogoča izločanje barv iz fotografije.

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

### **MyPalette**
-  Aplikacija uporablja zunanji API za pridobivanje barvnih palet iz spletne storitve **ColourLoversy**. API omogoča dostop do različnih barvnih palet, ki so razvrščene v več kategorij, kot so **popularne**, **nove** in **naključne**. Aplikacija uporablja te podatke za prikazovanje barvnih palet v uporabniškem vmesniku.

#### **Uporaba API-ja**:
- API klici se izvajajo ob izbiri kategorije v spustnem seznamu, kjer uporabnik izbere vrsto barvnih palet (npr. "Popular", "New", "Random").
- Klic API-ja se izvede z uporabo knjižnice **Volley**, ki omogoča asinhrono nalaganje podatkov iz API-ja in obdelavo rezultatov.
- API vrača podatke v obliki **JSON**, ki jih aplikacija obdeluje in prikazuje uporabniku v obliki barvnih palet.

#### **Pridobivanje podatkov iz API-ja**:
Aplikacija uporablja naslednje API konce:
- **Popularne palete**: `https://www.colourlovers.com/api/palettes/top?format=json&numResults=10`
- **Nove palete**: `https://www.colourlovers.com/api/palettes/new?format=json&numResults=10`
- **Naključne palete**: `https://www.colourlovers.com/api/palettes/random?format=json&numResults=10`

#### **Uporabniški vmesnik**:
- Ko uporabnik izbere eno od kategorij (npr. "Popular"), aplikacija pokliče ustrezen API in prikaže seznam barvnih palet.
- Vsaka paleta vsebuje seznam barv (HEX kode), ki se prikažejo uporabniku skupaj z naslovom palete.



### **PhotoColor API**

Aplikacija uporablja zunanji API za pridobivanje dvanajstih barv iz izbrane fotografije. To omogoča uporabniku, da enostavno izloči ključne barve iz fotografije in ustvari barvno paleto.

#### **Uporaba API-ja**:
- API klici se izvedejo ob izbiri fotografije, bodisi preko galerije bodisi z zajemom nove fotografije s kamero.
- Ko je slika izbrana, se pošlje v API za analizo, ki vrne dvanajst najpomembnejših barv.
- Te barve so nato prikazane v uporabniškem vmesniku kot barvni kvadrati.

#### **Pridobivanje podatkov iz API-ja**:
Aplikacija pošlje sliko na API končni naslov, kjer se izvede analiza barv in vrne seznam dvanajstih barv v obliki HEX kod.

#### API Končni točki:
- **URL za analizo slike**: `http://1kp3.com:5000/analyze`
  
Po pošiljanju slike, API vrne seznam barvnih kod, ki jih aplikacija prikaže uporabniku. Te kode so uporabljene za ustvarjanje vizualnih barvnih kvadratov, ki se prikažejo v uporabniškem vmesniku.

### **Uporabniški vmesnik**:
- Ko uporabnik izbere fotografijo (iz galerije ali z uporabo kamere), se pošlje na API za analizo.
- Ko API vrne rezultate, aplikacija prikaže barvne kvadrate, ki predstavljajo najpomembnejše barve na sliki.
- Barve so predstavljene kot HEX kode, ki jih uporabnik lahko kopira v svoje odložišče ob klikom na kvadrat.
---

## **Podatkovni model (shema)**

### **Shema podatkov**
- **Barvna paleta**:
  - **ID**: Unikaten identifikator.
  - **Seznam barv**: HEX kode barv v paleti.
  - **Liked**: Boolean, označuje, ali je paleta označena kot priljubljena.
  - **Downloaded**: Boolean, označuje, ali je bila paleta prenesena s spleta.
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




