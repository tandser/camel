package ru.tandser.camel;

import org.apache.camel.builder.RouteBuilder;

public class RouterConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        onException(InvalidFileExtension.class).continued(true);

        from("file:data?noop=true&recursive=true").transacted().choice()
                .when(header("CamelFileName").endsWith(".xml")).to("jms:xml-queue")
                .when(header("CamelFileName").endsWith(".txt")).to("jms:txt-queue")
                                                               .convertBodyTo(String.class)
                                                               .setProperty("name", header("CamelFileName"))
                                                               .setProperty("body", body())
                                                               .to("sql:INSERT INTO txt (name, body) VALUES (:#${property.name}, :#${property.body})?dataSource=#dataSource")
                .otherwise().throwException(InvalidFileExtension.class, null)
                            .to("jms:invalid-queue");

        from("file:reports?noop=true")
                .setHeader("to",      constant("admin@localhost"))
                .setHeader("from",    constant("statistics@localhost"))
                .setHeader("subject", constant("report"))
                .to("smtp://statistics@localhost?password=statistics");
    }
}