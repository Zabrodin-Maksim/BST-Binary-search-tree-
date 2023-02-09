package FifoALifo;

public interface IAbstrFifoALifo<T> {

    void zrus();

    boolean jePrazdny();

    void vloz(T data);

    T odeber();
}
