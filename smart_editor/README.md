# Protingas teksto redaktorius

**Autorius:** Ugnius Teišerskis  

**Data:** 2025 m. gegužės 21 d.

## Paskirtis

Šios programos paskirtis yra suteikti vartotojui teksto redagavimo įrankį, kurio pagrindinės funkcijos yra rašybos tikrinimas, žodžių nuspėjimas ir teksto vertimas. Programa sukurta naudojant objektinio programavimo principus ir projektavimo šablonus, siekiant gilinti objektinio programavimo žinias ir gebėjimus.

## Paleidimas

Programa paleidžiama vykdant `EditorApp` klasę, kuri inicijuoja vartotojo sąsajos užkrovimą. Norint paleisti programą, reikia:

1. Įsitikinti, kad visi projekto failai yra teisingai sukonfigūruoti
2. Paleisti `main.java.launcher.EditorApp` klasę
3. Programa automatiškai atvers grafinės vartotojo sąsajos langą

## Funkcionalumas

Programa turi šias pagrindines funkcijas:

- **Teksto redagavimas** - vartotojas gali įvesti, trinti, modifikuoti tekstą
- **Rašybos tikrinimas** - tikrina žodžius pagal pasirinkta žodyną ir pažymi neatpažintus žodžius
- **Teksto vertimas** - verčia tekstą pagal nurodytus žodynus (šaltinio ir tikslo)
- **Žodžių pasiūlymai** - siūlo galimus žodžius rašant tekstą
- **Redaktoriaus išsaugojimas/įkėlimas** - leidžia išsaugoti esamą redaktoriaus būseną ir ją vėliau atkurti
- **Žodynų pasirinkimas** - leidžia pasirinkti žodynus rašybos tikrinimui ir vertimui

## Pagrindinės klasės

Programą sudaro šios pagrindinės klasės:

### Editors paketas:
- `Editor` - abstrakti bazinė klasė, teikianti pagrindines teksto redagavimo funkcijas
- `SpellCheckEditor` - redaktorius su rašybos tikrinimo funkcionalumu
- `TranslateEditor` - redaktorius su vertimo funkcionalumu

### Interfaces paketas:
- `Editable` - apibrėžia pagrindinį teksto redagavimo funkcionalumą
- `Transformable` - apibrėžia teksto transformavimo galimybę

### Factory paketas:
- `EditorFactory` - abstrakti gamyklos klasė redaktorių sukūrimui
- `SpellCheckEditorFactory` - sukuria rašybos tikrinimo redaktorių
- `TranslateEditorFactory` - sukuria vertimo redaktorių

### GUI paketas:
- `EditorGUI` - pagrindinė vartotojo sąsajos klasė
- `EditorPanel` - teksto redagavimo laukas
- `ControlPanel` - valdymo mygtukai ir pasirinkimai
- `StatusPanel` - būsenos informacijos rodymas
- `EditorManager` - redaktorių valdymas

### Prediction paketas:
- `WordPredictor` - teikia žodžių pasiūlymus

### Serialization paketas:
- `EditorSerializer` - redaktoriaus išsaugojimas ir atkūrimas

### Exceptions paketas:
- `EditorException` - apibendrinta teksto redaktoriaus klaida
- `InvalidWordException` - klaidingų žodžių apdorojimas

## Klasių diagrama
![image](UML%20Class%20Diagram.png)
## Projektavimo šablonai

Programoje panaudoti šie projektavimo šablonai (design patterns):

1. **Factory Method** - naudojamas `EditorFactory`, `SpellCheckEditorFactory` ir `TranslateEditorFactory` klasėse redaktorių objektų sukūrimui. Šis šablonas leidžia kurti skirtingų tipų redaktorius per bendrą sąsają.

2. **Template Method** - naudojamas `Editor` abstrakčioje klasėje, kur bendras elgesys apibūdintas pagrindinėje klasėje, o specifiniai metodai (pvz., `transform()`) įgyvendinami paveldėtose klasėse.

3. **Command Pattern** elementai - matomi programos valdymo panelėje, kur vartotojo veiksmai atskiriami nuo jų įgyvendinimo.

4. **Prototype** - implementuojamas per `clone()` metodą `Editor` klasėje, leidžiantį kopijuoti redaktorių objektus.

5. **Strategy** - panašūs elementai matomi taikant skirtingus teksto transformavimo algoritmus per bendrą `Transformable` sąsają.

## Plėtimo galimybės

Programą galima plėsti šiomis kryptimis:

- Įdiegti papildomus redaktorių tipus, pvz., formatavimo redaktorių ar stilistikos tikrinimo redaktorių
- Pridėti daugiau žodžių pasiūlymo algoritmų, ne tik pagrįstų prefix atitikimu
- Įgyvendinti teksto formatavimo funkcijas (bold, italic, spalvos)
- Pridėti atšaukimo/pakartojimo (undo/redo) funkcionalumą naudojant Command pattern
- Įtraukti automatinio išsaugojimo funkcionalumą
- Pridėti galimybę keisti teksto šrifto dydį ir stilių
- Sukurti galimybę vienu metu atidaryti kelis redaktorių langus

## Išvados

Sukurta teksto redagavimo programa demonstruoja įvairius objektinio programavimo principus ir šablonus. Programa pasižymi moduline struktūra, kuri leidžia ją lengvai plėsti pridedant naujus redaktorių tipus ar funkcionalumą. Taikant tokius šablonus kaip Factory Method, Template Method ir kitus, programa tampa lankstesnė ir lengviau palaikoma. Abstrakčių klasių ir sąsajų naudojimas užtikrina gerą kodo struktūrą ir aiškumą.