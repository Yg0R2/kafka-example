package yg0r2.external;

import yg0r2.kafka.domain.Request;

public interface RequestProcessor {

    void processRequest(Request request);

}
