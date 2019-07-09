package sunkey.common.consts;

import java.io.Serializable;

/**
 * @author Sunkey
 * @since 2018-03-21 18:35
 **/
public interface NameCode extends Serializable {

    int getCode();

    String getName();

    default byte getCodeAsByte() {
        return (byte) getCode();
    }

}
