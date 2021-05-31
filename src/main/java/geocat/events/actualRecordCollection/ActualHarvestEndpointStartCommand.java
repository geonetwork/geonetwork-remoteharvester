package geocat.events.actualRecordCollection;

import geocat.events.Event;

public class ActualHarvestEndpointStartCommand extends Event {
    private String endPointId;
    private String harvesterId;
    private String getRecordsURL;
    private String filter;
    private int    nRecordPerRequest;
    private int    maxSimultaneousRequests;
    private int    expectedNumberOfRecords;

    public ActualHarvestEndpointStartCommand() {
    }


    public String getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(String endPointId) {
        this.endPointId = endPointId;
    }


    public String getHarvesterId() {
        return harvesterId;
    }

    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }


    public String getGetRecordsURL() {
        return getRecordsURL;
    }

    public void setGetRecordsURL(String getRecordsURL) {
        this.getRecordsURL = getRecordsURL;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getnRecordPerRequest() {
        return nRecordPerRequest;
    }

    public void setnRecordPerRequest(int nRecordPerRequest) {
        this.nRecordPerRequest = nRecordPerRequest;
    }

    public int getMaxSimultaneousRequests() {
        return maxSimultaneousRequests;
    }

    public void setMaxSimultaneousRequests(int maxSimultaneousRequests) {
        this.maxSimultaneousRequests = maxSimultaneousRequests;
    }


    public int getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }

    @Override
    public String toString(){
        return "ActualHarvestEndpointStartCommand with url="+getRecordsURL+", expected records ="+expectedNumberOfRecords;
    }


}
