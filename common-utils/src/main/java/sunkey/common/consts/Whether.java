package sunkey.common.consts;

/**
 * @author Sunkey
 * @since 2019-05-08 14:30
 **/
public interface Whether {

    byte YES = 1;
    byte NO = 0;

    static byte valueOf(boolean value) {
        return value ? YES : NO;
    }

    static boolean isYes(Byte value) {
        return value != null && value == YES;
    }

    static boolean isYesOrNull(Byte value) {
        return value == null || value == YES;
    }

    static boolean isNo(Byte value) {
        return value != null && value == NO;
    }

    static boolean isNoOrNull(Byte value) {
        return value == null || value == NO;
    }

}
