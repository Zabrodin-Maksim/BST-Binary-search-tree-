# Sem_B_Binary_search_treedat

A) Motivační přiklad:

V rámci semestrální práce B vybudujte vyhledávací jednotlivých památek, a to na 
základně zadaného kritéria typu řetězec (název památky, GPS). U GPS se jedná o složený 
klíč x, y.

B) Použité datové struktury:

Abstraktní datová struktura umožňující vyhledávání dle klíče je realizována jako binární 
vyhledávací strom (BVS) v dynamické paměti (tabulka na binárním stromu). 

Komponenta ABSTRTABLE pracuje s generickým prvkem

***K extends Comparable<K>, V*** 
> (K-key,V-value) a implementuje rozhraní:

***void zrus()*** 
> zrušení celé tabulky

***boolean jePrazdny()***
> test prázdnosti tabulky

***V najdi(K key)*** 
> vyhledá prvek dle klíče

***void vloz(K key, V value)*** 
> vloží prvek do tabulky

***V odeber(K key)***
> odebere prvek dle klíče z tabulky

***Iterator vytvorIterator (eTypProhl typ)*** 
> vytvoří iterátor, který umožňuje procházení stromu do šířky/hloubky ***(in-order)***

Iterátor využívá ADS ***zásobník/fronta*** (ABSTRLIFO/ ABSTRFIFO) postavenou nad ADS 
ze semestrální práce A (jako nová samostatná třída)

***void zrus()*** 
> zrušení celé fronty/zásobníku

***boolean jePrazdny()*** 
> test prázdnosti 

***void vloz(T data)*** 
> vloží prvek do zásobníku/fronty

***T odeber()***
> odebere prvek ze zásobníku/fronty

***Iterator vytvorIterator*** 
> vrací iterátor zásobníku/fronty

C) Pro ověření funkčnosti implementované ADS BVS v dynamické paměti vytvořte modul 
***Pamatky***. Tento modul implementuje následující rozhraní: 

***int importDatZTXT()***
> provede import dat z textového souboru

***int vlozZamek(Zamek zamek)*** 
> vloží do BVS nový záznam

***Zamek najdiZamek(String klic)***
> vyhledá zámek dle klíče 

***Zamek odeberZamek(String klic)***
> odebere zámek dle klíče 

***void zrus()*** 
> zruší BVS

***void prebuduj()**** 
> přebuduje BVS podle požadovaného klíče (Název/ GPS)

***void nastavKlic(eTypKey typ)***
> nastaví typ klíče (Název/ GPS)

***Zamek najdiNejbliz(String klic)***
> vyhledá nejbližší zámek dle klíče GPS (pokud je aktuálně klíč typu GPS)
***Iterator VytvorIterator(eTypProhl typ)***
>  vrací požadovaný typ iterátoru

****)Metoda prebuduj() přebuduje BVS tak aby byl strom vyvážený, a to na základně 
znalosti dimenze prvků a jejich klíčů.***

Modul ***Pamatky*** pracuje s datovým typem Zamek, který uchovává (i) id, (ii) název památky 
(iii) GPS. 

D) Pro obsluhu aplikace vytvořte grafické rozhraní ***ProgPamatky***

***Pozn.***: -Iterátor využívá ADS zásobník/fronta postavenou nad ADS ze semestrální práce A1
-V aplikaci ***nepoužívejte*** diakritické znaky

Zmíněný program nechť umožňuje zadávání vstupních dat z klávesnice, ze souboru a 
z generátoru, výstupy z programu nechť je možné zobrazit na obrazovce a uložit do souboru.
