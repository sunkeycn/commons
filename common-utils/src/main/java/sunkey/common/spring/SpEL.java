package sunkey.common.spring;

import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Sunkey
 * @since 2019-06-12 15:52
 **/
public class SpEL {

    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private static final StandardEvaluationContext context;

    static {
        context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());
        if (Spring.isLoaded()) {
            context.setBeanResolver(new BeanFactoryResolver(Spring.getApplicationContext()));
        }
    }

    public static Object eval(String spel) {
        SpelExpression expression = parser.parseRaw(spel);
        return expression.getValue(context);
    }

    public static Object eval(String spel, Object value) {
        SpelExpression expression = parser.parseRaw(spel);
        return expression.getValue(context, value);
    }

}
