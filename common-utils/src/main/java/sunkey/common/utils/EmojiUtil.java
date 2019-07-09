package sunkey.common.utils;

public class EmojiUtil {
    public static boolean isEmojiCharacter(char codePoint) {
        return codePoint >= 0x9FFF;
    }

    public static String replaceEmoji(String str, final String replace) {
        return replaceEmoji(str, new Holder<Character, Object>() {
            public Object hold(Character t) {
                return replace;
            }
        });
    }

    public static String replaceEmoji(String str, Holder<Character, Object> holder) {
        if (str == null || str.isEmpty()) return str;
        if (holder == null) throw new NullPointerException("holder");
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (isEmojiCharacter(chr)) {
                Object res = holder.hold(chr);
                if (res != null) sb.append(res);
            } else {
                sb.append(chr);
            }
        }
        return sb.toString();
    }

    private static interface Holder<I, O> {
        O hold(I t);
    }

    public static boolean containsEmoji(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     */
    public static String filterEmoji(String source) {
        source = replaceEmoji(source, "");
        if (source == null || source.isEmpty()) {//如果有效字符为空，返回???
            return "???";
        }
        return source;
    }

}
