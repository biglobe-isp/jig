package org.dddjava.jig.domain.model.identifier.type;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * 型の識別子の集合
 */
public class TypeIdentifiers {

    List<TypeIdentifier> identifiers;

    public TypeIdentifiers(List<TypeIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public List<TypeIdentifier> list() {
        ArrayList<TypeIdentifier> list = new ArrayList<>(this.identifiers);
        list.sort(Comparator.comparing(TypeIdentifier::fullQualifiedName));
        return list;
    }

    public Set<TypeIdentifier> set() {
        return new HashSet<>(identifiers);
    }

    public static Collector<TypeIdentifier, ?, TypeIdentifiers> collector() {
        return Collectors.collectingAndThen(Collectors.toList(), TypeIdentifiers::new);
    }

    public String asText() {
        return identifiers.stream().map(TypeIdentifier::fullQualifiedName).distinct().collect(joining(", ", "[", "]"));
    }

    public String asSimpleText() {
        return identifiers.stream().map(TypeIdentifier::asSimpleText).distinct().collect(joining(", ", "[", "]"));
    }
}
