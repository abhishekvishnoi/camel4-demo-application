import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.amqp.AMQPComponent;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ApplicationScoped
public class SuperUserKafkaRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {

       // AMQPComponent authorizedAmqp =
         //       AMQPComponent.amqpComponent("amqp://ex-aao-amqp-0-svc.artemis.svc.cluster.local:5762", "VUdSWL8u", "FsimPzVG");


        from("timer://foo?fixedRate=true&period=600000")
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

                .to("direct:hello-kafka")
                .to("direct:hello-artemis");


        from("direct:hello-artemis")
                .routeId("ArtemisGreetingRoute")
                .log("hello artmis ")
                .setBody(simple("hello artemis!!"))
                .to("amqp:queue:myaddress::myqueue")
                .log("message sent to the artemis queue");


        from("direct:hello-kafka")
                .routeId("KafkaGreetingRoute")
                .log("hello Kafka")
                .setBody(simple("hello kafka!!"))
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
