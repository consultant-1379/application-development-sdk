
package com.ericsson.enm.sdk.dbpop.web;

import com.ericsson.enm.sdk.dbpop.service.DatabasePopulatorBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Resource for populate the database with extra mos.
 */
@WebListener
public class DbPopulator implements ServletContextListener {

    private Logger logger  = LoggerFactory.getLogger(DbPopulator.class);
    private Timer timer;

    @Inject
    private DatabasePopulatorBean dbPopulator;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing database populator");
        timer = new Timer();
        PopulateTimerTask task = new PopulateTimerTask();
        timer.schedule(task, 3000, 3000);
        logger.info("Populate task scheduled to run in 3 seg");
    }

    private void populateDatabase() {
        dbPopulator.createMoIfNotExists("OSS_NE_DEF", "NetworkElement", "RNC01", "RNC");
        dbPopulator.createMoIfNotExists("OSS_NE_DEF", "NetworkElement", "RNC02", "RNC");
        dbPopulator.createMoIfNotExists("OSS_NE_DEF", "NetworkElement", "MyNode01", "ERBS");
        dbPopulator.createMoIfNotExists("OSS_NE_DEF", "NetworkElement", "MyRadioNode", "RadioNode");
        dbPopulator.createMoIfNotExists("OSS_NE_DEF", "NetworkElement", "MGW01", "MGW");
    }

    class PopulateTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                logger.info("Populating the database with sample record");
                populateDatabase();
                logger.info("Database ready");
                timer.cancel();
            } catch (Exception ex) {
                logger.error("Cannot populate database now. Will retry in 3 sec. Reason: " + ex.getCause().getMessage());
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
