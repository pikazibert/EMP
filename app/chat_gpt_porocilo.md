# Poročilo o projektu

## 1. Kratek opis projekta
MyPalette je interaktivna aplikacija za Android, zasnovana za ustvarjanje barvnih palet. Aplikacija omogoča generiranje harmoničnih barvnih shem glede na uporabnikov izbor, shranjevanje, deljenje in filtriranje shranjenih palet. Glavna cilja projekta sta podpora ustvarjalcem pri oblikovanju privlačnih barvnih kombinacij in zagotavljanje enostavne uporabe.

## 2. Tehnologije, uporabljene pri razvoju rešitve
- **Jezik:** Kotlin
- **Platforma:** Android
- **Knjiznice:**
    - `yuku.ambilwarna` za izbiro barv
    - `AppCompatDelegate` za upravljanje tem
- **Arhitektura:** Aktivnosti (`MainActivity`, `SavedPalettesActivity`, `SettingsInfoActivity`) in lokalne shranjevalne zmogljivosti (`SharedPreferences`).

## 3. Opis implementiranih funkcionalnosti in primerov uporabe
- **Generiranje barvnih shem:** Izbor harmoničnih shem (komplementarne, analogne, triadne itd.) na podlagi HEX kode barve.
- **Upravljanje palet:** Shramba barvnih palet z možnostjo filtriranja glede na število barv in priljubljenost.
- **Izbira teme:** Možnost preklapljanja med dnevnimi, nočnimi in sistemskimi nastavitvami.
- **Deljenje palet:** Deljenje izbranih palet prek drugih aplikacij.
- **Zgodovina barv:** Prikaz zgodovine zadnjih petih izbranih barv.

## 4. Arhitekturna zasnova aplikacije
Aplikacija temelji na treh glavnih aktivnostih:
1. **MainActivity:** Osrednja aktivnost za vnos barve, generiranje shem in prikaz trenutne palete.
2. **SavedPalettesActivity:** Aktivnost za prikaz in upravljanje shranjenih palet.
3. **SettingsInfoActivity:** Aktivnost za spreminjanje nastavitev teme in ogled informacij o projektu.

Shranjevanje podatkov je izvedeno z uporabo `SharedPreferences`, kjer se barvne palete shranjujejo kot nizovi HEX kod.

## 5. Uporaba ViewModel, UI State in StateFlow
ViewModel, UI State in StateFlow v trenutni implementaciji niso uporabljeni. Logika in stanje aplikacije sta neposredno povezana z aktivnostmi, kar je enostavnejša, a manj modularna rešitev.

## 6. Uporaba zunanjih API-jev
Aplikacija ne uporablja zunanjih API-jev. Generiranje barvnih shem temelji na interni knjižnici `ColorUtils` (ni vključena v posredovani kodi). Uporabljena je knjižnica `yuku.ambilwarna` za dialog izbire barv.

## 7. Podatkovni model
Podatki o shranjenih paletah so organizirani v naslednji obliki:
- **Palette Key:** Edinstven ključ, ki identificira posamezno paleto (`palette_{časovni žig}`).
- **Barve:** Niz HEX kod, shranjenih kot `Set<String>`.
- **Priljubljenost:** Bool vrednost (`liked_{key}`), ki označuje, ali je paleta priljubljena.

### Primer sheme:
```plaintext
Key: palette_1693852495084
Value: ["#FFFFFF", "#000000", "#FF5733"]
Liked: false
```

Ta struktura omogoča enostavno upravljanje, prikaz in filtriranje podatkov v aplikaciji.
