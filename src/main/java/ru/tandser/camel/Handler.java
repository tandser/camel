package ru.tandser.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Handler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void handleXml(File file) {
        logger.info(".handleXml({})", file.getName());
    }

    public void handleTxt(File file) {
        logger.info(".handleTxt({})", file.getName());
    }

    public void handleAny(File file) {
        logger.info(".handleAny({})", file.getName());
    }
}