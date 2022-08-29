import java.util.HashMap;
import java.util.Map;

/**
 * Eventually, this _PresentationDriver should instantiate and start all components,
 * and provide terminal control to simulate device/sensor activation
 * for all components.
 *
 * To do: Add all components.
 * To do: Add timed device simulations for token and car systems for presentation.
 */

public class _PresentationDriver {
    public static final boolean DEBUG = true;
    private static Map<SysSuper_Component.Sys, SysSuper_Component> components = new HashMap<>();
    private static Map<SysSuper_Component.Sys, SysSuper_Component.Device[]> devices = new HashMap<>();

    public static void main(String[] args) {
        //Token Monitoring System
        Sys_TokenMonitoring sysTokenMonitoring = new Sys_TokenMonitoring();
        components.put(SysSuper_Component.Sys.TOKEN, sysTokenMonitoring);
        //Sys_Exhibit System
        Sys_Exhibit sysExhibit = new Sys_Exhibit();
        components.put(SysSuper_Component.Sys.EXHIBIT, sysExhibit);
        //Self-Driving Sys_SelfDrivingCar.Car System
        Sys_SelfDrivingCar sysSelfDrivingCar = new Sys_SelfDrivingCar();
        components.put(SysSuper_Component.Sys.AUTO_CAR, sysSelfDrivingCar);
        // Controller Sys_Controller
        Sys_Controller sysController = new Sys_Controller();
        GUI_Controller guiController = new GUI_Controller();
        components.put(SysSuper_Component.Sys.CONTROLLER, sysController);
        Thread clientThread = new Thread(new Runnable(){
           public void run(){
               guiController.main(null, sysController);
           } 
        });
        clientThread.start();
        // ATS
        Sys_AutomatedTicket ats = new Sys_AutomatedTicket();
        components.put(SysSuper_Component.Sys.ATS, ats);
        // Sys_Oversight
        Sys_Oversight sysOversight = new Sys_Oversight();
        components.put(SysSuper_Component.Sys.OVERSIGHT, sysOversight) ;
         

        IF_MessageBroker.components = components;
        components.forEach((k,v) -> {
            v.run();
            devices.put(k, v.deviceArray);
        } );

        listDevices();
    }

    public static void listDevices() {
        System.out.println();
        devices.forEach((k, v) -> {
            if (v.length != 0) {
                System.out.println(k.getVAL() + " - " + k);
                for (int i = 0; i < v.length; i++) {
                    System.out.println("   " + i + " - " +v[i].name + ": " + v[i].state);
                }
            }
        } );
    }
}
