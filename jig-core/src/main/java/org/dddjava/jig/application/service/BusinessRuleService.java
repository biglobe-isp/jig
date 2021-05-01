package org.dddjava.jig.application.service;

import org.dddjava.jig.application.repository.JigSourceRepository;
import org.dddjava.jig.domain.model.jigdocument.implementation.CategoryUsageDiagram;
import org.dddjava.jig.domain.model.jigdocument.implementation.MethodSmellList;
import org.dddjava.jig.domain.model.jigdocument.specification.Categories;
import org.dddjava.jig.domain.model.jigmodel.domains.businessrules.BusinessRules;
import org.dddjava.jig.domain.model.jigmodel.domains.categories.CategoryTypes;
import org.dddjava.jig.domain.model.jigmodel.domains.collections.JigCollectionTypes;
import org.dddjava.jig.domain.model.jigmodel.applications.ServiceMethods;
import org.dddjava.jig.domain.model.jigsource.jigfactory.Architecture;
import org.dddjava.jig.domain.model.jigsource.jigfactory.TypeFacts;
import org.dddjava.jig.domain.model.parts.annotation.ValidationAnnotatedMembers;
import org.dddjava.jig.domain.model.parts.relation.method.MethodRelations;
import org.springframework.stereotype.Service;

/**
 * ビジネスルールの分析サービス
 */
@Service
public class BusinessRuleService {

    final Architecture architecture;
    final JigSourceRepository jigSourceRepository;

    public BusinessRuleService(Architecture architecture, JigSourceRepository jigSourceRepository) {
        this.architecture = architecture;
        this.jigSourceRepository = jigSourceRepository;
    }

    /**
     * ビジネスルール一覧を取得する
     */
    public BusinessRules businessRules() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        return typeFacts.toBusinessRules(architecture);
    }

    /**
     * メソッドの不吉なにおい一覧を取得する
     */
    public MethodSmellList methodSmells() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        MethodRelations methodRelations = typeFacts.toMethodRelations();
        return new MethodSmellList(businessRules(), methodRelations);
    }

    /**
     * 区分一覧を取得する
     */
    public Categories categories() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();

        return Categories.create(CategoryTypes.from(businessRules().jigTypes()), typeFacts.toClassRelations());
    }

    /**
     * コレクションを分析する
     */
    public JigCollectionTypes collections() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();

        return new JigCollectionTypes(businessRules().jigTypes(), typeFacts.toClassRelations());
    }

    /**
     * 区分使用図
     */
    public CategoryUsageDiagram categoryUsages() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        ServiceMethods serviceMethods = new ServiceMethods(typeFacts.applicationMethodsOf(architecture));

        return new CategoryUsageDiagram(serviceMethods, typeFacts.toBusinessRules(architecture));
    }

    public ValidationAnnotatedMembers validationAnnotatedMembers() {
        TypeFacts typeFacts = jigSourceRepository.allTypeFacts();
        return typeFacts.validationAnnotatedMembers();
    }
}
