import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("timer:foo?period=5000")
                .log("Hello World");

    }
}
