package com.dwarfeng.subgrade.sdk.database.definition;

import java.util.*;

/**
 * MySQL8 Helper。
 *
 * <p>
 * TODO 该类未实现，在此仅作为占位，请勿使用。
 *
 * @author DwArFeng
 * @since 1.1.1
 */
@SuppressWarnings("all") // TODO 抑制所有警告，在该类实现后删除该注解。
public class MySQL8Helper {

    private static final String CUSTOM_COLUMN_PROPERTY = "COLUMN_PROPERTY";
    private static final String CUSTOM_DEFAULT = "DEFAULT";
    private static final String CUSTOM_ENGINE = "ENGINE";
    private static final String CUSTOM_COLLATE = "CUSTOM_COLLATE";
    private static final String CUSTOM_CHARACTER_SET = "CUSTOM_CHARACTER_SET";
    private static final String CUSTOM_DEFAULT_CHARSET = "DEFAULT_CHARSET";
    private static final String CUSTOM_COLLATION = "COLLATION";
    private static final String CUSTOM_AUTO_INCREMENT = "AUTO_INCREMENT";
    private static final String NAME_PRIMARY_KEY = "PK";
    private static final String TYPE_INDEX = "INDEX";
    private static final String CUSTOM_INDEX_TYPE = "TYPE_INDEX";
    private static final String CUSTOM_INDEX_ASC = "ASC";
    private static final String CUSTOM_INDEX_DESC = "DESC";
    private static final String CUSTOM_INDEX_LENGTH = "LENGTH";
    private static final String CUSTOM_INDEX_STORAGE = "STORAGE";
    private static final String CUSTOM_INDEX_KEY_BLOCK_SIZE = "KEY_BLOCK_SIZE";
    private static final String CUSTOM_INDEX_PARSER = "PARSER";
    private static final String TYPE_FOREIGN_KEY = "FOREIGN_KEY";
    private static final String CUSTOM_FOREIGN_REF_SCHEMA = "REF_SCHEMA";
    private static final String CUSTOM_FOREIGN_REF_TABLE = "REF_TABLE";
    private static final String CUSTOM_FOREIGN_REF_COLUMNS = "REF_COLUMNS";
    private static final String CUSTOM_FOREIGN_ON_UPDATE = "ON_UPDATE";
    private static final String CUSTOM_FOREIGN_ON_DELETE = "ON_DELETE";

    @SuppressWarnings("SpellCheckingInspection")
    public enum Engine {
        FEDERATED("FEDERATED"),
        MEMORY("MEMORY"),
        INNODB("InnoDB"),
        PERFORMANCE_SCHEMA("PERFORMANCE_SCHEMA"),
        MYISAM("MyISAM"),
        MRG_MYISAM("MRG_MYISAM"),
        BLACKHOLE("BLACKHOLE"),
        CSV("CSV"),
        ARCHIVE("ARCHIVE");

        private final String sqlFragment;

        Engine(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public enum Charset {
        ARMSCII8("armscii8"),
        ASCII("ascii"),
        BIG5("big5"),
        BINARY("binary"),
        CP1250("cp1250"),
        CP1251("cp1251"),
        CP1256("cp1256"),
        CP1257("cp1257"),
        CP850("cp850"),
        CP852("cp852"),
        CP866("cp866"),
        CP932("cp932"),
        DEC8("dec8"),
        EUCJPMS("eucjpms"),
        EUCKR("euckr"),
        GB18030("gb18030"),
        GB2312("gb2312"),
        GBK("gbk"),
        GEOSTD8("geostd8"),
        GREEK("greek"),
        HEBREW("hebrew"),
        HP8("hp8"),
        KEYBCS2("keybcs2"),
        KOI8R("koi8r"),
        KOI8U("koi8u"),
        LATIN1("latin1"),
        LATIN2("latin2"),
        LATIN5("latin5"),
        LATIN7("latin7"),
        MACCE("macce"),
        MACROMAN("macroman"),
        SJIS("sjis"),
        SWE7("swe7"),
        TIS620("tis620"),
        UCS2("ucs2"),
        UJIS("ujis"),
        UTF16("utf16"),
        UTF16LE("utf16le"),
        UTF32("utf32"),
        UTF8("utf8"),
        UTF8MB4("utf8mb4");

        private final String sqlFragment;

        Charset(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public enum Collation {
        ARMSCII8_BIN("armscii8_bin"),
        ARMSCII8_GENERAL_CI("armscii8_general_ci"),
        ASCII_BIN("ascii_bin"),
        ASCII_GENERAL_CI("ascii_general_ci"),
        BIG5_BIN("big5_bin"),
        BIG5_CHINESE_CI("big5_chinese_ci"),
        BINARY("binary"),
        CP1250_BIN("cp1250_bin"),
        CP1250_CROATIAN_CI("cp1250_croatian_ci"),
        CP1250_CZECH_CS("cp1250_czech_cs"),
        CP1250_GENERAL_CI("cp1250_general_ci"),
        CP1250_POLISH_CI("cp1250_polish_ci"),
        CP1251_BIN("cp1251_bin"),
        CP1251_BULGARIAN_CI("cp1251_bulgarian_ci"),
        CP1251_GENERAL_CI("cp1251_general_ci"),
        CP1251_GENERAL_CS("cp1251_general_cs"),
        CP1251_UKRAINIAN_CI("cp1251_ukrainian_ci"),
        CP1256_BIN("cp1256_bin"),
        CP1256_GENERAL_CI("cp1256_general_ci"),
        CP1257_BIN("cp1257_bin"),
        CP1257_GENERAL_CI("cp1257_general_ci"),
        CP1257_LITHUANIAN_CI("cp1257_lithuanian_ci"),
        CP850_BIN("cp850_bin"),
        CP850_GENERAL_CI("cp850_general_ci"),
        CP852_BIN("cp852_bin"),
        CP852_GENERAL_CI("cp852_general_ci"),
        CP866_BIN("cp866_bin"),
        CP866_GENERAL_CI("cp866_general_ci"),
        CP932_BIN("cp932_bin"),
        CP932_JAPANESE_CI("cp932_japanese_ci"),
        DEC8_BIN("dec8_bin"),
        DEC8_SWEDISH_CI("dec8_swedish_ci"),
        EUCJPMS_BIN("eucjpms_bin"),
        EUCJPMS_JAPANESE_CI("eucjpms_japanese_ci"),
        EUCKR_BIN("euckr_bin"),
        EUCKR_KOREAN_CI("euckr_korean_ci"),
        GB18030_BIN("gb18030_bin"),
        GB18030_CHINESE_CI("gb18030_chinese_ci"),
        GB18030_UNICODE_520_CI("gb18030_unicode_520_ci"),
        GB2312_BIN("gb2312_bin"),
        GB2312_CHINESE_CI("gb2312_chinese_ci"),
        GBK_BIN("gbk_bin"),
        GBK_CHINESE_CI("gbk_chinese_ci"),
        GEOSTD8_BIN("geostd8_bin"),
        GEOSTD8_GENERAL_CI("geostd8_general_ci"),
        GREEK_BIN("greek_bin"),
        GREEK_GENERAL_CI("greek_general_ci"),
        HEBREW_BIN("hebrew_bin"),
        HEBREW_GENERAL_CI("hebrew_general_ci"),
        HP8_BIN("hp8_bin"),
        HP8_ENGLISH_CI("hp8_english_ci"),
        KEYBCS2_BIN("keybcs2_bin"),
        KEYBCS2_GENERAL_CI("keybcs2_general_ci"),
        KOI8R_BIN("koi8r_bin"),
        KOI8R_GENERAL_CI("koi8r_general_ci"),
        KOI8U_BIN("koi8u_bin"),
        KOI8U_GENERAL_CI("koi8u_general_ci"),
        LATIN1_BIN("latin1_bin"),
        LATIN1_DANISH_CI("latin1_danish_ci"),
        LATIN1_GENERAL_CI("latin1_general_ci"),
        LATIN1_GENERAL_CS("latin1_general_cs"),
        LATIN1_GERMAN1_CI("latin1_german1_ci"),
        LATIN1_GERMAN2_CI("latin1_german2_ci"),
        LATIN1_SPANISH_CI("latin1_spanish_ci"),
        LATIN1_SWEDISH_CI("latin1_swedish_ci"),
        LATIN2_BIN("latin2_bin"),
        LATIN2_CROATIAN_CI("latin2_croatian_ci"),
        LATIN2_CZECH_CS("latin2_czech_cs"),
        LATIN2_GENERAL_CI("latin2_general_ci"),
        LATIN2_HUNGARIAN_CI("latin2_hungarian_ci"),
        LATIN5_BIN("latin5_bin"),
        LATIN5_TURKISH_CI("latin5_turkish_ci"),
        LATIN7_BIN("latin7_bin"),
        LATIN7_ESTONIAN_CS("latin7_estonian_cs"),
        LATIN7_GENERAL_CI("latin7_general_ci"),
        LATIN7_GENERAL_CS("latin7_general_cs"),
        MACCE_BIN("macce_bin"),
        MACCE_GENERAL_CI("macce_general_ci"),
        MACROMAN_BIN("macroman_bin"),
        MACROMAN_GENERAL_CI("macroman_general_ci"),
        SJIS_BIN("sjis_bin"),
        SJIS_JAPANESE_CI("sjis_japanese_ci"),
        SWE7_BIN("swe7_bin"),
        SWE7_SWEDISH_CI("swe7_swedish_ci"),
        TIS620_BIN("tis620_bin"),
        TIS620_THAI_CI("tis620_thai_ci"),
        UCS2_BIN("ucs2_bin"),
        UCS2_CROATIAN_CI("ucs2_croatian_ci"),
        UCS2_CZECH_CI("ucs2_czech_ci"),
        UCS2_DANISH_CI("ucs2_danish_ci"),
        UCS2_ESPERANTO_CI("ucs2_esperanto_ci"),
        UCS2_ESTONIAN_CI("ucs2_estonian_ci"),
        UCS2_GENERAL_CI("ucs2_general_ci"),
        UCS2_GENERAL_MYSQL500_CI("ucs2_general_mysql500_ci"),
        UCS2_GERMAN2_CI("ucs2_german2_ci"),
        UCS2_HUNGARIAN_CI("ucs2_hungarian_ci"),
        UCS2_ICELANDIC_CI("ucs2_icelandic_ci"),
        UCS2_LATVIAN_CI("ucs2_latvian_ci"),
        UCS2_LITHUANIAN_CI("ucs2_lithuanian_ci"),
        UCS2_PERSIAN_CI("ucs2_persian_ci"),
        UCS2_POLISH_CI("ucs2_polish_ci"),
        UCS2_ROMAN_CI("ucs2_roman_ci"),
        UCS2_ROMANIAN_CI("ucs2_romanian_ci"),
        UCS2_SINHALA_CI("ucs2_sinhala_ci"),
        UCS2_SLOVAK_CI("ucs2_slovak_ci"),
        UCS2_SLOVENIAN_CI("ucs2_slovenian_ci"),
        UCS2_SPANISH2_CI("ucs2_spanish2_ci"),
        UCS2_SPANISH_CI("ucs2_spanish_ci"),
        UCS2_SWEDISH_CI("ucs2_swedish_ci"),
        UCS2_TURKISH_CI("ucs2_turkish_ci"),
        UCS2_UNICODE_520_CI("ucs2_unicode_520_ci"),
        UCS2_UNICODE_CI("ucs2_unicode_ci"),
        UCS2_VIETNAMESE_CI("ucs2_vietnamese_ci"),
        UJIS_BIN("ujis_bin"),
        UJIS_JAPANESE_CI("ujis_japanese_ci"),
        UTF16_BIN("utf16_bin"),
        UTF16_CROATIAN_CI("utf16_croatian_ci"),
        UTF16_CZECH_CI("utf16_czech_ci"),
        UTF16_DANISH_CI("utf16_danish_ci"),
        UTF16_ESPERANTO_CI("utf16_esperanto_ci"),
        UTF16_ESTONIAN_CI("utf16_estonian_ci"),
        UTF16_GENERAL_CI("utf16_general_ci"),
        UTF16_GERMAN2_CI("utf16_german2_ci"),
        UTF16_HUNGARIAN_CI("utf16_hungarian_ci"),
        UTF16_ICELANDIC_CI("utf16_icelandic_ci"),
        UTF16_LATVIAN_CI("utf16_latvian_ci"),
        UTF16_LITHUANIAN_CI("utf16_lithuanian_ci"),
        UTF16_PERSIAN_CI("utf16_persian_ci"),
        UTF16_POLISH_CI("utf16_polish_ci"),
        UTF16_ROMAN_CI("utf16_roman_ci"),
        UTF16_ROMANIAN_CI("utf16_romanian_ci"),
        UTF16_SINHALA_CI("utf16_sinhala_ci"),
        UTF16_SLOVAK_CI("utf16_slovak_ci"),
        UTF16_SLOVENIAN_CI("utf16_slovenian_ci"),
        UTF16_SPANISH2_CI("utf16_spanish2_ci"),
        UTF16_SPANISH_CI("utf16_spanish_ci"),
        UTF16_SWEDISH_CI("utf16_swedish_ci"),
        UTF16_TURKISH_CI("utf16_turkish_ci"),
        UTF16_UNICODE_520_CI("utf16_unicode_520_ci"),
        UTF16_UNICODE_CI("utf16_unicode_ci"),
        UTF16_VIETNAMESE_CI("utf16_vietnamese_ci"),
        UTF16LE_BIN("utf16le_bin"),
        UTF16LE_GENERAL_CI("utf16le_general_ci"),
        UTF32_BIN("utf32_bin"),
        UTF32_CROATIAN_CI("utf32_croatian_ci"),
        UTF32_CZECH_CI("utf32_czech_ci"),
        UTF32_DANISH_CI("utf32_danish_ci"),
        UTF32_ESPERANTO_CI("utf32_esperanto_ci"),
        UTF32_ESTONIAN_CI("utf32_estonian_ci"),
        UTF32_GENERAL_CI("utf32_general_ci"),
        UTF32_GERMAN2_CI("utf32_german2_ci"),
        UTF32_HUNGARIAN_CI("utf32_hungarian_ci"),
        UTF32_ICELANDIC_CI("utf32_icelandic_ci"),
        UTF32_LATVIAN_CI("utf32_latvian_ci"),
        UTF32_LITHUANIAN_CI("utf32_lithuanian_ci"),
        UTF32_PERSIAN_CI("utf32_persian_ci"),
        UTF32_POLISH_CI("utf32_polish_ci"),
        UTF32_ROMAN_CI("utf32_roman_ci"),
        UTF32_ROMANIAN_CI("utf32_romanian_ci"),
        UTF32_SINHALA_CI("utf32_sinhala_ci"),
        UTF32_SLOVAK_CI("utf32_slovak_ci"),
        UTF32_SLOVENIAN_CI("utf32_slovenian_ci"),
        UTF32_SPANISH2_CI("utf32_spanish2_ci"),
        UTF32_SPANISH_CI("utf32_spanish_ci"),
        UTF32_SWEDISH_CI("utf32_swedish_ci"),
        UTF32_TURKISH_CI("utf32_turkish_ci"),
        UTF32_UNICODE_520_CI("utf32_unicode_520_ci"),
        UTF32_UNICODE_CI("utf32_unicode_ci"),
        UTF32_VIETNAMESE_CI("utf32_vietnamese_ci"),
        UTF8_BIN("utf8_bin"),
        UTF8_CROATIAN_CI("utf8_croatian_ci"),
        UTF8_CZECH_CI("utf8_czech_ci"),
        UTF8_DANISH_CI("utf8_danish_ci"),
        UTF8_ESPERANTO_CI("utf8_esperanto_ci"),
        UTF8_ESTONIAN_CI("utf8_estonian_ci"),
        UTF8_GENERAL_CI("utf8_general_ci"),
        UTF8_GENERAL_MYSQL500_CI("utf8_general_mysql500_ci"),
        UTF8_GERMAN2_CI("utf8_german2_ci"),
        UTF8_HUNGARIAN_CI("utf8_hungarian_ci"),
        UTF8_ICELANDIC_CI("utf8_icelandic_ci"),
        UTF8_LATVIAN_CI("utf8_latvian_ci"),
        UTF8_LITHUANIAN_CI("utf8_lithuanian_ci"),
        UTF8_PERSIAN_CI("utf8_persian_ci"),
        UTF8_POLISH_CI("utf8_polish_ci"),
        UTF8_ROMAN_CI("utf8_roman_ci"),
        UTF8_ROMANIAN_CI("utf8_romanian_ci"),
        UTF8_SINHALA_CI("utf8_sinhala_ci"),
        UTF8_SLOVAK_CI("utf8_slovak_ci"),
        UTF8_SLOVENIAN_CI("utf8_slovenian_ci"),
        UTF8_SPANISH2_CI("utf8_spanish2_ci"),
        UTF8_SPANISH_CI("utf8_spanish_ci"),
        UTF8_SWEDISH_CI("utf8_swedish_ci"),
        UTF8_TOLOWER_CI("utf8_tolower_ci"),
        UTF8_TURKISH_CI("utf8_turkish_ci"),
        UTF8_UNICODE_520_CI("utf8_unicode_520_ci"),
        UTF8_UNICODE_CI("utf8_unicode_ci"),
        UTF8_VIETNAMESE_CI("utf8_vietnamese_ci"),
        UTF8MB4_0900_AI_CI("utf8mb4_0900_ai_ci"),
        UTF8MB4_0900_AS_CI("utf8mb4_0900_as_ci"),
        UTF8MB4_0900_AS_CS("utf8mb4_0900_as_cs"),
        UTF8MB4_0900_BIN("utf8mb4_0900_bin"),
        UTF8MB4_BIN("utf8mb4_bin"),
        UTF8MB4_CROATIAN_CI("utf8mb4_croatian_ci"),
        UTF8MB4_CS_0900_AI_CI("utf8mb4_cs_0900_ai_ci"),
        UTF8MB4_CS_0900_AS_CS("utf8mb4_cs_0900_as_cs"),
        UTF8MB4_CZECH_CI("utf8mb4_czech_ci"),
        UTF8MB4_DA_0900_AI_CI("utf8mb4_da_0900_ai_ci"),
        UTF8MB4_DA_0900_AS_CS("utf8mb4_da_0900_as_cs"),
        UTF8MB4_DANISH_CI("utf8mb4_danish_ci"),
        UTF8MB4_DE_PB_0900_AI_CI("utf8mb4_de_pb_0900_ai_ci"),
        UTF8MB4_DE_PB_0900_AS_CS("utf8mb4_de_pb_0900_as_cs"),
        UTF8MB4_EO_0900_AI_CI("utf8mb4_eo_0900_ai_ci"),
        UTF8MB4_EO_0900_AS_CS("utf8mb4_eo_0900_as_cs"),
        UTF8MB4_ES_0900_AI_CI("utf8mb4_es_0900_ai_ci"),
        UTF8MB4_ES_0900_AS_CS("utf8mb4_es_0900_as_cs"),
        UTF8MB4_ES_TRAD_0900_AI_CI("utf8mb4_es_trad_0900_ai_ci"),
        UTF8MB4_ES_TRAD_0900_AS_CS("utf8mb4_es_trad_0900_as_cs"),
        UTF8MB4_ESPERANTO_CI("utf8mb4_esperanto_ci"),
        UTF8MB4_ESTONIAN_CI("utf8mb4_estonian_ci"),
        UTF8MB4_ET_0900_AI_CI("utf8mb4_et_0900_ai_ci"),
        UTF8MB4_ET_0900_AS_CS("utf8mb4_et_0900_as_cs"),
        UTF8MB4_GENERAL_CI("utf8mb4_general_ci"),
        UTF8MB4_GERMAN2_CI("utf8mb4_german2_ci"),
        UTF8MB4_HR_0900_AI_CI("utf8mb4_hr_0900_ai_ci"),
        UTF8MB4_HR_0900_AS_CS("utf8mb4_hr_0900_as_cs"),
        UTF8MB4_HU_0900_AI_CI("utf8mb4_hu_0900_ai_ci"),
        UTF8MB4_HU_0900_AS_CS("utf8mb4_hu_0900_as_cs"),
        UTF8MB4_HUNGARIAN_CI("utf8mb4_hungarian_ci"),
        UTF8MB4_ICELANDIC_CI("utf8mb4_icelandic_ci"),
        UTF8MB4_IS_0900_AI_CI("utf8mb4_is_0900_ai_ci"),
        UTF8MB4_IS_0900_AS_CS("utf8mb4_is_0900_as_cs"),
        UTF8MB4_JA_0900_AS_CS("utf8mb4_ja_0900_as_cs"),
        UTF8MB4_JA_0900_AS_CS_KS("utf8mb4_ja_0900_as_cs_ks"),
        UTF8MB4_LA_0900_AI_CI("utf8mb4_la_0900_ai_ci"),
        UTF8MB4_LA_0900_AS_CS("utf8mb4_la_0900_as_cs"),
        UTF8MB4_LATVIAN_CI("utf8mb4_latvian_ci"),
        UTF8MB4_LITHUANIAN_CI("utf8mb4_lithuanian_ci"),
        UTF8MB4_LT_0900_AI_CI("utf8mb4_lt_0900_ai_ci"),
        UTF8MB4_LT_0900_AS_CS("utf8mb4_lt_0900_as_cs"),
        UTF8MB4_LV_0900_AI_CI("utf8mb4_lv_0900_ai_ci"),
        UTF8MB4_LV_0900_AS_CS("utf8mb4_lv_0900_as_cs"),
        UTF8MB4_PERSIAN_CI("utf8mb4_persian_ci"),
        UTF8MB4_PL_0900_AI_CI("utf8mb4_pl_0900_ai_ci"),
        UTF8MB4_PL_0900_AS_CS("utf8mb4_pl_0900_as_cs"),
        UTF8MB4_POLISH_CI("utf8mb4_polish_ci"),
        UTF8MB4_RO_0900_AI_CI("utf8mb4_ro_0900_ai_ci"),
        UTF8MB4_RO_0900_AS_CS("utf8mb4_ro_0900_as_cs"),
        UTF8MB4_ROMAN_CI("utf8mb4_roman_ci"),
        UTF8MB4_ROMANIAN_CI("utf8mb4_romanian_ci"),
        UTF8MB4_RU_0900_AI_CI("utf8mb4_ru_0900_ai_ci"),
        UTF8MB4_RU_0900_AS_CS("utf8mb4_ru_0900_as_cs"),
        UTF8MB4_SINHALA_CI("utf8mb4_sinhala_ci"),
        UTF8MB4_SK_0900_AI_CI("utf8mb4_sk_0900_ai_ci"),
        UTF8MB4_SK_0900_AS_CS("utf8mb4_sk_0900_as_cs"),
        UTF8MB4_SL_0900_AI_CI("utf8mb4_sl_0900_ai_ci"),
        UTF8MB4_SL_0900_AS_CS("utf8mb4_sl_0900_as_cs"),
        UTF8MB4_SLOVAK_CI("utf8mb4_slovak_ci"),
        UTF8MB4_SLOVENIAN_CI("utf8mb4_slovenian_ci"),
        UTF8MB4_SPANISH2_CI("utf8mb4_spanish2_ci"),
        UTF8MB4_SPANISH_CI("utf8mb4_spanish_ci"),
        UTF8MB4_SV_0900_AI_CI("utf8mb4_sv_0900_ai_ci"),
        UTF8MB4_SV_0900_AS_CS("utf8mb4_sv_0900_as_cs"),
        UTF8MB4_SWEDISH_CI("utf8mb4_swedish_ci"),
        UTF8MB4_TR_0900_AI_CI("utf8mb4_tr_0900_ai_ci"),
        UTF8MB4_TR_0900_AS_CS("utf8mb4_tr_0900_as_cs"),
        UTF8MB4_TURKISH_CI("utf8mb4_turkish_ci"),
        UTF8MB4_UNICODE_520_CI("utf8mb4_unicode_520_ci"),
        UTF8MB4_UNICODE_CI("utf8mb4_unicode_ci"),
        UTF8MB4_VI_0900_AI_CI("utf8mb4_vi_0900_ai_ci"),
        UTF8MB4_VI_0900_AS_CS("utf8mb4_vi_0900_as_cs"),
        UTF8MB4_VIETNAMESE_CI("utf8mb4_vietnamese_ci"),
        UTF8MB4_ZH_0900_AS_CS("utf8mb4_zh_0900_as_cs");

        private final String sqlFragment;

        Collation(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum ColumnProperty {
        BINARY("BINARY"),
        UNSIGNED("UNSIGNED"),
        ZERO_FILL("ZERO FILL"),
        NULL("NULL"),
        NOT_NULL("NOT NULL"),
        AUTO_INCREMENT("AUTO_INCREMENT");

        private final static ColumnPropertyComparator COLUMN_PROPERTY_COMPARATOR = new ColumnPropertyComparator();

        public static void sort(ColumnProperty... columnProperties) {
            Arrays.sort(columnProperties, COLUMN_PROPERTY_COMPARATOR);
        }

        public static void sort(List<ColumnProperty> columnProperties) {
            columnProperties.sort(COLUMN_PROPERTY_COMPARATOR);
        }

        private final String sqlFragment;

        ColumnProperty(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }

        private static class ColumnPropertyComparator implements Comparator<ColumnProperty> {

            private static final Map<ColumnProperty, Integer> INDEX_MAP;

            static {
                INDEX_MAP = new EnumMap<>(ColumnProperty.class);
                INDEX_MAP.put(BINARY, 0);
                INDEX_MAP.put(UNSIGNED, 1);
                INDEX_MAP.put(ZERO_FILL, 2);
                INDEX_MAP.put(NULL, 3);
                INDEX_MAP.put(NOT_NULL, 4);
                INDEX_MAP.put(AUTO_INCREMENT, 5);
            }

            @Override
            public int compare(ColumnProperty o1, ColumnProperty o2) {
                if (Objects.equals(o1, o2)) {
                    return 0;
                }
                return INDEX_MAP.get(o1).compareTo(INDEX_MAP.get(o2));
            }
        }
    }

    public enum IndexType {
        INDEX("INDEX"),
        UNIQUE("UNIQUE INDEX"),
        FULLTEXT("FULLTEXT INDEX"),
        SPATIAL("SPATIAL INDEX"),
        PRIMARY("PRIMARY KEY");

        private final String sqlFragment;

        IndexType(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum IndexStorage {
        BTREE("BTREE");

        private final String sqlFragment;

        IndexStorage(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

    public enum ForeignKeyOption {
        RESTRICT("RESTRICT"),
        CASCADE("CASCADE"),
        SET_NULL("SET NULL"),
        NO_ACTION("NO ACTION");

        private final String sqlFragment;

        ForeignKeyOption(String sqlFragment) {
            this.sqlFragment = sqlFragment;
        }

        public String sqlFragment() {
            return sqlFragment;
        }
    }

//    TODO 待实现方法参考模板。
//
//    /**
//     * 设置数据表的库名称。
//     *
//     * @param schemaName 数据表的库名称。
//     */
//    public void setSchemaName(String schemaName) {
//        this.schemaName = schemaName;
//    }
//
//    /**
//     * 设置数据表的名称。
//     *
//     * @param tableName 数据表的名称。
//     */
//    public void setTableName(String tableName) {
//        this.tableName = tableName;
//    }
//
//    /**
//     * 设置数据库引擎。
//     *
//     * @param engine 数据库引擎。
//     */
//    public void setEngine(@Nonnull Engine engine) {
//        customDefinition.put(MySQL8Constants.CUSTOM_ENGINE, engine);
//    }
//
//    /**
//     * 设置数据库的默认字符集。
//     *
//     * @param charset 数据库的默认字符集。
//     */
//    public void setDefaultCharset(@Nonnull Charset charset) {
//        customDefinition.put(MySQL8Constants.CUSTOM_DEFAULT_CHARSET, charset);
//    }
//
//    /**
//     * 设置数据库字符的对比方法。
//     *
//     * @param collation 数据库字符的对比方法。
//     */
//    public void setCollation(@Nonnull Collation collation) {
//        customDefinition.put(MySQL8Constants.CUSTOM_COLLATION, collation);
//    }
//
//    /**
//     * 设置数据库自增列的自增大小。
//     *
//     * @param autoIncrement 数据库自增列的自增大小。
//     */
//    public void setAutoIncrement(int autoIncrement) {
//        customDefinition.put(MySQL8Constants.CUSTOM_AUTO_INCREMENT, autoIncrement);
//    }
//
//    /**
//     * 增加列定义。
//     *
//     * @param columnName 列的名称。
//     * @param type       列的类型。
//     */
//    public void addColumn(@Nonnull String columnName, @Nonnull String type) {
//        addColumn(columnName, type, null, null, null);
//    }
//
//    /**
//     * 增加列定义。
//     *
//     * @param columnName       列的名称。
//     * @param type             列的类型。
//     * @param defaultValue     列的默认值。
//     * @param columnProperties 列的属性。
//     */
//    public void addColumn(
//            @Nonnull String columnName, @Nonnull String type, String defaultValue, Charset characterSet,
//            Collation collate, ColumnProperty... columnProperties) {
//        makeSureAllColumnNotExists(Collections.singletonList(columnName));
//        makeSureColumnPropertiesValid(Arrays.asList(columnProperties));
//        Map<String, Object> customDefinition = new HashMap<>();
//        customDefinition.put(MySQL8Constants.CUSTOM_DEFAULT, defaultValue);
//        customDefinition.put(MySQL8Constants.CUSTOM_COLUMN_PROPERTY, Arrays.asList(columnProperties));
//        if (Objects.nonNull(characterSet)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_CHARACTER_SET, characterSet);
//        }
//        if (Objects.nonNull(collate)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_COLLATE, collate);
//        }
//        columnDefinitionMap.put(columnName, new DefaultColumnDefinition(columnName, type, customDefinition));
//    }
//
//    private void makeSureColumnPropertiesValid(List<ColumnProperty> columnProperties) {
//        if (CollectionUtil.conatinsNull(columnProperties)) {
//            throw new IllegalArgumentException("入口参数 columnProperties 不允许含有 NULL 元素");
//        }
//        boolean nullFlag = false;
//        boolean notNullFlag = false;
//        for (ColumnProperty columnProperty : columnProperties) {
//            if (Objects.equals(columnProperty, ColumnProperty.NULL)) {
//                nullFlag = true;
//            } else if (Objects.equals(columnProperty, ColumnProperty.NOT_NULL)) {
//                notNullFlag = true;
//            }
//        }
//        if (nullFlag && notNullFlag) {
//            throw new IllegalArgumentException("入口参数 columnProperties 不允许同时包含 NULL 和 NOT_NULL");
//        }
//    }
//
//    /**
//     * 添加索引。
//     *
//     * @param name        索引的名称。
//     * @param indexType   索引的类型。
//     * @param columnNames 与索引相关的列名称。
//     */
//    public void addIndex(@Nonnull String name, @Nonnull IndexType indexType, @Nonnull String... columnNames) {
//        addIndex(name, indexType, null, null, null, columnNames);
//    }
//
//    /**
//     * 添加索引。
//     *
//     * @param name         索引的名称。
//     * @param indexType    索引的类型。
//     * @param indexStorage 索引的存储。
//     * @param keyBlockSize 索引的 KEY_BLOCK_SIZE。
//     * @param parser       索引的 parser。
//     * @param columnNames  与索引有关的列名称。
//     */
//    public void addIndex(
//            @Nonnull String name, @Nonnull IndexType indexType, IndexStorage indexStorage,
//            Integer keyBlockSize, String parser, @Nonnull String... columnNames) {
//        List<String> nameList = Arrays.asList(columnNames);
//        makeSureAllColumnExists(nameList);
//        Map<String, Object> customDefinition = new HashMap<>();
//        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_TYPE, indexType);
//        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_LENGTH, new HashMap<String, Integer>());
//        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_ASC, new HashSet<String>());
//        customDefinition.put(MySQL8Constants.CUSTOM_INDEX_DESC, new HashSet<String>());
//        if (Objects.nonNull(indexStorage)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_STORAGE, indexStorage);
//        }
//        if (Objects.nonNull(keyBlockSize)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_KEY_BLOCK_SIZE, keyBlockSize);
//        }
//        if (Objects.nonNull(parser)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_INDEX_PARSER, parser);
//        }
//        indexDefinitionMap.put(name, new DefaultIndexDefinition(
//                MySQL8Constants.NAME_PRIMARY_KEY, MySQL8Constants.TYPE_INDEX,
//                nameList.stream().map(columnDefinitionMap::get).collect(Collectors.toList()), customDefinition));
//    }
//
//    /**
//     * 设置索引的长度。
//     *
//     * @param indexName  索引的名称。
//     * @param columnName 需要设置索引长度的列。
//     * @param length     索引的长度。
//     */
//    @SuppressWarnings("unchecked")
//    public void setIndexLength(@Nonnull String indexName, @Nonnull String columnName, Integer length) {
//        makeSureIndexExists(indexName);
//        makeSureAllColumnExists(Collections.singletonList(columnName));
//        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
//        HashMap<String, Integer> lengthMap =
//                (HashMap<String, Integer>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_LENGTH);
//        if (Objects.isNull(length)) {
//            lengthMap.remove(columnName);
//        } else {
//            lengthMap.put(columnName, length);
//        }
//    }
//
//    /**
//     * 设置索引升序排序。
//     *
//     * @param indexName   索引的名称。
//     * @param columnNames 需要设置索引升序排序的列的名称。
//     */
//    @SuppressWarnings("unchecked")
//    public void setIndexAsc(@Nonnull String indexName, @Nonnull String... columnNames) {
//        List<String> columnNameList = Arrays.asList(columnNames);
//        makeSureIndexExists(indexName);
//        makeSureAllColumnExists(columnNameList);
//        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
//        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_ASC)).addAll(columnNameList);
//        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_DESC)).removeAll(columnNameList);
//    }
//
//    /**
//     * 设置索引降序排序。
//     *
//     * @param indexName   索引的名称。
//     * @param columnNames 需要设置索引降序排序的列的名称。
//     */
//    @SuppressWarnings("unchecked")
//    public void setIndexDesc(@Nonnull String indexName, @Nonnull String... columnNames) {
//        List<String> columnNameList = Arrays.asList(columnNames);
//        makeSureIndexExists(indexName);
//        makeSureAllColumnExists(columnNameList);
//        Map<String, Object> customDefinition = indexDefinitionMap.get(indexName).getCustomDefinition();
//        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_ASC)).removeAll(columnNameList);
//        ((HashSet<String>) customDefinition.get(MySQL8Constants.CUSTOM_INDEX_DESC)).addAll(columnNameList);
//    }
//
//    /**
//     * 设置外键约束。
//     *
//     * @param foreignKeyName 外键约束的名称。
//     * @param refSchemaName  外键约束引用的数据库名称。
//     * @param refTableName   外键约束引用的数据表名称。
//     * @param refColumnNames 外键约束引用的列名称。
//     * @param columnNames    与外键约束有关的列名称。
//     */
//    public void addForeignKey(
//            @Nonnull String foreignKeyName, String refSchemaName, @Nonnull String refTableName,
//            @Nonnull List<String> refColumnNames, @Nonnull List<String> columnNames) {
//        addForeignKey(foreignKeyName, refSchemaName, refTableName, refColumnNames, columnNames, null, null);
//    }
//
//    /**
//     * 设置外键约束。
//     *
//     * @param foreignKeyName 外键约束的名称。
//     * @param refSchemaName  外键约束引用的数据库名称。
//     * @param refTableName   外键约束引用的数据表名称。
//     * @param refColumnNames 外键约束引用的列名称。
//     * @param columnNames    与外键约束有关的列名称。
//     * @param onUpdate       有关列更新时的动作。
//     * @param onDelete       有关列删除时的动作。
//     */
//    public void addForeignKey(
//            @Nonnull String foreignKeyName, String refSchemaName, @Nonnull String refTableName,
//            @Nonnull List<String> refColumnNames, @Nonnull List<String> columnNames, ForeignKeyOption onUpdate,
//            ForeignKeyOption onDelete) {
//        makeSureAllColumnExists(columnNames);
//        Map<String, Object> customDefinition = new HashMap<>();
//        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_SCHEMA, refSchemaName);
//        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_TABLE, refTableName);
//        customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_REF_COLUMNS, refColumnNames);
//        if (Objects.nonNull(onUpdate)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_ON_UPDATE, onUpdate);
//        }
//        if (Objects.nonNull(onDelete)) {
//            customDefinition.put(MySQL8Constants.CUSTOM_FOREIGN_ON_DELETE, onDelete);
//        }
//        constraintDefinitionMap.put(foreignKeyName, new DefaultConstraintDefinition(
//                foreignKeyName, MySQL8Constants.TYPE_FOREIGN_KEY,
//                columnNames.stream().map(columnDefinitionMap::get).collect(Collectors.toList()), customDefinition));
//    }
//
//    private void makeSureAllColumnNotExists(List<String> nameList) {
//        for (String name : nameList) {
//            if (columnDefinitionMap.containsKey(name)) {
//                throw new IllegalArgumentException("列名称 " + name + "已经存在");
//            }
//        }
//    }
//
//    private void makeSureAllColumnExists(List<String> nameList) {
//        if (!columnDefinitionMap.keySet().containsAll(nameList)) {
//            for (String name : nameList) {
//                if (!columnDefinitionMap.containsKey(name)) {
//                    throw new IllegalArgumentException("列名称" + name + "不存在");
//                }
//            }
//        }
//    }
//
//    private void makeSureIndexExists(String name) {
//        if (!indexDefinitionMap.containsKey(name)) {
//            throw new IllegalArgumentException("索引称" + name + "不存在");
//        }
//    }
//
//    /**
//     * 构造数据表定义。
//     *
//     * @return 构造生成的数据表定义。
//     */
//    @SuppressWarnings("DuplicatedCode")
//    public TableDefinition buildTableDefinition() {
//        if (Objects.isNull(tableName)) {
//            throw new IllegalArgumentException("您还没有指定表名称，请先调用 setTableName(String)");
//        }
//        if (Objects.nonNull(schemaName) && tableName.contains(".")) {
//            throw new IllegalArgumentException("指定数据库名称时，表名称不应该含有字符 '.'");
//        }
//        if (columnDefinitionMap.isEmpty()) {
//            throw new IllegalArgumentException("您还没有指定表中的任何列，请先调用 addColumn(String, String)");
//        }
//        long count = indexDefinitionMap.entrySet().stream().filter(
//                entry -> Objects.equals(entry.getValue().getCustomDefinition().get(MySQL8Constants.CUSTOM_INDEX_TYPE),
//                        IndexType.PRIMARY)).map(Map.Entry::getKey).count();
//        if (count == 0) {
//            throw new IllegalArgumentException("您还没有指定表中的主键，先调用 addIndex(String, IndexType, " +
//                    "IndexStorage, Integer, String, String...) 方法添加类型为 IndexType.PRIMARY的主键");
//        }
//        if (count > 1) {
//            throw new IllegalArgumentException("您指定了多个主键索引，请检查程序并去除多余的主键索引");
//        }
//
//        return new DefaultTableDefinition(
//                schemaName,
//                tableName,
//                new ArrayList<>(columnDefinitionMap.values()),
//                new ArrayList<>(constraintDefinitionMap.values()),
//                new ArrayList<>(indexDefinitionMap.values()),
//                customDefinition
//        );
//    }
}
