package org.dddjava.jig.domain.model.categories;

import org.dddjava.jig.domain.model.declaration.field.FieldDeclarations;
import org.dddjava.jig.domain.model.declaration.field.StaticFieldDeclarations;
import org.dddjava.jig.domain.model.declaration.type.TypeIdentifiers;
import org.dddjava.jig.domain.model.networks.type.TypeDependencies;

import java.util.ArrayList;
import java.util.List;

/**
 * 区分の切り口一覧
 */
public class CategoryAngles {

    List<CategoryAngle> list;

    public CategoryAngles(CategoryTypes categoryTypes, TypeDependencies typeDependencies, FieldDeclarations fieldDeclarations, StaticFieldDeclarations staticFieldDeclarations) {
        List<CategoryAngle> list = new ArrayList<>();
        for (CategoryType categoryType : categoryTypes.list()) {
            list.add(new CategoryAngle(categoryType, typeDependencies, fieldDeclarations, staticFieldDeclarations));
        }
        this.list = list;
    }

    public List<CategoryAngle> list() {
        return list;
    }

    public TypeIdentifiers typeIdentifiers() {
        return list.stream().map(CategoryAngle::typeIdentifier).collect(TypeIdentifiers.collector());
    }
}
