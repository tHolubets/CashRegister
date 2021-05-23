package com.studing.cashRegister.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.*;
import javax.servlet.annotation.*;
/**
 * Listener for attribute changes logging
 * @author tHolubets
 */
@WebListener
public class AttributeListener implements HttpSessionAttributeListener {
    private static final Logger logger = LoggerFactory.getLogger(AttributeListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        logger.debug("Attribute added = {}({})", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        logger.debug("Attribute removed = {}({})", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        logger.debug("Attribute replaced = {}({})", event.getName(), event.getValue());
    }
}
