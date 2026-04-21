import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import pojo.Customer;

@ApplicationScoped
public class CustomerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json);

        // @formatter:off
        rest("/customer").description("Hello REST service")
                .consumes("application/json")
                .produces("application/json")
                .put().type(Customer.class)
                .to("direct:update-customer");

        from("direct:update-customer")
                .routeId("direct-update-customer")
                .process(new Processor()

                    {@Override public void process(Exchange exchange)throws Exception {
                            exchange.getIn().setHeader("id", exchange.getIn().getHeader("id"));
                     }})
                .to("sql:UPDATE customers SET first_name=:#${body.first_name}, email=:#${body.email} WHERE id=:#${body.id}")
                .transform().constant("Hello World1");

    }
}
