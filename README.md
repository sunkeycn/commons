# common-utils 通用工具包
> * 通用常量、接口、异常定义、状态枚举等
> * MQ消息队列支持(ONS)
> * 通用工具类:并发、异常、响应体、VOs、SpringBoot、JSON、Regex、Date、Network等


### Invoke 使用介绍

```

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


```

### ONS 使用介绍

```
@SpringBootApplication
@EnableOnsMessage
public class MyApplication {
    public static void main(String[] args){
        SpringApplication.run(MyApplication.class, args);
    }
}

@Component
public class MyMqMessageHandler {
    
    @MessageListener(topic="${ons.order.topic}", tags="*")
    public Action handleMyOrderMessage(String topic, 
                                       String tag, 
                                       String body, 
                                       byte[] data, 
                                       Message message, 
                                       ConsumeContext context){
        // do something...
        return Action.CommitMessage;
    }
    
    @MessageListener(topic="MyTopic", tags="MyTag1||MyTag2||MyTag3")
    public void handleMyOrderMessage(Message message){
        // do something...
    }
    
    @MessageListener(topic="MyTopic")
    public void handleMyOrderMessage(String data) throws Exception {
        // do something...
    }
        
}

```