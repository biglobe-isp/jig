package jig.domain.model.report;

import jig.domain.model.angle.GenericModelAngle;
import jig.domain.model.characteristic.Characteristic;
import jig.domain.model.identifier.type.TypeIdentifierFormatter;
import jig.domain.model.japanese.JapaneseName;
import jig.domain.model.report.template.ItemRowConverter;
import jig.domain.model.report.template.Report;
import jig.domain.model.report.template.ReportImpl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenericModelReport {

    private enum Items {
        クラス名(Row::クラス名),
        クラス和名(Row::クラス和名),
        使用箇所(Row::使用箇所);

        Function<Row, String> func;

        Items(Function<Row, String> func) {
            this.func = func;
        }
    }

    private final Characteristic characteristic;
    private final List<Row> list;

    public GenericModelReport(Characteristic characteristic, List<Row> list) {
        this.characteristic = characteristic;
        this.list = list;
    }

    public Report toReport() {
        List<ItemRowConverter<Row>> rowConverters =
                Arrays.stream(Items.values())
                        .map(item -> new ItemRowConverter<>(item, item.func))
                        .collect(Collectors.toList());
        return new ReportImpl<>(characteristic.name(), rowConverters, list);
    }

    public static class Row {
        GenericModelAngle genericModelAngle;
        JapaneseName japaneseName;
        TypeIdentifierFormatter identifierFormatter;

        public Row(GenericModelAngle genericModelAngle, JapaneseName japaneseName, TypeIdentifierFormatter identifierFormatter) {
            this.genericModelAngle = genericModelAngle;
            this.japaneseName = japaneseName;
            this.identifierFormatter = identifierFormatter;
        }

        String クラス名() {
            return genericModelAngle.typeIdentifier().format(identifierFormatter);
        }

        String クラス和名() {
            return japaneseName.summarySentence();
        }

        public String 使用箇所() {
            return genericModelAngle.userTypeIdentifiers().asSimpleText();
        }
    }
}