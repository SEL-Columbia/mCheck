package org.who.formhub.listener;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

public class FormDispatchFailedException extends RuntimeException {
    private ArrayList<Throwable> innerExceptions;

    public FormDispatchFailedException() {
        innerExceptions = new ArrayList<Throwable>();
    }

    public void add(Throwable e) {
        innerExceptions().add(e);
    }

    public ArrayList<Throwable> innerExceptions() {
        return innerExceptions;
    }

    public boolean hasExceptions() {
        return innerExceptions.size() > 0;
    }

    @Override
    public String getMessage() {
        String message = "Number of failures: " + innerExceptions().size() + ". Failures: [\n";
        for (Throwable innerException : innerExceptions) {
            message += "Message: " + innerException.getMessage() + "\n";
            message += "Inner exception message: " + innerException.getCause().getMessage() + "\n";
            message += StringUtils.join(innerException.getStackTrace(), "\n    ") + "\n";
            message += StringUtils.join(innerException.getCause().getStackTrace(), "\n    ") + "\n=============\n";
        }
        message += "]";
        return message;
    }
}
