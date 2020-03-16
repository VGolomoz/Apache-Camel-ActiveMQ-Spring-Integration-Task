package app.integration;

import app.integration.entities.MsgBody;
import app.integration.entities.MsgHeaders;
import app.integration.repositories.MsgBodyRepository;
import app.integration.repositories.MsgHeadersRepository;
import app.integration.utility.IncrementorServiceImpl;
import app.integration.utility.TxtFileIncrementor;
import app.integration.utility.UndefinedFileIncrementor;
import app.integration.utility.XmlFileIncrementor;
import app.integration.utility.interfaces.Incrementable;
import app.integration.utility.interfaces.IncrementorService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for inserting and reading entities into/from database.
 *
 * @author Vadym
 *
 */
@DataJpaTest
@RunWith(SpringRunner.class)
class IntegrationTaskApplicationTests {

    /**
     * Prepare test context before testing.
     */
    @TestConfiguration
    static class IncrementorServiceImplTestContextConfiguration {

        /**
         * Create TxtFileIncrementor bean.
         */
        @Bean
        public Incrementable txtIncrementor() {
            return new TxtFileIncrementor();
        }

        /**
         * Create XmlFileIncrementor bean.
         */
        @Bean
        public Incrementable xmlIncrementor() {
            return new XmlFileIncrementor();
        }

        /**
         * Create UndefinedFileIncrementor bean.
         */
        @Bean
        public Incrementable undefinedIncrementor() {
            return new UndefinedFileIncrementor();
        }

        /**
         * Create IncrementorServiceImpl bean with dependencies.
         */
        @Bean
        public IncrementorService incrementorService() {
            return new IncrementorServiceImpl(txtIncrementor(), xmlIncrementor(), undefinedIncrementor());
        }
    }

	/**
	 * Fill repository dependency for MsgBody Entity.
	 */
	@Autowired
	private MsgBodyRepository msgBodyRepository;
	/**
	 * Fill repository dependency for MsgHeaders Entity.
	 */
	@Autowired
	private MsgHeadersRepository msgHeadersRepository;
    /**
     * Fill dependency by IncrementorServiceImplTestContext.
     */
    @Autowired
    private IncrementorService incrementorService;


	/**
	 * Check successful inserting and reading rows from database tables.
	 */
    @Test
    public void saveMsgBodyAndMsgHeaders_thenReturn() {
        // given
        MsgHeaders msgHeaders = new MsgHeaders();
        msgHeaders.setValue("test msgHeaders Value");
        msgHeaders.setEntryTime(Calendar.getInstance());
        MsgBody msgBody = new MsgBody();
        msgBody.setValue("test msgBody Value");
        msgBody.setMsgHeaders(msgHeaders);
        msgBody = msgBodyRepository.save(msgBody);

        // when
        Optional<MsgBody> foundMsgBody = msgBodyRepository.findById(msgBody.getId());
        Optional<MsgHeaders> foundMsgHeaders = msgHeadersRepository.findById(msgBody.getMsgHeaders().getId());

        // then
        if (foundMsgBody.isPresent())
            assertThat(foundMsgBody.get()).isEqualTo(msgBody);
        if (foundMsgHeaders.isPresent())
            assertThat(foundMsgHeaders.get()).isEqualTo(msgHeaders);
    }

    /**
     * Check increment operation for each counter.
     */
    @Test
    public void checkFilesCountersIncrement() {
        //given
        int TotalCounterValueAfterIncrement = 3;
        String countersValuesAfterIncrement = "Was processed: txtFiles = 1, xmlFiles = 1, undefinedFiles = 1";

        // when
        incrementorService.incrementTxtCount();
        incrementorService.incrementXmlCount();
        incrementorService.incrementUndefinedCount();

        // then
        assertThat(incrementorService.getTotalCounterValue()).isEqualTo(TotalCounterValueAfterIncrement);
        assertThat(incrementorService.getCountersValues()).isEqualTo(countersValuesAfterIncrement);
    }

    /**
     * Check reset operation for each counter.
     */
    @Test
    public void checkFilesCountersReset() {
        //given
        int TotalCounterValueAfterReset = 0;
        String countersValuesWhenReset = "Was processed: txtFiles = 0, xmlFiles = 0, undefinedFiles = 0";

        // when
        incrementorService.resetCounters();

        // then
        assertThat(incrementorService.getTotalCounterValue()).isEqualTo(TotalCounterValueAfterReset);
        assertThat(incrementorService.getCountersValues()).isEqualTo(countersValuesWhenReset);

    }

}
