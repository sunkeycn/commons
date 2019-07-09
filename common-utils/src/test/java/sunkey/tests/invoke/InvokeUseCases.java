package sunkey.tests.invoke;

import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;
import sunkey.common.invoke.*;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author Sunkey
 * @since 2019-07-09 17:02
 **/
public class InvokeUseCases {

    public static void main(String[] args) throws Exception {
        MethodResolver<OnsInvokeContext> resolver = new MethodResolver<>();
        resolver.add(new OnsDefaultArgumentResolver());
        Method testMethod = InvokeUseCases.class.getMethod("testMethod", String.class, Message.class);
        MethodInvoker<OnsInvokeContext> invoker = resolver.getMethodInvoker(testMethod);
        InvokeUseCases target = new InvokeUseCases();
        OnsInvokeContext context = new OnsInvokeContext();
        Object result = invoker.invoke(target, context);
    }

    public Object testMethod(String body, Message message) {
        return null;
    }

    @Getter
    @Setter
    @ToString
    public static class OnsInvokeContext implements InvokeContext {
        private Message message;
        private ConsumeContext context;
    }

    @Order
    public static class OnsDefaultArgumentResolver implements ArgumentResolver<OnsInvokeContext> {
        @Override
        public Object resolveArgument(OnsInvokeContext context, Argument arg) {
            if (arg.isType(Message.class)) {
                return context.getMessage();
            }
            if (arg.isType(ConsumeContext.class)) {
                return context.getContext();
            }
            if (arg.isType(byte[].class)) {
                return context.getMessage().getBody();
            }
            if (arg.isType(String.class)) {
                switch (arg.getName()) {
                    case "topic":
                        return context.getMessage().getTopic();
                    case "tag":
                        return context.getMessage().getTag();
                    case "data":
                    case "body":
                        return new String(context.getMessage().getBody(), StandardCharsets.UTF_8);
                    case "msgId":
                        return context.getMessage().getMsgID();
                    case "key":
                        return context.getMessage().getKey();
                }
            }
            throw arg.cannotResolve();
        }

        @Override
        public boolean canResolve(Argument arg) {
            if (arg.isOneOfType(Message.class, ConsumeContext.class, byte[].class)) {
                return true;
            }
            if (arg.isType(String.class) && arg.isOneOfName("topic", "tag", "data", "body", "msgId", "key")) {
                return true;
            }
            return false;
        }
    }

}
