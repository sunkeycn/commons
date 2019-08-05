package sunkey.common.utils.excel;

import lombok.Getter;
import lombok.Setter;
import sunkey.common.utils.excel.valid.ValidResult;

import java.util.ArrayList;

/**
 * @author Sunkey
 * @since 2019-06-11 11:40
 **/
@Getter
@Setter
public class Result<T> extends ArrayList<T> {

    private ReadConfiguration<T> config;
    private ValidResult validResult;

    public Result() {
    }

    public Result(ReadConfiguration<T> config) {
        this.config = config;
        this.validResult = new ValidResult(config.getErrorFormatter());
    }

    public boolean hasErrors() {
        return validResult.hasErrors();
    }

    @Override
    public String toString() {
        return "Result[result=" + validResult.toString() + ", data=" + super.toString() + "]";
    }
}
