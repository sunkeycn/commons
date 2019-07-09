package sunkey.common.utils.excel;

import sunkey.common.utils.excel.support.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author Sunkey
 * @since 2019-06-11 11:51
 **/
@Getter
@Setter
@ToString
public class ReadConfiguration<T> {

    private Class<T> dataType;
    /**
     * base on 0
     */
    private int headerRow = 0;
    /**
     * base on 0
     */
    private int dataStartRow = 1;
    /**
     * base on 0
     */
    private int dataEndRow = Integer.MAX_VALUE;
    /**
     * Validator可用(并且Bean标注@Valid)或跳过
     */
    private boolean autoValidate = true;

    private ValueExtractor headerNameExtractor = DefaultValueExtractor.INSTANCE;

    private ValueExtractor valueExtractor = DefaultValueExtractor.INSTANCE;

    private ObjectFactory objectFactory = DefaultObjectFactory.INSTANCE;

    private ErrorFormatter errorFormatter = DefaultErrorFormatter.INSTANCE;

    private ConversionService conversionService = new DefaultConversionService();

    public ReadConfiguration<T> dataType(Class<T> type) {
        this.dataType = type;
        return this;
    }

    public ReadConfiguration<T> headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public ReadConfiguration<T> dataStartRow(int dataStartRow) {
        this.dataStartRow = dataStartRow;
        return this;
    }

    public ReadConfiguration<T> dataEndRow(int dataEndRow) {
        this.dataEndRow = dataEndRow;
        return this;
    }

    public ReadConfiguration<T> autoValidate(boolean autoValidate) {
        this.autoValidate = autoValidate;
        return this;
    }

    public ReadConfiguration<T> headerNameExtractor(ValueExtractor headerNameExtractor) {
        this.headerNameExtractor = headerNameExtractor;
        return this;
    }

    /**
     * 默认Blank为null
     *
     * @param valueExtractor
     * @return
     */
    public ReadConfiguration<T> valueExtractor(ValueExtractor valueExtractor) {
        this.valueExtractor = valueExtractor;
        return this;
    }

    public ReadConfiguration<T> objectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
        return this;
    }

    public ReadConfiguration<T> errorFormatter(ErrorFormatter errorFormatter) {
        this.errorFormatter = errorFormatter;
        return this;
    }

    public ReadConfiguration<T> conversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
        return this;
    }

    public Class<T> getDataType() {
        return dataType;
    }

}
