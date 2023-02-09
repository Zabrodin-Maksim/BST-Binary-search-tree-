package FifoALifo;

import graphic.DoubleList.AbstrDoubleList;
import graphic.DoubleList.IAbstrDoubleList;
import graphic.DoubleList.LinkedListException;

public class AbstrLifo<T> implements IAbstrFifoALifo<T> {
    private final IAbstrDoubleList<T> doubleList;

    public AbstrLifo() {
        doubleList = new AbstrDoubleList<>();
    }

    @Override
    public void zrus() {
        doubleList.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return doubleList.jePrazdny();
    }

    @Override
    public void vloz(T data) {
        doubleList.vlozPosledni(data);
    }

    @Override
    public T odeber()  {
        try {
            return doubleList.odeberPosledni();
        } catch (LinkedListException e) {
            throw new RuntimeException(e);
        }
    }
}
