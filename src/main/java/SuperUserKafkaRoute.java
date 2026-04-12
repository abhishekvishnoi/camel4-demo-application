import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ApplicationScoped
public class SuperUserKafkaRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {

        from("timer://foo?fixedRate=true&period=60000")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        try {
                            String hostname = InetAddress.getLocalHost().getHostName();
                            System.out.println("Machine Name: " + hostname);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setBody(simple("hello kafka!!"))
                .to("direct:hello-kafka");


        from("direct:hello-kafka")
                .routeId("KafkaGreetingRoute")
                .log("hello")
                .to("kafka:{{topic}}?brokers={{broker}}")
                .log("message sent to the topic");

        // Kafka Consumer
        from("kafka:{{topic}}?brokers={{broker}}")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}");


    }



}
