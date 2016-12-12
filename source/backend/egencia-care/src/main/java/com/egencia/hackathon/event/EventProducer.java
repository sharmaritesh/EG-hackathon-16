package com.egencia.hackathon.event;

/**
 * Created by gurssingh on 12/12/16.
 */

import com.egencia.hackathon.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class EventProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventProducer.class);
    private static EventProducer eventProducer;
    private BlockingQueue<Alert> queue;

    private EventProducer(BlockingQueue<Alert> queue) {
        this.queue = queue;
    }

    /**
     * Exposes the instance method
     * @param queue
     * @return
     */
    public static EventProducer getInstance(BlockingQueue<Alert> queue) {
        synchronized (EventManager.class) {
            if (eventProducer == null) {
                eventProducer = new EventProducer(queue);
            }
        }
        return eventProducer;
    }

    public void publish(Alert message) {
        this.putInQueue(message);
    }

    void putInQueue(Alert payload) {
        try {
            queue.put(payload);
        } catch (InterruptedException e) {
            LOGGER.warn("Unable to put data for analytics errorReason={} ", e.getMessage());
        }
    }
}

