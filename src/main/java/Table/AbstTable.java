package Table;

import FifoALifo.AbstFifo;
import FifoALifo.AbstrLifo;
import FifoALifo.IAbstrFifoALifo;
import util.eTypProhl;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstTable<K extends Comparable<K>,V> implements IAbstrTable<K,V>{

    private Node<K,V> rootNode;


    public static class Node<K,V>{
        public Node<K,V> leftChild;
        public Node<K,V> rightChild;
        public Node<K,V> parentNode;
        public K key;
        public V value;

        public Node<K, V> getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node<K, V> leftChild) {
            this.leftChild = leftChild;
        }

        public Node<K, V> getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node<K, V> rightChild) {
            this.rightChild = rightChild;
        }

        public Node<K, V> getParentNode() {
            return parentNode;
        }

        public void setParentNode(Node<K, V> parentNode) {
            this.parentNode = parentNode;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node(Node<K, V> leftChild, Node<K, V> rightChild, K key, V value, Node<K,V> parentNode) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.key = key;
            this.value = value;
            this.parentNode = parentNode;
        }
    }
    @Override
    public void zrus() {
        rootNode = null;
    }                                                                                               //je

    @Override
    public boolean jePrazdny() {
        return rootNode == null;
    }                                                                                       //je

    @Override
    public V najdi(K key) throws TableException {
        Node<K,V> actual = rootNode;
        while(actual!=null){
            int comp = key.compareTo(actual.key);
            if(comp > 0) actual = actual.rightChild;
            else if(comp < 0) actual = actual.leftChild;
            else {
                return actual.value;
            }
        }
        throw new TableException("Pervek nevyhledan");
    }                                                                      //je

    @Override
    public void vloz(K key, V value) throws TableException {
        if(jePrazdny()){
            rootNode = new Node<>(null, null, key, value,null);
        }
        else {
            Node<K,V> currentNode = rootNode;
            while(true){
                int comp = key.compareTo(currentNode.key);
                if(comp > 0) {
                    if(currentNode.rightChild==null){
                        currentNode.rightChild = new Node<>(null, null, key, value,currentNode);
                        return;
                    }
                    else currentNode = currentNode.rightChild;
                }
                else if(comp < 0) {
                    if(currentNode.leftChild == null){
                        currentNode.leftChild = new Node<>(null, null, key, value,currentNode);
                        return;
                    }
                    else currentNode = currentNode.leftChild;
                }
                else {
                    System.out.println(currentNode.key+"EROR");
                    throw new TableException("Takovy KEY uz existuje");
                }
            }
        }
    }

    @Override
    public V odeber(K key) throws TableException {
        Node<K,V> currentNode = rootNode;
        boolean itLeftChild = true;
        while(currentNode!=null){
            int comp = key.compareTo(currentNode.key);
            if(comp > 0){
                itLeftChild = false;
                currentNode = currentNode.rightChild;
            }
            else if(comp < 0) {
                itLeftChild = true;
                currentNode = currentNode.leftChild;
            }

            else {
                V vratit = currentNode.value;
                if(currentNode.leftChild == null && currentNode.rightChild == null){                                      //Нет детей
                    if(currentNode == rootNode){
                        rootNode = null;
                    }
                    if(!itLeftChild){
                        currentNode.parentNode.rightChild = null;
                    }
                    else currentNode.parentNode.leftChild = null;
                }

                else if(currentNode.rightChild ==null) {                                                                  // Нет правого ребенка
                    if(!itLeftChild){
                        currentNode.parentNode.rightChild = currentNode.leftChild;
                    }
                    else {
                        currentNode.parentNode.leftChild = currentNode.leftChild;
                    }
                    currentNode.leftChild.parentNode =currentNode.parentNode;

                }
                else if(currentNode.leftChild ==null) {                                                                     //нет левого ребенка
                    if(!itLeftChild){
                        currentNode.parentNode.rightChild = currentNode.rightChild;
                    }
                    else {
                        currentNode.parentNode.leftChild = currentNode.rightChild;
                    }
                    currentNode.rightChild.parentNode =currentNode.parentNode;
                }

                else{
                    if(currentNode.parentNode.leftChild ==currentNode){
                        currentNode.parentNode.leftChild = currentNode.rightChild;
                    }
                    else {
                        currentNode.parentNode.rightChild = currentNode.rightChild;
                    }
                    Node<K,V> ostatni = currentNode.leftChild;
                    currentNode = currentNode.rightChild;
                    while(currentNode.leftChild !=null){
                        currentNode = currentNode.leftChild;
                    }
                    currentNode.leftChild = ostatni;
                }
                return vratit;
            }
        }
        throw new TableException("Pervek nevyhledan");
    }

    @Override
    public Iterator<V> vytvorIterator(eTypProhl e) throws TableException {
        if(jePrazdny()) throw new TableException("Strom je prazdny");
        switch (e){
            case HLOUBKA -> {
                return hloubka();
            }
            case SIRKA -> {
                IAbstrFifoALifo<V> fronta = new AbstFifo<>();
                IAbstrFifoALifo<Node<K,V>> lifo = new AbstFifo<>();
                lifo.vloz(rootNode);
                while (!lifo.jePrazdny()){
                    Node<K,V> currentNode = lifo.odeber();
                    fronta.vloz(currentNode.value);
                    if(currentNode.leftChild !=null){
                        lifo.vloz(currentNode.leftChild);
                    }
                    if(currentNode.rightChild !=null){
                        lifo.vloz(currentNode.rightChild);
                    }
                }
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return !fronta.jePrazdny();
                    }

                    @Override
                    public V next() {
                        return fronta.odeber();
                    }
                };
            }
        }
        return null;
    }

    private Iterator<V> hloubka(){
        IAbstrFifoALifo<Node<K,V>> zasobnik = new AbstrLifo<>();
        zasobnik.vloz(rootNode);

        return new Iterator<>() {
            Node<K,V> element = rootNode;
            @Override
            public boolean hasNext() {
                return !zasobnik.jePrazdny();
            }

            @Override
            public V next() {
               if(!hasNext()) {
                   throw new NoSuchElementException();
               }
                while (element != null && element.leftChild != null) {
                    zasobnik.vloz(element.leftChild);
                    element = element.leftChild;
                }

                Node<K,V> node = zasobnik.odeber();

                if (node.rightChild != null) {
                    zasobnik.vloz(node.rightChild);
                    element = node.rightChild;
                }

                return node.value;
            }
        };
    }
    @Override
    public V najdiNejblizsi(K key) {
        Node<K,V> currentNode = rootNode;
        int nejMensi = Integer.MAX_VALUE;
        V nejMen = null;
        while(currentNode != null){
            int comp = key.compareTo(currentNode.key);
            int cislo = (int) Math.sqrt(Math.abs(comp*comp - nejMensi*nejMensi));
            if(cislo<nejMensi) {
                nejMensi = cislo;
                nejMen = currentNode.value;
            }
            if(comp > 0) currentNode = currentNode.rightChild;
            else if(comp < 0) currentNode = currentNode.leftChild;
            else {
                return currentNode.value;
            }
        }
        return nejMen;
    }

    private IAbstrFifoALifo<V> zasobnik(){
        IAbstrFifoALifo<V> zasobnik = new AbstrLifo<>();
        deep(rootNode,zasobnik);
        return zasobnik;
    }
    private void deep(Node<K,V> node, IAbstrFifoALifo<V> zasobnik){
        if(node!=null){
            deep(node.leftChild,zasobnik);
            zasobnik.vloz(node.value);
            deep(node.rightChild,zasobnik);
        }
    }
}
