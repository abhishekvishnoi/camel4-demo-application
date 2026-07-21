import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class CustomerStreamRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        /**
         * Camel Route to pick up a message from Debezium's Kafka Topic ,
         * Strip off the unnecessary details and push the message to 2 distinct
         * downstream endpoints : ie Kafka | ArtemisMQ
         */
        from("kafka:{{customer-topic}}?brokers={{broker}}")
                .routeId("CustomerStreamRoute")
                .log("recieved the message from customer CDC stream ${body} ")
                .setBody(jsonpath("$.payload.after.first_name"))
                .to("kafka:{{customer-firstname-topic}}?brokers={{broker}}")
                .to("amqp:queue:cdcaddress::customer_firstname")
                .log("message sent to the topic");

    }
}
