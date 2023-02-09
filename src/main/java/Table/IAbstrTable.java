package Table;

import util.eTypProhl;

import java.util.Iterator;

public interface IAbstrTable<K extends Comparable<K>, V> {
    void zrus();
    boolean jePrazdny();
    V najdi(K key) throws TableException;
    void vloz(K key,V value) throws TableException;
    V odeber(K key) throws TableException;
    Iterator vytvorIterator(eTypProhl e) throws TableException;

    V najdiNejblizsi(K key);
}