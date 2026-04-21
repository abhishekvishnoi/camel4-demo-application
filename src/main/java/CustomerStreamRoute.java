import org.apache.camel.builder.RouteBuilder;

//@ApplicationScoped
public class CustomerStreamRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("kafka:{{customer-topic}}?brokers={{broker}}")
                .routeId("CustomerStreamRoute")
                .log("recieved the message from customer CDC stream ${body} ")
                .setBody(jsonpath("$.payload.after.first_name"))
                .to("kafka:{{customer-firstname-topic}}?brokers={{broker}}")
                .log("message sent to the topic");

    }
}
