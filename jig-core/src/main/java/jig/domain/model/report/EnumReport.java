package jig.domain.model.report;

import jig.domain.model.angle.EnumAngle;
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

public class EnumReport {

    private enum Items {
        クラス名(Row::クラス名),
        クラス和名(Row::クラス和名),
        定数宣言(Row::定数宣言),
        フィールド(Row::フィールド),
        使用箇所(Row::使用箇所),
        パラメーター有り(Row::パラメーター有り),
        振る舞い有り(Row::振る舞い有り),
        多態(Row::多態);

        Function<Row, String> func;

        Items(Function<Row, String> func) {
            this.func = func;
        }
    }

    private final List<Row> list;

    public EnumReport(List<Row> list) {
        this.list = list;
    }

    public Report toReport() {
        List<ItemRowConverter<Row>> rowConverters =
                Arrays.stream(Items.values())
                        .map(item -> new ItemRowConverter<>(item, item.func))
                        .collect(Collectors.toList());
        return new ReportImpl<>("ENUM", rowConverters, list);
    }

    public static class Row {
        EnumAngle enumAngle;
        JapaneseName japaneseName;
        TypeIdentifierFormatter identifierFormatter;

        public Row(EnumAngle enumAngle, JapaneseName japaneseName, TypeIdentifierFormatter identifierFormatter) {
            this.enumAngle = enumAngle;
            this.japaneseName = japaneseName;
            this.identifierFormatter = identifierFormatter;
        }

        String クラス名() {
            return enumAngle.typeIdentifier().format(identifierFormatter);
        }

        String クラス和名() {
            return japaneseName.summarySentence();
        }

        public String 定数宣言() {
            return enumAngle.constantsDeclarations().toNameText();
        }

        public String フィールド() {
            return enumAngle.fieldDeclarations().toSignatureText();
        }

        public String 使用箇所() {
            return enumAngle.userTypeIdentifiers().asSimpleText();
        }

        public String パラメーター有り() {
            return enumAngle.characteristics().has(Characteristic.ENUM_PARAMETERIZED).toSymbolText();
        }

        public String 振る舞い有り() {
            return enumAngle.characteristics().has(Characteristic.ENUM_BEHAVIOUR).toSymbolText();
        }

        public String 多態() {
            return enumAngle.characteristics().has(Characteristic.ENUM_POLYMORPHISM).toSymbolText();
        }
    }
}