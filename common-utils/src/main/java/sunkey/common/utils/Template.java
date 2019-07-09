package sunkey.common.utils;

import sunkey.common.utils.template.system.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sunkey
 * @since 2019-03-22 13:38
 **/
public class Template {

    public static final String OPEN = "{";
    public static final String CLOSE = "}";
    public static final char REPLACER = '\\';

    private static final Extensions extensions = new Extensions();
    private static final Map<String, Template> caches = new HashMap<>();

    public static Extensions extensions() {
        return extensions;
    }

    /**
     * Without Cache, equal to new Template(str)
     *
     * @param template
     * @return
     */
    public static Template valueOf(String template) {
        return new Template(template);
    }

    /**
     * With Cache.
     *
     * @param template
     * @return
     */
    public static Template forName(String template) {
        return caches.computeIfAbsent(template, Template::new);
    }

    private final String expression;
    private final LinkedList<Block> blocks = new LinkedList<>();

    public Template(String expression) {
        this.expression = expression;
        explain();
        makeAliases();
    }

    public String format(Object... params) {
        Map<String, Object> _params = new HashMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                _params.put(String.valueOf(i + 1), params[i]);
                _params.put("$" + i, params[i]);
            }
        }
        return render(_params);
    }

    public String render(Map<String, ?> context) {
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            sb.append(block.render(context));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return expression;
    }

    private void explain() {
        int offset = 0;
        int[] points = findNextParam(offset);
        while (points != null) {
            if (points[0] > offset)
                blocks.add(new TextBlock(expression.substring(offset, points[0])));
            blocks.add(parse(expression.substring(points[0] + 1, points[1])));
            offset = points[1] + 1;
            points = findNextParam(offset);
        }
        if (offset != expression.length())
            blocks.add(new TextBlock(expression.substring(offset)));
    }

    private void makeAliases() {
        AtomicInteger counter = new AtomicInteger();
        blocks.stream()
                .filter(b -> b instanceof ParamBlock)
                .map(b -> (ParamBlock) b)
                .forEach(b -> b.addAlias("$" + counter.getAndIncrement()));
    }

    private static Block parse(String expression) {
        Extension extension = extensions.find(expression);
        if (extension == null) {
            return new ParamBlock(expression);
        } else if (extension instanceof Function) {
            return new FunctionParamBlock(expression, (Function) extension);
        } else {
            return new ExtensionParamBlock(expression, extension);
        }
    }

    private int[] findNextParam(int offset) {
        return findNextParam(expression, offset, OPEN, CLOSE, REPLACER);
    }

    private static int[] findNextParam(String str, int offset, String open, String close, char replacer) {
        int startIndex = findIndex(str, open, offset, replacer);
        if (startIndex != -1) {
            int endIndex = findIndex(str, close, startIndex + 1, replacer);
            if (endIndex != -1) {
                return new int[]{startIndex, endIndex};
            }
        }
        return null;
    }

    private static int findIndex(String str, String find, int startOffset, char replacer) {
        int i = str.indexOf(find, startOffset);
        if (i == -1) {
            return -1;
        }
        if (i > 0 && str.charAt(i - 1) == replacer) {
            return findIndex(str, find, i + 1, replacer);
        }
        return i;
    }

}
