package sunkey.tests.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import sunkey.common.ons.EnableOnsMessage;
import sunkey.common.ons.MessageListener;

/**
 * @author Sunkey
 * @since 2019-07-09 17:35
 **/
public class OnsUseCases {

    //@SpringBootApplication
    @EnableOnsMessage
    public static class MyApplication {
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class, args);
        }
    }

    @Component
    public class MyMqMessageHandler {

        @MessageListener(topic = "${ons.order.topic}", tags = "*")
        public Action handleMyOrderMessage(String topic,
                                           String tag,
                                           String body,
                                           byte[] data,
                                           Message message,
                                           ConsumeContext context) {
            // do something...
            return Action.CommitMessage;
        }

        @MessageListener(topic = "MyTopic", tags = "MyTag1||MyTag2||MyTag3")
        public void handleMyOrderMessage(Message message) {
            // do something...
        }

        @MessageListener(topic = "MyTopic")
        public void handleMyOrderMessage(String data) throws Exception {
            // do something...
        }

    }


}
