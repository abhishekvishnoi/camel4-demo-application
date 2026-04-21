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

        rest("/customer").description("Hello REST service")
                .put().type(Customer.class)
                    .consumes("application/json")
                    .produces("application/json")
                    .to("direct:update-customer")
                .get().type(Customer.class)
                    .to("direct:get-customer");

        from("direct:update-customer")
                .routeId("direct-update-customer")
                .process(new Processor()

                    {@Override public void process(Exchange exchange)throws Exception {
                            exchange.getIn().setHeader("id", exchange.getIn().getHeader("id"));
                     }})
                .to("sql:UPDATE customers SET first_name=:#${body.first_name}, email=:#${body.email} WHERE id=:#${body.id}")
                .log("Updated the Database for the customer table .")
                .transform().constant("Hello World1");

        from("direct:get-customer")
                .log("Requesting customer details list")
                .to("sql:SELECT * FROM customers");

    }
}
