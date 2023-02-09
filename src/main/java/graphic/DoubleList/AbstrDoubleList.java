package graphic.DoubleList;

import java.util.Iterator;

public class AbstrDoubleList<T> implements IAbstrDoubleList<T>{

    private Prvek<T> first;
    private Prvek<T> last;
    private Prvek<T> aktualni;

    private static class Prvek<T>{
        public Prvek<T> previous;
        public Prvek<T> next;
        public T data;
        public Prvek(Prvek<T> previous, T data, Prvek<T> next) {
            this.previous=previous;
            this.next=next;
            this.data = data;
        }
    }

    @Override
    public void zrus() {
        first =null;
        last =null;
        aktualni =null;
    }

    @Override
    public boolean jePrazdny() {
        return first == null;
    }

    @Override
    public void vlozPrvni(T data) {
        if(jePrazdny()){
               Prvek<T> prvni = new Prvek<>(null,data,null);
               prvni.previous=prvni;
               prvni.next=prvni;
               first = prvni;
               last = prvni;
               aktualni = first;
        }
        else {
            Prvek<T> prvni = new Prvek<>(last,data,first);
            last.next = prvni;
            first.previous = prvni;
            first = prvni;
        }
    }

    @Override
    public void vlozNaslednika(T data) throws LinkedListException {
        if(aktualni == null) throw new LinkedListException("Actual data not exist");
        if(aktualni == last) vlozPosledni(data);
        else {
            Prvek<T> newActual = new Prvek<>(aktualni,data,aktualni.next);
            aktualni.next.previous = newActual;
            aktualni.next = newActual;
        }
    }


    @Override
    public void vlozPosledni(T data) {
        if(jePrazdny()) vlozPrvni(data);
        else if(first==first.next){
            Prvek<T> posledni = new Prvek<>(first,data, first);
            first.next=posledni;
            last =posledni;
            first.previous=posledni;
            aktualni = last;
        }
        else {
            Prvek<T> prvek1 = new Prvek<>(last,data,first);
            first.previous= prvek1;
            last.next= prvek1;
            last = prvek1;
        }
    }

    @Override
    public void vlozPredchudce(T data) throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Actual data is not aviliable");
        if(aktualni == first) vlozPrvni(data);
        else {
            Prvek<T> predchudce = new Prvek<>(aktualni.previous,data, aktualni);
            aktualni.previous.next  = predchudce;
            aktualni.previous=predchudce;
            aktualni =predchudce;
        }
    }

    @Override
    public T zpristupniAktualni() throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Actual data is not aviliable");
        return aktualni.data;
    }

    @Override
    public T zpristupniPrvni() throws LinkedListException {
        if(first ==null) throw new LinkedListException("First data is not exist");
        aktualni = first;
        return aktualni.data;
    }

    @Override
    public T zpristupniPosledni() throws LinkedListException {
        if(last ==null) throw new LinkedListException("Last data is not exist");
        aktualni = last;
        return aktualni.data;
    }

    @Override
    public T zpristupniNaslednika() throws LinkedListException {
        if(aktualni == null) throw new LinkedListException("Actual data is not aviliable");
        aktualni = aktualni.next;
        if(aktualni == first) throw new LinkedListException("List was finished");
        return aktualni.data;
    }

    @Override
    public T zpristupniPredchudce() throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Actual data is not aviliable");
        aktualni = aktualni.previous;
        if(aktualni == last) throw new LinkedListException("List was finished");
        return aktualni.data;
    }

    @Override
    public T odeberAktualni() throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Aktualni prvek neni nastavlen");
        Prvek<T> aktualni = this.aktualni;
        this.aktualni.previous.next= this.aktualni.next;
        this.aktualni.next.previous= this.aktualni.previous;
        if(last == aktualni) last = first.previous;
        else if(first ==aktualni) first = last.next;
        this.aktualni = first;
        return aktualni.data;
    }

    @Override
    public T odeberPrvni() throws LinkedListException {
        if(first ==null) throw new LinkedListException("Prvni prvek neexistuje");
        Prvek<T> prvni = first;
        if(first.next == first) {
            zrus();
            return prvni.data;
        }
        first = first.next;
        last.next= first;
        first.previous= last;
        return prvni.data;
    }

    @Override
    public T odeberPosledni() throws LinkedListException {
        if(last==null) throw new LinkedListException("Prvni prvek neexistuje");
        Prvek<T> posledi = last;
        if(last.next==last) {
            zrus();
            return posledi.data;
        }
        last = last.previous;
        last.next= first;
        first.previous= last;
        return posledi.data;
    }

    @Override
    public T odeberNaslednika() throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Aktualni prvek neni nastavlen");
        Prvek<T> naslednik = aktualni.next;
        if(first ==naslednik) odeberPrvni();
        else if(last ==naslednik) odeberPosledni();
        else {
            aktualni.next = aktualni.next.next;
            aktualni.next.previous = aktualni;
        }
        return naslednik.data;
    }

    @Override
    public T odeberPredchudce() throws LinkedListException {
        if(aktualni ==null) throw new LinkedListException("Aktualni prvek neni nastavlen");
        Prvek<T> predchudce = aktualni.previous;
        if(first ==predchudce)odeberPrvni();
        else if(last ==predchudce) odeberPosledni();
        else {
            aktualni.previous.previous.next= aktualni;
            aktualni.previous= aktualni.previous.previous;
        }
        return predchudce.data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Prvek<T> iter = last;
            Boolean zkontrolujPrvni = true;
            @Override
            public boolean hasNext() {
                if(iter ==null) return false;
                if(zkontrolujPrvni){
                    zkontrolujPrvni =false;
                    return first!=null;
                }
                return iter.next!=first;
            }

            @Override
            public T next() {
                iter = iter.next;
                return iter.data;
            }
        };
    }




}
