package sunkey.common.utils.excel.support;

import sunkey.common.utils.excel.valid.ValidResult;

/**
 * @author Sunkey
 * @since 2019-06-12 14:11
 **/
public interface ErrorFormatter {

    String format(ValidResult.Error error);

}
