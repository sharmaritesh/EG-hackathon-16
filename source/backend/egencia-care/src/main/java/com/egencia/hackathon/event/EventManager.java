package com.egencia.hackathon.event;

/**
 * Created by gurssingh on 12/12/16.
 */

import com.egencia.hackathon.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class EventManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);
    private boolean started = false;
    private BlockingQueue<Alert> blockingQueue;
    private List<Worker> workers;
    /**
     * The default buffer size.
     */
    int queueSize = EventConstants.DEFAULT_QUEUE_SIZE;

    public EventManager() {

    }

    public EventManager(Environment environment) {
        init(); // This is added when object is not created by spring
    }

    @PostConstruct
    void init() {
        started = start();
//        if(started) {
//            this.configureDefaultHandlers();
//        }
    }

    public void shutdown() {
        if (!isStarted())
            return;

        stop();

        for(EventManager.Worker worker: workers) {
            // interrupt the worker thread so that it can terminate
            worker.interrupt();
        }
    }

    private void stop() {
        started = false;
    }

    private boolean isStarted() {
        return started;
    }

    private boolean start() {
        if (queueSize < 1) {
            LOGGER.error("Invalid queue size queueSize={}", queueSize);
            return false;
        }
        blockingQueue = new ArrayBlockingQueue<Alert>(queueSize);

        this.startWorkers();
        return true;
    }

    /**
     * This method is responsible to start the message consumers.<br>
     * By default it starts 1 message consumers. <br>
     * This can be configured using a property <b>audit.number.of.consumers</b>
     */
    private void startWorkers() {
        int no = EventConstants.DEFAULT_NUMBER_OF_CONSUMERS;
        workers = new ArrayList<EventManager.Worker>();
        for(int i=1; i<=no; i++) {
            Worker worker = new Worker();
            worker.setDaemon(true);
            worker.setName("AsyncMessageConsumer-Worker-" + i + "-" + worker.getName());
            worker.start();
            workers.add(worker);
        }
    }

    protected void append(Alert msg) {
        try {
            blockingQueue.put(msg);
        } catch (InterruptedException e) {
            LOGGER.warn("Unable to put data for analytics errorReason={} ", e.getMessage());
        }
    }

    class Worker extends Thread {

        /**
         * This method is responsible for running all the message handlers. <br>
         * First it will run all the default handlers - if configured - these
         * are run for all the payloads <br>
         * Then it will run all the custom handlers - binded per message or
         * payload
         *
         * @param event
         * @return {@link Alert}
         */
        private Alert processHandlers(Alert event) {
            event = this.processCustomHandlers(event);
            return event;
        }

        /**
         * This method is responsible for running all the custom handlers, if
         * any.
         *
         * @param event
         * @return {@link Alert}
         */
        private Alert processCustomHandlers(Alert event) {
            //TODO
            return event;
        }

        public void run() {

            while (isStarted()) {
                try {
                    Alert event = blockingQueue.take();
                    if(event == null) {
                        continue;
                    }
                    Alert msgTuple = this.processHandlers(event);
                    //TODO //add implementation of notification.

                } catch (InterruptedException ie) {
                    break;
                }
                catch(Exception exception)
                {
                    //Added exception catch block so that consumer threads don't die in case of any unexpected errors
                    LOGGER.warn(exception.getMessage());
                }
            }
        }
    }
}

