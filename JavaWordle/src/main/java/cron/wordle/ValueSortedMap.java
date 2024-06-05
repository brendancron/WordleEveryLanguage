package cron.wordle;

import java.util.*;

public class ValueSortedMap<K, V extends Comparable<V>> extends AbstractMap<K, V> {

    private final ArrayList<Map.Entry<K, V>> sortedList;

    public ValueSortedMap() {
        sortedList = new ArrayList<>();
    }

    @Override
    public V put(K key, V value) {
        Map.Entry<K, V> newEntry = new AbstractMap.SimpleEntry<>(key, value);
        // Use a comparator that reverses the natural order
        Comparator<Map.Entry<K, V>> comparator = Map.Entry.<K, V>comparingByValue().reversed();
        int index = Collections.binarySearch(sortedList, newEntry, comparator);
        V oldValue = null;
        if (index < 0) {
            index = -index - 1; // Convert negative index to insertion point
        } else {
            // If the key already exists, remove the old entry before inserting the new one
            for (int i = index; i < sortedList.size() && comparator.compare(sortedList.get(i), newEntry) == 0; i++) {
                if (sortedList.get(i).getKey().equals(key)) {
                    oldValue = sortedList.get(i).getValue();
                    sortedList.remove(i);
                    break;
                }
            }
        }
        sortedList.add(index, newEntry);
        return oldValue;
    }

    @Override
    public V get(Object key) {
        for (Map.Entry<K, V> entry : sortedList) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public K getByIndex(int i) {
        return sortedList.get(i).getKey();
    }

    @Override
    public V remove(Object key) {
        Iterator<Map.Entry<K, V>> iterator = sortedList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            if (entry.getKey().equals(key)) {
                iterator.remove();
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void clear() {
        sortedList.clear();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return sortedList.iterator();
            }

            @Override
            public int size() {
                return sortedList.size();
            }
        };
    }

    @Override
    public String toString() {
        return sortedList.toString();
    }
}
