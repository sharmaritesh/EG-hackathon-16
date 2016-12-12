package com.egencia.hackathon.event;

/**
 * Created by gurssingh on 12/12/16.
 */

import com.egencia.hackathon.model.Alert;
import com.egencia.hackathon.model.AlertReply;
import com.egencia.hackathon.service.AlertHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BlockingQueue<AlertReply> alertReplyQueue;
    private List<Thread> workers;
    @Autowired
    private AlertHandler alertHandler;
    /**
     * The default buffer size.
     */
    int queueSize = EventConstants.DEFAULT_QUEUE_SIZE;

    public EventManager() {
        init();
    }

    @PostConstruct
    void init() {
        started = start();
    }

    public void shutdown() {
        if (!isStarted())
            return;

        stop();

        for(Thread worker: workers) {
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
        alertReplyQueue = new ArrayBlockingQueue<AlertReply>(queueSize);
        this.startWorkers();
        return true;
    }

    /**
     * This method is responsible to start the message consumers.<br>
     * By default it starts 1 message consumers. <br>
     * This can be configured using a property <b>audit.number.of.consumers</b>
     */
    private void startWorkers() {
        workers = new ArrayList<Thread>();
        Worker1 worker = new Worker1();
        worker.setDaemon(true);
        worker.setName("AsyncMessageConsumer-Worker-" + 1 + "-" + worker.getName());
        worker.start();
        workers.add(worker);
        Worker2 worker2 = new Worker2();
        worker2.setDaemon(true);
        worker2.setName("AsyncMessageConsumer-Worker-" + 2 + "-" + worker.getName());
        worker2.start();
        workers.add(worker2);
    }

    public void append(Alert alert) {
        try {
            blockingQueue.put(alert);
        } catch (InterruptedException e) {
            LOGGER.warn("Unable to put data alert notification data, errorReason={} ", e.getMessage());
        }
    }

    public void enqueueAlertReply(AlertReply alertReply) {
        try {
            alertReplyQueue.put(alertReply);
        } catch (InterruptedException e) {
            LOGGER.warn("Unable to put data for alert reply notification errorReason={} ", e.getMessage());
        }
    }

    class Worker1 extends Thread {

        public void run() {

            while (isStarted()) {
                try {
                    Alert alertEvent = blockingQueue.take();
                    if(alertEvent == null) {
                        continue;
                    }
                    alertHandler.handleAlert(alertEvent);
                } catch (InterruptedException ie) {
                    break;
                }
                catch(Exception exception) {
                    //Added exception catch block so that consumer threads don't die in case of any unexpected errors
                    LOGGER.warn(exception.getMessage());
                }
            }
        }
    }

    class Worker2 extends Thread {

        public void run() {

            while (isStarted()) {
                try {
                    AlertReply alertReplyEvent = alertReplyQueue.take();
                    if(alertReplyEvent == null) {
                        continue;
                    }
                    alertHandler.handleAlertReply(alertReplyEvent);
                } catch (InterruptedException ie) {
                    break;
                }
                catch(Exception exception) {
                    //Added exception catch block so that consumer threads don't die in case of any unexpected errors
                    LOGGER.warn(exception.getMessage());
                }
            }
        }
    }
}

