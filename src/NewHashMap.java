public class NewHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final float loadFactor;

    private Node<K, V>[] bucket;
    private int size;
    private int threshold;

    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }


    public NewHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public NewHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public NewHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Неверный размер: " + initialCapacity);
        }
        if (loadFactor <= 0.0) {
            throw new IllegalArgumentException("Неверный коэффицент загрузки: " + loadFactor);
        }

        this.loadFactor = loadFactor;
        this.bucket = (Node<K, V>[]) new Node[initialCapacity];
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    @Override
    public V put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);

        if (bucket[index] == null) {
            bucket[index] = newNode;
            size++;

            return null;
        }

        Node<K, V> current = bucket[index];
        Node<K, V> prev = null;
        while (current != null) {
            if (keysEqual(current.key, key)) {
                V oldValue = current.value;
                current.value = value;

                return oldValue;
            }
            prev = current;
            current = current.next;
        }
        prev.next = newNode;
        size++;

        return null;
    }

    @Override
    public V get(K key) {
        int index = getIndex(key);
        Node<K, V> current = bucket[index];

        while (current != null) {
            if (keysEqual(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = bucket[index];
        Node<K, V> prev = null;

        while (current != null) {
            if (keysEqual(current.key, key)) {
                V removedValue = current.value;
                if (prev == null) {
                    bucket[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return removedValue;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public void clear() {
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = bucket.length * 2;
        Node<K, V>[] newBucket = (Node<K, V>[]) new Node[newCapacity];

        //Перехеш
        for (Node<K, V> head : bucket) {
            Node<K, V> current = head;
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = Math.abs(current.key.hashCode()) % newCapacity;
                current.next = newBucket[newIndex];
                newBucket[newIndex] = current;
                current = next;
            }
        }
        bucket = newBucket;
        threshold = (int) (newCapacity * loadFactor);
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0; // null ключ будет в бакете 0
        }
        return Math.abs(key.hashCode()) % bucket.length;
    }

    private boolean keysEqual(K key1, K key2) {
        if (key1 == null && key2 == null) return true;
        if (key1 == null || key2 == null) return false;

        return key1.equals(key2);
    }
}
