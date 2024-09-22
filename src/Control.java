
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Control {

    private static final String RESOURCE_FOLDER = "resources/";
    private static final String READ_FILE_PATH = RESOURCE_FOLDER + "read.txt";
    private static final String WRITE_FILE_PATH = RESOURCE_FOLDER + "write.txt";
    private static final String SNMP_ADDRESS = "127.0.0.1/161"; // Replace with your SNMP agent address
    private static final String COMMUNITY = "public"; // Replace with your community string
    private static final int INTERVAL_SECONDS = 5; // Interval in seconds

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    public void startPeriodicTask() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            // System.out.println("Starting execution at: " + System.currentTimeMillis());
            try {
                executeControlTask();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // System.out.println("Finished execution at: " + System.currentTimeMillis());
        };
        scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
        // System.out.println("Scheduled task to run every " + INTERVAL_SECONDS + " seconds.");
    }

    public void stopPeriodicTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }

    public void executeControlTask() throws Exception {
        // System.out.println("Executing control task at: " + System.currentTimeMillis());
        try {
            // Read OIDs from the file
            List<String> oids = readOIDsFromFile(READ_FILE_PATH);

            // Perform SNMP GET and write results
            performSnmpGetAndWriteResults(oids, WRITE_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readOIDsFromFile(String filePath) throws IOException {
        List<String> oids = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                oids.add(line.trim());
            }
        }
        return oids;
    }

    private void performSnmpGetAndWriteResults(List<String> oids, String writeFilePath) throws Exception {
        // Create SNMP4J objects
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);

        // Define the target
        CommunityTarget<UdpAddress> target = new CommunityTarget<>();
        target.setCommunity(new OctetString(COMMUNITY));
        target.setAddress(new UdpAddress(SNMP_ADDRESS)); // Correct address format
        target.setRetries(2);
        target.setTimeout(1000);
        target.setVersion(SnmpConstants.version2c);

        // Start the transport
        transport.listen();

        // Prepare to write results
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(writeFilePath))) {
            for (String oidStr : oids) {
                OID oid = new OID(oidStr);
                PDU pdu = new PDU();
                pdu.add(new VariableBinding(oid));
                pdu.setType(PDU.GET);

                // Send the PDU
                ResponseEvent response = snmp.get(pdu, target);

                // Process the response
                if (response != null && response.getResponse() != null) {
                    PDU responsePDU = response.getResponse();
                    String result = responsePDU.get(0).getVariable().toString();
                    writer.write(oidStr + " : " + result);
                    writer.newLine();
                } else {
                    writer.write(oidStr + " : No Response");
                    writer.newLine();
                }
            }
        } finally {
            snmp.close();
        }
    }
}
