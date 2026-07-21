import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

//@ApplicationScoped
public class SampleExceptionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true) // Mark exception as handled (caller won't see the raw error)
                .maximumRedeliveries(2) // Retry twice before giving up
                .redeliveryDelay(1000) // Wait 1 second between retries
                .transform().constant("Sorry, random boolean exception generated.!")
                .to("log:errorLog");

        from("timer://foo?fixedRate=true&period=6000")
                .routeId("timerexceptionRoute")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        boolean randomBool = Math.random() < 0.25;

                        if(randomBool==true){
                            throw new Exception("random exception thrown.!!!");
                        }
                    }
                })
                .to("direct:hello-camel");

        from("direct:hello-kafka")
                .routeId("CamelExceptionRoute-1")
                .log("hello camel")
                .setBody(simple("hello kafka!!"))
                .to("kafka:hello-camel?brokers={{broker}}")
                .log("message sent to the topic");


    }
}
