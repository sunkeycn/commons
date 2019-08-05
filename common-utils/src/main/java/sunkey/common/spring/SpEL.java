package sunkey.common.spring;

import org.springframework.context.expression.*;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;

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
        context.addPropertyAccessor(new BeanExpressionContextAccessor());
        context.addPropertyAccessor(new BeanFactoryAccessor());
        context.addPropertyAccessor(new EnvironmentAccessor());
        if (Spring.isLoaded()) {
            context.setBeanResolver(new BeanFactoryResolver(Spring.getApplicationContext()));
            context.setTypeLocator(new StandardTypeLocator(
                    Spring.getApplicationContext().getBeanFactory().getBeanClassLoader()));
            context.setTypeConverter(new StandardTypeConverter(Spring.getConversionService()));
        } else {
            context.setTypeConverter(new StandardTypeConverter(new DefaultConversionService()));
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
