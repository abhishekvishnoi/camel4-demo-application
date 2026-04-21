import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

//@ApplicationScoped
public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

       /* restConfiguration()
                .bindingMode(RestBindingMode.json);

        // @formatter:off
        rest("/hello").description("Hello REST service")
                .consumes("application/json")
                .produces("application/json")

                .get().description("Find all todos").outType(String.class)
                .responseMessage().code(200)
                .message("All hellos successfully returned")
                .endResponseMessage()
                .to("direct:sayHello");


        from("direct:sayHello").log("Hellow World!!");*/

    }
}
