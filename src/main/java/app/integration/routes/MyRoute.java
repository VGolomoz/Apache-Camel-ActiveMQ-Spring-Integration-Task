package app.integration.routes;

import app.integration.dto.Catalog;
import app.integration.dto.ObjectFactory;
import app.integration.entities.MsgBody;
import app.integration.entities.MsgHeaders;
import app.integration.exceptions.UndefinedFileTypeException;
import app.integration.repositories.MsgBodyRepository;
import app.integration.utility.interfaces.IncrementorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * Class creates routing rules for reading files from specific directory followed by sending to message broker and
 * processing them, counting and sending a mail with a report
 *
 * @author Vadym
 */
@Slf4j
@Component
public class MyRoute extends RouteBuilder {

    /**
     * Directory where files are read from.
     */
    @Value("${task.src.dir}")
    private String srcDir;
    /**
     * Message broker queue for text files
     */
    @Value("${activemq.txt.queue}")
    private String txtQueue;
    /**
     * Message broker queue for xml files
     */
    @Value("${activemq.xml.queue}")
    private String xmlQueue;
    /**
     * Message broker queue for undefined files
     */
    @Value("${activemq.error.queue}")
    private String errorQueue;
    /**
     * Mail host for sending report
     */
    @Value("${task.mail.host}")
    private String mailHost;
    /**
     * Mail port for sending report
     */
    @Value("${task.mail.port}")
    private String mailPort;
    /**
     * Sender's email for sending report
     */
    @Value("${task.mail.login}")
    private String mailLogin;
    /**
     * Sender's password for sending report
     */
    @Value("${task.mail.password}")
    private String mailPassword;
    /**
     * Destination email address for sending report
     */
    @Value("${task.mail.destination}")
    private String mailDestination;
    /**
     * Value of processed messages before sending a report
     */
    @Value("${task.messages.limit}")
    private Integer messageLimit;

    /**
     * Service for counting processed files
     */
    private IncrementorService incrementorService;
    /**
     * Repository for saving processed message body to database
     */
    private MsgBodyRepository msgBodyRepository;


    /**
     * Constructs a new instance of class and fill required dependencies
     */
    @Autowired
    public MyRoute(IncrementorService incrementorService, MsgBodyRepository msgBodyRepository) {
        this.incrementorService = incrementorService;
        this.msgBodyRepository = msgBodyRepository;

        CamelContext context = new DefaultCamelContext();
        context.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"));
    }

    /**
     * Called on initialization to build the routes
     */
    @Override
    public void configure() {
        produceQueues();
        consumeTextQueue();
        consumeXmlQueue();
    }


    /**
     * Read files from specified directory and depending on the type of file it processing and send to the appropriate
     * queue. Count amount of processed files by {@link IncrementorService} and depending on messages amount send mail
     * report, write logs
     */
    private void produceQueues() {
        JaxbDataFormat jaxb = new JaxbDataFormat(ObjectFactory.class.getPackage().getName());
        Predicate txtPredicate = PredicateBuilder.and(simple("${file:name.ext} == 'txt'"));
        Predicate xmlPredicate = PredicateBuilder.and(simple("${file:name.ext} == 'xml'"));
        String smtpAddress = "smtp://" + mailHost + ":" + mailPort + "?username=" + mailLogin + "&password=" +
                mailPassword + "&mail.smtp.auth=true";

        from(srcDir + "?autoCreate=true&move=.done&readLock=markerFile")
                .onException(UndefinedFileTypeException.class).handled(true).to(errorQueue)
                .log(LoggingLevel.DEBUG, "Exception undefined type of file: ${file:name}")
                .end()
                .choice()
                    .when(txtPredicate)
                        .bean(incrementorService, "incrementTxtCount()")
                        .to(txtQueue)
                    .when(xmlPredicate)
                        .bean(incrementorService, "incrementXmlCount()")
                        .unmarshal(jaxb)
                        .process(exchange -> {
                            Catalog catalog = (Catalog) exchange.getIn().getBody();
                            log.debug("Mapping dto from xml: " + catalog.toString());
                        })
                        .to(xmlQueue)
                    .otherwise()
                        .bean(incrementorService, "incrementUndefinedCount()")
                        .throwException(new UndefinedFileTypeException())
                .end()
                .to("direct:checkOnMsgLimit");

        from("direct:checkOnMsgLimit")
                .choice()
                    .when(method(incrementorService, "getTotalCounterValue()").isGreaterThanOrEqualTo(messageLimit))
                        .setHeader("destination").simple(mailDestination)
                        .setHeader("sender").simple(mailLogin)
                        .setHeader("subject").simple("Was processed 100 messages")
                        .setBody().method(incrementorService, "getCountersValues()")
                        .to(smtpAddress)
                        .log(LoggingLevel.DEBUG, incrementorService.getCountersValues())
                        .bean(incrementorService, "resetCounters()")
                .end();
    }

    /**
     * Read text files from message broker queue, logging received body message to specific file and directory
     */
    private void consumeTextQueue(){
        from(txtQueue)
                .log(LoggingLevel.DEBUG, txtQueue + " : received message body: ${body}")
                .log(LoggingLevel.INFO, "txtFilesLog", txtQueue + " : received message body: ${body}");
    }

    /**
     * Read xml files from message broker queue, save received body message and headers to database
     */
    private void consumeXmlQueue(){
        from(xmlQueue)
                .process(exchange -> {
                    MsgHeaders msgHeaders = new MsgHeaders();
                    msgHeaders.setValue(exchange.getMessage().getHeaders().toString());
                    msgHeaders.setEntryTime(Calendar.getInstance());
                    MsgBody msgBody = new MsgBody();
                    msgBody.setValue(exchange.getMessage().getBody(String.class));
                    msgBody.setMsgHeaders(msgHeaders);
                    msgBodyRepository.save(msgBody);
                    log.debug(xmlQueue + " : Save received message body and headers to database");
                });
    }
}
