package org.dddjava.jig.domain.model.jigsource.jigreader;

import org.dddjava.jig.domain.model.jigsource.file.text.javacode.JavaSources;
import org.dddjava.jig.domain.model.jigsource.file.text.javacode.PackageInfoSources;
import org.dddjava.jig.domain.model.jigsource.file.text.kotlincode.KotlinSources;
import org.dddjava.jig.domain.model.jigsource.file.text.scalacode.ScalaSources;
import org.dddjava.jig.domain.model.parts.package_.PackageComments;
import org.dddjava.jig.domain.model.parts.class_.type.ClassComments;

/**
 * コードを使用する別名別名読み取り機
 */
public class SourceCodeAliasReader {

    JavaSourceAliasReader javaSourceAliasReader;
    KotlinSourceAliasReader kotlinSourceAliasReader;
    ScalaSourceAliasReader scalaSourceAliasReader;

    public SourceCodeAliasReader(JavaSourceAliasReader javaSourceAliasReader) {
        this(javaSourceAliasReader, sources -> ClassComments.empty(), sources -> ClassComments.empty());
    }

    public SourceCodeAliasReader(JavaSourceAliasReader javaSourceAliasReader, KotlinSourceAliasReader kotlinSourceAliasReader) {
        this(javaSourceAliasReader, kotlinSourceAliasReader, sources -> ClassComments.empty());
    }

    public SourceCodeAliasReader(JavaSourceAliasReader javaSourceAliasReader, ScalaSourceAliasReader scalaSourceAliasReader) {
        this(javaSourceAliasReader, sources -> ClassComments.empty(), scalaSourceAliasReader);
    }

    private SourceCodeAliasReader(JavaSourceAliasReader javaSourceAliasReader, KotlinSourceAliasReader kotlinSourceAliasReader, ScalaSourceAliasReader scalaSourceAliasReader) {
        this.javaSourceAliasReader = javaSourceAliasReader;
        this.kotlinSourceAliasReader = kotlinSourceAliasReader;
        this.scalaSourceAliasReader = scalaSourceAliasReader;
    }

    public PackageComments readPackages(PackageInfoSources packageInfoSources) {
        return javaSourceAliasReader.readPackages(packageInfoSources);
    }

    public ClassComments readJavaSources(JavaSources javaSources) {
        return javaSourceAliasReader.readAlias(javaSources);
    }

    public ClassComments readKotlinSources(KotlinSources kotlinSources) {
        return kotlinSourceAliasReader.readAlias(kotlinSources);
    }

    public ClassComments readScalaSources(ScalaSources scalaSources) {
        return scalaSourceAliasReader.readAlias(scalaSources);
    }
}
