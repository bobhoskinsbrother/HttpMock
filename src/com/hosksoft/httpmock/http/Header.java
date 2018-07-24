package com.hosksoft.httpmock.http;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Header {

    private final String name;
    private final List<String> value;

    public Header(String name, List<String> value) {
        this.name = name;
        if(value.size()==0) {
            throw new IllegalArgumentException("Need to supply at least one header value");
        }
        this.value = value;
    }

    public Header(String name, String... value) {
        this(name, Arrays.asList(value));
    }

    public static Header header(String name, String... value) {
        return new Header(name, value);
    }

    public static Header header(String name, List<String> value) {
        return new Header(name, value);
    }

    public boolean equals(Header other) {
        return other.name != null && other.value != null && other.name.equals(this.name) && (
                new HashSet<>(this.value).equals(new HashSet<>(other.value))
        );
    }

    public String name() {
        return name;
    }

    public String value() {
        return printValues(value);
    }

    @Override
    public String toString() {
        return printList(name, value);
    }

    private String printValues(List<String> value) {
        final Iterator<String> iterator = value.iterator();
        StringBuilder b = new StringBuilder();
        while (iterator.hasNext()) {
            b.append(iterator.next());
            if(iterator.hasNext()) {
                b.append(",");
            }
        }
        return b.toString();
    }

    private String printList(String name, List<String> values) {
        StringBuilder b = new StringBuilder();
        final Iterator<String> valuesIterator = values.iterator();
        while (valuesIterator.hasNext()) {
            b.append(name).append(":").append(valuesIterator.next());
            if(valuesIterator.hasNext()) {
                b.append(", ");
            }
        }
        return b.toString();
    }
}
