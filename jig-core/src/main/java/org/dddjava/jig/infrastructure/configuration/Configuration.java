package org.dddjava.jig.infrastructure.configuration;

import org.dddjava.jig.application.repository.JigSourceRepository;
import org.dddjava.jig.application.service.*;
import org.dddjava.jig.domain.model.jigdocument.stationery.JigDocumentContext;
import org.dddjava.jig.domain.model.jigmodel.applications.MethodNodeLabelStyle;
import org.dddjava.jig.domain.model.jigsource.jigfactory.Architecture;
import org.dddjava.jig.domain.model.jigsource.jigreader.SourceCodeAliasReader;
import org.dddjava.jig.domain.model.parts.alias.AliasFinder;
import org.dddjava.jig.domain.model.jigsource.jigreader.CommentRepository;
import org.dddjava.jig.domain.model.parts.package_.PackageComment;
import org.dddjava.jig.domain.model.parts.class_.type.ClassComment;
import org.dddjava.jig.domain.model.parts.package_.PackageIdentifier;
import org.dddjava.jig.domain.model.parts.class_.type.TypeIdentifier;
import org.dddjava.jig.infrastructure.PrefixRemoveIdentifierFormatter;
import org.dddjava.jig.infrastructure.asm.AsmFactReader;
import org.dddjava.jig.infrastructure.filesystem.LocalFileSourceReader;
import org.dddjava.jig.infrastructure.logger.MessageLogger;
import org.dddjava.jig.infrastructure.mybatis.MyBatisSqlReader;
import org.dddjava.jig.infrastructure.onmemoryrepository.OnMemoryCommentRepository;
import org.dddjava.jig.infrastructure.onmemoryrepository.OnMemoryJigSourceRepository;
import org.dddjava.jig.presentation.controller.ApplicationListController;
import org.dddjava.jig.presentation.controller.BusinessRuleListController;
import org.dddjava.jig.presentation.controller.DiagramController;
import org.dddjava.jig.presentation.view.ResourceBundleJigDocumentContext;
import org.dddjava.jig.presentation.view.ViewResolver;
import org.dddjava.jig.presentation.view.handler.JigDocumentHandlers;

import java.nio.file.Path;

public class Configuration {
    JigProperties properties;

    JigSourceReadService jigSourceReadService;
    JigDocumentHandlers documentHandlers;
    ApplicationService applicationService;
    DependencyService dependencyService;
    BusinessRuleService businessRuleService;
    AliasService aliasService;

    public Configuration(JigProperties originalProperties, SourceCodeAliasReader sourceCodeAliasReader) {
        this.properties = new JigPropertyLoader(originalProperties).load();
        properties.prepareOutputDirectory();

        // AliasFinderが無くなったらなくせる
        CommentRepository commentRepository = new OnMemoryCommentRepository();

        JigSourceRepository jigSourceRepository = new OnMemoryJigSourceRepository(commentRepository);
        this.aliasService = new AliasService(commentRepository);

        Architecture architecture = new PropertyArchitectureFactory(properties).architecture();

        this.businessRuleService = new BusinessRuleService(architecture, jigSourceRepository);
        this.dependencyService = new DependencyService(businessRuleService, new MessageLogger(DependencyService.class), jigSourceRepository);
        this.applicationService = new ApplicationService(architecture, new MessageLogger(ApplicationService.class), jigSourceRepository);
        PrefixRemoveIdentifierFormatter prefixRemoveIdentifierFormatter = new PrefixRemoveIdentifierFormatter(
                properties.getOutputOmitPrefix()
        );

        // TypeやMethodにAliasを持たせて無くす
        AliasFinder aliasFinder = new AliasFinder() {
            @Override
            public PackageComment find(PackageIdentifier packageIdentifier) {
                return commentRepository.get(packageIdentifier);
            }

            @Override
            public ClassComment find(TypeIdentifier typeIdentifier) {
                return commentRepository.get(typeIdentifier);
            }
        };

        JigDocumentContext jigDocumentContext = ResourceBundleJigDocumentContext
                .getInstanceWithAliasFinder(aliasFinder, properties.linkPrefix());
        ViewResolver viewResolver = new ViewResolver(
                prefixRemoveIdentifierFormatter,
                // TODO MethodNodeLabelStyleとDiagramFormatをプロパティで受け取れるようにする
                // @Value("${methodNodeLabelStyle:SIMPLE}") String methodNodeLabelStyle
                // @Value("${diagram.format:SVG}") String diagramFormat
                MethodNodeLabelStyle.SIMPLE,
                properties.outputDiagramFormat,
                jigDocumentContext,
                aliasService
        );
        BusinessRuleListController businessRuleListController = new BusinessRuleListController(
                applicationService,
                businessRuleService
        );
        ApplicationListController applicationListController = new ApplicationListController(
                applicationService,
                businessRuleService
        );
        DiagramController diagramController = new DiagramController(
                dependencyService,
                businessRuleService,
                applicationService
        );
        this.jigSourceReadService = new JigSourceReadService(
                jigSourceRepository,
                new AsmFactReader(),
                sourceCodeAliasReader,
                new MyBatisSqlReader(),
                new LocalFileSourceReader()
        );
        this.documentHandlers = new JigDocumentHandlers(
                viewResolver,
                businessRuleListController,
                applicationListController,
                diagramController
        );
    }

    public JigSourceReadService implementationService() {
        return jigSourceReadService;
    }

    public JigDocumentHandlers documentHandlers() {
        return documentHandlers;
    }

    public Path outputDirectory() {
        return properties.outputDirectory;
    }
}
