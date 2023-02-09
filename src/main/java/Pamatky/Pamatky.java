package Pamatky;

import FifoALifo.AbstFifo;
import FifoALifo.IAbstrFifoALifo;
import Model.GPS;
import Model.Zamek;
import Table.AbstTable;
import Table.IAbstrTable;
import Table.TableException;
import util.Perzistance;
import util.eTypKey;
import util.eTypProhl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Pamatky implements IPamatky{

    private IAbstrTable<Zamek,Zamek> strom;
    private eTypKey actualTyp;

    public Pamatky() {
        this.strom = new AbstTable<>();
        this.actualTyp = eTypKey.NAZEV;;
    }

    @Override
    public int importDatZTXT(String nazev) throws FileNotFoundException, TableException {
        return Perzistance.nacist(strom,nazev,actualTyp);
    }

    @Override
    public void vlozZamek(Zamek zamek) throws TableException {
        strom.vloz(zamek,zamek);
    }

    @Override
    public Zamek najdiZamek(String klic) throws TableException {
        if(actualTyp==eTypKey.GPS){
            String[] gps = klic.split(" ");
            return strom.najdi(new Zamek(null,null,new GPS(Integer.parseInt(gps[0]),Integer.parseInt(gps[1])),actualTyp));
        }
        else return strom.najdi(new Zamek(null,klic,null,actualTyp));
    }

    @Override
    public Zamek odeberZamek(String klic) throws TableException {
        if(actualTyp==eTypKey.GPS){
            String[] gps = klic.split(" ");
            return strom.odeber(new Zamek(null,null,new GPS(Integer.parseInt(gps[0]),Integer.parseInt(gps[1])),actualTyp));
        }
        else return strom.odeber(new Zamek(null,klic,null,actualTyp));

    }

    @Override
    public void zrus() {
        strom.zrus();
    }

    @Override
    public void prebuduj() throws TableException {

            Iterator<Zamek> iter = strom.vytvorIterator(eTypProhl.SIRKA);
            List<Zamek> list = new ArrayList<>();

            while (iter.hasNext()) {
                Zamek zamek = iter.next();
                zamek.setTyp(actualTyp);
                list.add(zamek);
            }

            Comparator<Zamek> comparator = null;
            switch (actualTyp) {
                case NAZEV -> {
                    comparator = Comparator.comparing(Zamek::getNazev);
                }
                case GPS -> {
                    comparator = Comparator.comparing(Zamek::getGps);
                }
            }

            list.sort(comparator);
//            list.forEach(System.out::println);

            strom.zrus();
            prebuduj(list);
    }

    @Override
    public void nastavKlic(eTypKey typ) {
        switch (typ){
            case GPS -> actualTyp = eTypKey.GPS;
            case NAZEV -> actualTyp = eTypKey.NAZEV;
        }
    }

    @Override
    public Zamek najdiNejbliz(String klic) throws TableException {
            if(actualTyp==eTypKey.GPS){
                String [] klic1 = klic.split(" ");
                return strom.najdiNejblizsi(new Zamek("","",new GPS(Integer.parseInt(klic1[0]),
                        Integer.parseInt(klic1[1])),eTypKey.GPS));
            }
        return  null;
    }

    @Override
    public Iterator<Zamek> vytvorIterator(eTypProhl typ) throws TableException {
        return strom.vytvorIterator(typ);
    }

    public eTypKey getActualKlic(){
        return actualTyp;
    }
    private void prebuduj(List<Zamek> list) throws TableException {
        if (list.size() == 0) {
            return;
        }
        if (list.size() == 1) {
            vlozZamek(list.get(0));
            return;
        }

        int mid = list.size()/2;

        System.out.println(list.get(mid));
        vlozZamek(list.get(mid));

        prebuduj(list.subList(0, mid));
        prebuduj(list.subList(mid+1, list.size()));
    }
}
