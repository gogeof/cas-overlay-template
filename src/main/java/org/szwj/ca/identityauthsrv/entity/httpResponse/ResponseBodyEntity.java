package org.szwj.ca.identityauthsrv.entity.httpResponse;

import sglca.helper.models.ResponseBody;

public class ResponseBodyEntity extends ResponseBody {

    public EventValueEntity eventValue;

    public EventValueEntity getEventValueEntity() {
        return eventValue;
    }

    public void setEventValueEntity(EventValueEntity eventValue) {
        this.eventValue = eventValue;
    }
}
