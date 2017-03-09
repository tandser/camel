package ru.tandser.camel;

import org.apache.camel.management.event.ExchangeSentEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;

public class CustomEventNotifier extends EventNotifierSupport {

    private static final String PATH_TO_REPORTS = "reports/";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int  xml;
    private int  txt;
    private int  other;
    private int  total;
    private long duration;

    @Override
    public void notify(EventObject event) throws Exception {
        if (event instanceof ExchangeSentEvent) {
            ExchangeSentEvent sentEvent = (ExchangeSentEvent) event;
            URI uri = URI.create(sentEvent.getEndpoint().getEndpointUri());
            String schema = uri.getScheme();
            if ("jms".equals(schema) || "sql".equals(schema)) {
                duration += sentEvent.getTimeTaken();
                String host = uri.getHost();
                if (host != null) {
                    if (host.startsWith("xml")) {
                        xml++;
                    } else if (host.startsWith("txt")) {
                        txt++;
                    } else {
                        other++;
                    }
                    if (++total % 10 == 0) {
                        report();
                        xml = txt = other = 0;
                    }
                }
            }
        }
    }

    @Override
    public boolean isEnabled(EventObject event) {
        return event instanceof ExchangeSentEvent;
    }

    private void report() throws Exception {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(PATH_TO_REPORTS + filename)))) {
            printWriter.println("xml      : " + xml);
            printWriter.println("txt      : " + txt);
            printWriter.println("other    : " + other);
            printWriter.println("duration : " + duration);
        }
    }
}