package org.dddjava.jig.domain.model.jigmodel.jigtype.member;

import org.dddjava.jig.domain.model.parts.class_.method.MethodComment;
import org.dddjava.jig.domain.model.parts.annotation.MethodAnnotations;
import org.dddjava.jig.domain.model.parts.class_.method.DecisionNumber;
import org.dddjava.jig.domain.model.parts.class_.method.MethodDeclaration;
import org.dddjava.jig.domain.model.parts.class_.method.MethodDerivation;
import org.dddjava.jig.domain.model.parts.class_.method.Visibility;
import org.dddjava.jig.domain.model.parts.relation.method.MethodDepend;
import org.dddjava.jig.domain.model.parts.relation.method.UsingFields;
import org.dddjava.jig.domain.model.parts.relation.method.UsingMethods;
import org.dddjava.jig.domain.model.parts.class_.type.TypeIdentifier;
import org.dddjava.jig.domain.model.parts.class_.type.TypeIdentifiers;

import java.util.List;

/**
 * メソッド
 */
public class JigMethod {

    MethodDeclaration methodDeclaration;
    MethodComment methodComment;

    boolean nullDecision;

    DecisionNumber decisionNumber;
    MethodAnnotations methodAnnotations;
    Visibility visibility;

    MethodDepend methodDepend;
    MethodDerivation methodDerivation;

    public JigMethod(MethodDeclaration methodDeclaration, MethodComment methodComment, boolean nullDecision, DecisionNumber decisionNumber, MethodAnnotations methodAnnotations, Visibility visibility, MethodDepend methodDepend, MethodDerivation methodDerivation) {
        this.methodDeclaration = methodDeclaration;
        this.methodComment = methodComment;
        this.nullDecision = nullDecision;
        this.decisionNumber = decisionNumber;
        this.methodAnnotations = methodAnnotations;
        this.visibility = visibility;
        this.methodDepend = methodDepend;
        this.methodDerivation = methodDerivation;
    }

    public MethodDeclaration declaration() {
        return methodDeclaration;
    }

    public DecisionNumber decisionNumber() {
        return decisionNumber;
    }

    public MethodAnnotations methodAnnotations() {
        return methodAnnotations;
    }

    public Visibility visibility() {
        return visibility;
    }

    public boolean isPublic() {
        return visibility.isPublic();
    }

    public UsingFields usingFields() {
        return methodDepend.usingFields();
    }

    public UsingMethods usingMethods() {
        return methodDepend.usingMethods();
    }

    public MethodWorries methodWorries() {
        return new MethodWorries(this);
    }

    public boolean conditionalNull() {
        return nullDecision;
    }

    public boolean referenceNull() {
        return methodDepend.hasNullReference();
    }

    public boolean notUseMember() {
        return methodDepend.notUseMember();
    }

    public TypeIdentifiers usingTypes() {
        return methodDepend.usingTypes();
    }

    public String aliasTextOrBlank() {
        return methodComment.asText();
    }

    public String aliasText() {
        return methodComment
                .asTextOrDefault(declaration().declaringType().asSimpleText() + "\\n"
                        + declaration().methodSignature().methodName());
    }

    public JigMethodDescription description() {
        return JigMethodDescription.from(methodComment.documentationComment());
    }

    public String labelTextWithSymbol() {
        String name = methodComment.asTextOrDefault(declaration().methodSignature().methodName());
        return visibility.symbol() + ' ' + name;
    }

    public List<TypeIdentifier> listArguments() {
        return declaration().methodSignature().arguments();
    }

    public MethodDerivation derivation() {
        return methodDerivation;
    }

    public boolean objectMethod() {
        return declaration().methodSignature().isObjectMethod();
    }

    public boolean documented() {
        return methodComment.exists();
    }

    public boolean remarkable() {
        return visibility == Visibility.PUBLIC || documented();
    }
}
