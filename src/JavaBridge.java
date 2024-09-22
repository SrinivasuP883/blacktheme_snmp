

public class JavaBridge {

    public MyApp myApp;

    public JavaBridge(MyApp myApp) {
        this.myApp = myApp;
    }

    public void authenticate(String username, String password) {
        myApp.authenticateUser(username, password);
    }

     public void performSnmpGet(String community, String ipAddress, String oids) {
        myApp.performSnmpGet(community, ipAddress, oids);
    }

    
    public void performSnmpSet(String setCommunity, String setIpAddress, String setOid, String setValue) {
        myApp.performSnmpSet(setCommunity, setIpAddress, setOid, setValue);
    }

    // public void performIpSet(String ipAddress, String subnetMask, String gateway, String interfaceName) {
    //     myApp.performIpSet(ipAddress, subnetMask, gateway, interfaceName);
    // }

    public void goBack() {
        myApp.loadLoginPage();
    }
   
}
