import java.util.Map;

public abstract class IF_MessageBroker {
    protected static Map<SysSuper_Component.Sys, SysSuper_Component> components;

    public static void routeMessage(SysSuper_Component.Message message){
        SysSuper_Component supComponent = components.get(message.destination());
        if (supComponent != null)
            supComponent.QUEUE.add(message);
    }
}
