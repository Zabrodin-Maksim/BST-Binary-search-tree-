import FifoALifo.AbstFifo;
import FifoALifo.IAbstrFifoALifo;
import Model.Zamek;
import Pamatky.Pamatky;
import Table.AbstTable;
import Table.IAbstrTable;
import Table.TableException;
import util.Perzistance;
import util.eTypKey;
import util.eTypProhl;

import java.util.Comparator;
import java.util.Iterator;

public class Main {


    public static void main(String[] args) throws TableException {

        IAbstrTable<Integer,String> table = new AbstTable<>();
        table.vloz(52,"1");
        table.vloz(40,"2");
        table.vloz(44,"3");
        table.vloz(45,"4");
        table.vloz(39,"5");
        System.out.println(table.odeber(44));
//        IAbstrFifoALifo<String> abst = new AbstFifo<>();
//        abst.vloz("1");
//        abst.vloz("2");
//        abst.vloz("3");
//        abst.vloz("4");
//        while (!abst.jePrazdny()){
//            System.out.println(abst.odeber());
//        }

        //        pamatky.nastavKlic(eTypKey.GPS);
//        pamatky.importDatZTXT();
//        String cord = "N50 15.9853 E016 06.9480"; //N49 17.7740 E016 31.4101
//        System.out.println(pamatky.najdiNejbliz(cord));


    }
}
