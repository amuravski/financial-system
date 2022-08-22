package com.solvd.financialsystem.utils;

import java.util.Comparator;
import java.util.Map;

public enum SortOrder {

    DESCENDING {
        @Override
        public <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> getComparator() {
            return (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        }
    },
    ASCENDING {
        @Override
        public <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> getComparator() {
            return Map.Entry.comparingByValue();
        }
    };

    public abstract <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> getComparator();
}
