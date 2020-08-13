package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] storage;
    private int currentCapacity;
    private int size;

    public MyHashMap() {
        storage = new Node[INITIAL_CAPACITY];
        currentCapacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (suchElementExists(key, value)) {
            return;
        }
        int index = hashIndex(key);
        if (storage[index] != null) {
            Node<K, V> node = storage[index];
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }

            if (!Objects.equals(node.key, key)) {
                while (node.next != null) {
                    if (Objects.equals(node.key, key)) {
                        node.value = value;
                        return;
                    }
                    node = node.next;
                }
                linkLast(node, key, value);
            }

        } else {
            storage[index] = new Node<>(key, value);
        }
        size++;
        capacityCheck();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = storage[hashIndex(key)];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % currentCapacity) : 0;
    }

    private void capacityCheck() {
        if (size > (storage.length * LOAD_FACTOR)) {
            size = 0;
            currentCapacity *= 2;
            Node<K, V>[] tempStorage = storage;
            storage = new Node[currentCapacity];
            for (int i = 0; i < tempStorage.length; i++) {
                while (tempStorage[i] != null) {
                    put(tempStorage[i].key, tempStorage[i].value);
                    tempStorage[i] = tempStorage[i].next;
                }
            }
        }
    }

    private boolean suchElementExists(K key, V value) {
        if (size == 0) {
            return false;
        }

        for (Node<K, V> node : storage) {
            if (node != null) {
                Node<K, V> newNode = new Node<>(key, value);
                if (Objects.equals(node, newNode)) {
                    return true;
                }
                if (node.next != null) {
                    Node<K, V> collisionNode = node;
                    while (collisionNode.next != null) {
                        if (Objects.equals(collisionNode, newNode)) {
                            return true;
                        }
                        collisionNode = collisionNode.next;
                    }
                }
            }
        }

        return false;
    }

    private void linkLast (Node<K, V> node, K key, V value) {
        node.next = new Node<>(key, value);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
