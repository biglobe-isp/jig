package org.dddjava.jig.application.service;

import org.dddjava.jig.domain.model.businessrules.BusinessRuleNetwork;
import org.dddjava.jig.domain.model.businessrules.BusinessRules;
import org.dddjava.jig.domain.model.implementation.analyzed.AnalyzedImplementation;
import org.dddjava.jig.domain.model.implementation.analyzed.declaration.namespace.AllPackageIdentifiers;
import org.dddjava.jig.domain.model.implementation.analyzed.declaration.namespace.PackageIdentifiers;
import org.dddjava.jig.domain.model.implementation.analyzed.networks.packages.PackageNetwork;
import org.dddjava.jig.domain.model.implementation.analyzed.networks.packages.PackageRelations;
import org.dddjava.jig.domain.model.implementation.analyzed.networks.type.TypeRelations;
import org.dddjava.jig.domain.model.notice.Warning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 依存関係サービス
 */
@Service
public class DependencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyService.class);

    BusinessRuleService businessRuleService;

    public DependencyService(BusinessRuleService businessRuleService) {
        this.businessRuleService = businessRuleService;
    }

    public AllPackageIdentifiers allPackageIdentifiers(AnalyzedImplementation analyzedImplementation) {
        PackageIdentifiers packageIdentifiers = analyzedImplementation.typeByteCodes().types().packages();
        return packageIdentifiers.allPackageIdentifiers();
    }

    /**
     * パッケージ依存を取得する
     */
    public PackageNetwork packageDependencies(AnalyzedImplementation analyzedImplementation) {
        BusinessRules businessRules = businessRuleService.businessRules(analyzedImplementation);

        if (businessRules.empty()) {
            LOGGER.warn(Warning.ビジネスルールが見つからないので出力されない通知.text());
            return new PackageNetwork(new PackageIdentifiers(Collections.emptyList()), new PackageRelations(Collections.emptyList()));
        }

        PackageRelations packageRelations = new TypeRelations(analyzedImplementation.typeByteCodes()).packageDependencies();

        return new PackageNetwork(businessRules.identifiers().packageIdentifiers(), packageRelations);
    }

    public BusinessRuleNetwork businessRuleNetwork(AnalyzedImplementation analyzedImplementation) {
        BusinessRuleNetwork businessRuleNetwork = new BusinessRuleNetwork(
                businessRuleService.businessRules(analyzedImplementation),
                new TypeRelations(analyzedImplementation.typeByteCodes()));
        return businessRuleNetwork;
    }
}
