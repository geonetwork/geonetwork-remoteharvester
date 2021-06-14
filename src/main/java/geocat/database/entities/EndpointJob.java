package geocat.database.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


//create table endpoint_job ( endpoint_job_id varchar(40), harvest_job_id varchar(40), state varchar(40), url varchar(255), url_get_records varchar(255), expected_number_of_records int, filter text,look_for_nested_discovery_service bool);
@Entity
@Table(name="endpoint_job",
    indexes= {
        @Index(
                name="harvestJobId_idx",
                columnList="harvestJobId",
                unique=false
        )
    })

public class EndpointJob {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long endpointJobId;
    @Column(columnDefinition = "varchar(40)")
    private String harvestJobId;
    private String url;
    private boolean lookForNestedDiscoveryService;
    @Column(columnDefinition = "text")
    private String filter;
    private Integer expectedNumberOfRecords;
    private String urlGetRecords;
    @Enumerated(EnumType.STRING)
    private EndpointJobState state;

    @Column(columnDefinition = "timestamp with time zone")
    ZonedDateTime createTimeUTC;
    @Column(columnDefinition = "timestamp with time zone")
    ZonedDateTime lastUpdateUTC;


    @PrePersist
    private void onInsert() {
        this.createTimeUTC =  ZonedDateTime.now(ZoneId.of("UTC"));
        this.lastUpdateUTC = this.createTimeUTC;
    }

    @PreUpdate
    private void onUpdate() {
        this.lastUpdateUTC = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public ZonedDateTime getCreateTimeUTC() {
        return createTimeUTC;
    }

    public ZonedDateTime getLastUpdateUTC() {
        return lastUpdateUTC;
    }


    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getExpectedNumberOfRecords() {
        return expectedNumberOfRecords;
    }

    public void setExpectedNumberOfRecords(int expectedNumberOfRecords) {
        this.expectedNumberOfRecords = expectedNumberOfRecords;
    }

    public String getUrlGetRecords() {
        return urlGetRecords;
    }

    public void setUrlGetRecords(String urlGetRecords) {
        this.urlGetRecords = urlGetRecords;
    }

    public EndpointJobState getState() {
        return state;
    }

    public void setState(EndpointJobState state) {
        this.state = state;
    }

    public long getEndpointJobId() {
        return endpointJobId;
    }

    public void setEndpointJobId(long endpointJobId) {
        this.endpointJobId = endpointJobId;
    }

    public String getHarvestJobId() {
        return harvestJobId;
    }

    public void setHarvestJobId(String harvestJobId) {
        this.harvestJobId = harvestJobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
