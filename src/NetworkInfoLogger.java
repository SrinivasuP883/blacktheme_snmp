import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkInfoLogger {

    private static final String FILE_PATH = "network_info.txt";
    private static final int INTERVAL_SECONDS = 10; // Interval for periodic tasks

    private ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        NetworkInfoLogger networkInfoLogger = new NetworkInfoLogger();
        networkInfoLogger.startPeriodicTask();

        // Add a shutdown hook to stop the periodic task when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(networkInfoLogger::stopPeriodicTask));
    }

    public void startPeriodicTask() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            try {
                writeNetworkInfo();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
        System.out.println("Scheduled task to run every " + INTERVAL_SECONDS + " seconds.");
    }

    public void stopPeriodicTask() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        System.out.println("Scheduled task stopped.");
    }

    private void writeNetworkInfo() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Network Interfaces:\n");
            writer.newLine();

            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();

                    // Check if the interface is up and not a loopback
                    if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                        writer.write("Interface: " + networkInterface.getDisplayName());
                        writer.newLine();

                        // Get and list IP addresses for the interface
                        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddress = inetAddresses.nextElement();
                            writer.write("IP Address: " + inetAddress.getHostAddress());
                            writer.newLine();
                        }

                        // Example subnet mask (this could be more dynamically determined)
                        writer.write("Subnet Mask: " + getSubnetMask(24)); // Example subnet mask
                        writer.newLine();

                        // Example static gateway (this should ideally be dynamically determined)
                        String gateway = "192.168.0.1"; 
                        writer.write("Gateway: " + gateway);
                        writer.newLine();
                        writer.newLine(); // Extra line for spacing
                    }
                }
            } catch (SocketException e) {
                writer.write("Error retrieving network interfaces: " + e.getMessage());
                writer.newLine();
            }
        }
    }

    private String getSubnetMask(int prefixLength) {
        int mask = 0xFFFFFFFF << (32 - prefixLength);
        return (mask >> 24 & 0xFF) + "." + (mask >> 16 & 0xFF) + "." + (mask >> 8 & 0xFF) + "." + (mask & 0xFF);
    }
}
