package geocat.eventprocessor.processors.determinework;

import geocat.csw.CSWMetadata;
import geocat.csw.CSWService;
import geocat.database.service.DatabaseUpdateService;
import geocat.eventprocessor.BaseEventProcessor;
import geocat.events.Event;
import geocat.events.EventFactory;
import geocat.events.determinework.CSWEndPointDetectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class EventProcessor_CSWEndPointDetectedEvent extends BaseEventProcessor<CSWEndPointDetectedEvent>
{

    @Autowired
    CSWService cswService;

    @Autowired
    DatabaseUpdateService databaseUpdateService;

    @Autowired
    EventFactory eventFactory;

    CSWMetadata result;
    List<CSWEndPointDetectedEvent> newEndpoints;

    @Override
    public EventProcessor_CSWEndPointDetectedEvent internalProcessing(){
        newEndpoints =  databaseUpdateService.updateDatabase( result );
        return this;
    }

    @Override
    public EventProcessor_CSWEndPointDetectedEvent externalProcessing() throws Exception {
        result = cswService.getMetadata( getInitiatingEvent());
        return this;
    }

    @Override
    public List<Event> newEventProcessing(){
        List<Event> result = new ArrayList<>();
        if ( (newEndpoints != null) && (!newEndpoints.isEmpty()) )
            result.addAll( newEndpoints);
        else {
            Event e = eventFactory.create_CSWEndpointWorkDetermined( getInitiatingEvent().getHarvesterId(),
                    getInitiatingEvent().getEndPointId());
            result.add(e);
        }
        return result;
    }

}