public interface MyMap<K, V> {

    V put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean isEmpty();

    int size();

    void clear();
}
