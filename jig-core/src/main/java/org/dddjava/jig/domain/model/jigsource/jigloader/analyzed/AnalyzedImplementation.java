package org.dddjava.jig.domain.model.jigsource.jigloader.analyzed;

import org.dddjava.jig.domain.model.jigloaded.datasource.Sqls;
import org.dddjava.jig.domain.model.jigsource.bytecode.TypeByteCodes;
import org.dddjava.jig.domain.model.jigsource.source.Sources;

/**
 * 解析した実装
 */
public interface AnalyzedImplementation {

    static AnalyzedImplementation generate(Sources sources, TypeByteCodes typeByteCodes, Sqls sqls) {
        return new AnalyzedImplementationImpl(sources, typeByteCodes, sqls);
    }

    TypeByteCodes typeByteCodes();

    Sqls sqls();

    AnalyzeStatuses status();
}
