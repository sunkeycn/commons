package sunkey.common.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 此枚举类只有两个值：可用；删除；
 * 如需新增属性，继承此枚举
 *
 * @author Sunkey
 * @since 2018-03-21 10:27
 **/
@AllArgsConstructor
@Getter
public enum State {

    IN_USE((byte) 1),
    NOT_ENABLED((byte)2),
    DELETE((byte) 127),;
	
    private final byte code;

}
