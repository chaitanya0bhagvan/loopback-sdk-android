package com.strongloop.android.remoting.adapters;

/**
 * Created by Chaitanya on 20-02-2015.
 */
public class LoopbackThrowable extends Throwable {
    private String responseBody;

    public LoopbackThrowable(String responseBody) {
        this.responseBody = responseBody;
    }

    public LoopbackThrowable(String detailMessage, String responseBody) {
        super(detailMessage);
        this.responseBody = responseBody;
    }

    public LoopbackThrowable(String detailMessage, String responseBody, Throwable cause) {
        super(detailMessage, cause);
        this.responseBody = responseBody;
    }

    public LoopbackThrowable(String responseBody, Throwable cause) {
        super(cause);
        this.responseBody = responseBody;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
