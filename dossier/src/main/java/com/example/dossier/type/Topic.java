package com.example.dossier.type;

public enum Topic {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    STATEMENT_DENIED("statement-denied");

    private final String description;

    Topic(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
