package Pamatky;

import Model.Zamek;
import Table.TableException;
import util.eTypKey;
import util.eTypProhl;

import java.io.FileNotFoundException;
import java.util.Iterator;

public interface IPamatky {
    int importDatZTXT(String nazev) throws FileNotFoundException, TableException;// provede import dat z textového souboru
    void vlozZamek(Zamek zamek) throws TableException;// vloží do BVS nový záznam
    Zamek najdiZamek(String klic) throws TableException;// vyhledá zámek dle klíče
    Zamek odeberZamek(String klic) throws TableException;// odebere zámek dle klíče
    void zrus();// zruší BVS
    void prebuduj() throws TableException;//* přebuduje BVS podle požadovaného klíče
           // (Název/ GPS)
    void nastavKlic(eTypKey typ);// nastaví typ klíče (Název/ GPS)
    Zamek najdiNejbliz(String klic) throws TableException;// vyhledá nejbližší zámek dle klíče GPS
                                    //(pokud je aktuálně klíč typu GPS)

    Iterator<Zamek> vytvorIterator(eTypProhl typ) throws TableException;
    eTypKey getActualKlic();
}
