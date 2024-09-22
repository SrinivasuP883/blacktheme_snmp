// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.IOException;

// public class NetworkInfoTest {

//     private static final String FILE_PATH = "windows.txt";

//     public static void main(String[] args) {
//         try {
//             writeSimulatedNetworkInfo();
//             System.out.println("Network information written to " + FILE_PATH);
//         } catch (IOException e) {
//             System.err.println("Error writing to file: " + e.getMessage());
//         }
//     }

//     private static void writeSimulatedNetworkInfo() throws IOException {
//         String os = System.getProperty("os.name").toLowerCase();

//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
//             // Simulated network interfaces and data
//             if (os.contains("win")) {
//                 // Simulated for Windows
//                 String[] simulatedInterfaces = {
//                     "Realtek RTL8821CE 802.11ac PCIe Adapter",
//                     "Ethernet",
//                     "Wi-Fi",
//                     "Loopback",
//                     "Bluetooth",
//                     "VPN"
//                 };
//                 String[] simulatedIps = {
//                     "192.168.0.103",
//                     "192.168.1.2",
//                     "192.168.1.3",
//                     "127.0.0.1",
//                     "192.168.1.4",
//                     "192.168.1.5"
//                 };
//                 String simulatedGateway = "192.168.0.1"; // Adjusted for new IP range

//                 for (int i = 0; i < simulatedInterfaces.length; i++) {
//                     writer.write("Interface: " + simulatedInterfaces[i]);
//                     writer.newLine();
//                     writer.write("IP Address: " + simulatedIps[i]);
//                     writer.newLine();
//                     writer.write("Subnet Mask: " + getSubnetMask(24)); // Example subnet mask
//                     writer.newLine();
//                     writer.write("Gateway: " + (simulatedInterfaces[i].equals("Loopback") ? "N/A" : simulatedGateway));
//                     writer.newLine();
//                     writer.newLine(); // Extra line for spacing
//                 }

//             } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
//                 // Simulated for Linux/Unix/Mac
//                 String[] simulatedInterfaces = {"enp19s0", "wlan0", "lo", "eth1", "wlan1"};
//                 String[] simulatedIps = {"192.168.1.2", "192.168.1.3", "127.0.0.1", "192.168.1.4", "192.168.1.5"};
//                 String simulatedGateway = "192.168.1.1";

//                 for (int i = 0; i < simulatedInterfaces.length; i++) {
//                     writer.write("Interface: " + simulatedInterfaces[i]);
//                     writer.newLine();
//                     writer.write("IP Address: " + simulatedIps[i]);
//                     writer.newLine();
//                     writer.write("Subnet Mask: " + getSubnetMask(24)); // Example subnet mask
//                     writer.newLine();
//                     writer.write("Gateway: " + (simulatedInterfaces[i].equals("lo") ? "N/A" : simulatedGateway));
//                     writer.newLine();
//                     writer.newLine(); // Extra line for spacing
//                 }
//             } else {
//                 writer.write("Unsupported operating system: " + os);
//                 writer.newLine();
//             }
//         }
//     }

//     private static String getSubnetMask(int prefixLength) {
//         int mask = 0xFFFFFFFF << (32 - prefixLength);
//         return (mask >> 24 & 0xFF) + "." + (mask >> 16 & 0xFF) + "." + (mask >> 8 & 0xFF) + "." + (mask & 0xFF);
//     }
// }


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkInfoTest {

    private static final String FILE_PATH = "resources/ip_data.txt";
    private static final int INTERVAL_SECONDS = 10; // Interval for periodic tasks
    private ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        NetworkInfoTest networkInfo = new NetworkInfoTest();
        networkInfo.startPeriodicTask();

        // Add a shutdown hook to stop the periodic task when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            networkInfo.stopPeriodicTask();
        }));
    }

    public void startPeriodicTask() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            try {
                writeSimulatedNetworkInfo();
                System.out.println("Network information written to " + FILE_PATH);
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

    private void writeSimulatedNetworkInfo() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Simulated network interfaces and data
            if (os.contains("win")) {
                String[] simulatedInterfaces = {
                    "Realtek RTL8821CE 802.11ac PCIe Adapter",
                    "Ethernet"
                    // "Wi-Fi",
                    // "Loopback",
                    // "Bluetooth",
                    // "VPN"
                };
                String[] simulatedIps = {
                    "192.168.0.103",
                    "192.168.1.2"
                    // "192.168.1.3",
                    // "127.0.0.1",
                    // "192.168.1.4",
                    // "192.168.1.5"
                };
                String simulatedGateway = "192.168.0.1"; // Adjusted for new IP range

                for (int i = 0; i < simulatedInterfaces.length; i++) {
                    writer.write("Interface: " + simulatedInterfaces[i]);
                    writer.newLine();
                    writer.write("IP Address: " + simulatedIps[i]);
                    writer.newLine();
                    writer.write("Subnet Mask: " + getSubnetMask(24)); // Example subnet mask
                    writer.newLine();
                    writer.write("Gateway: " + (simulatedInterfaces[i].equals("Loopback") ? "N/A" : simulatedGateway));
                    writer.newLine();
                    writer.newLine(); // Extra line for spacing
                }

            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                String[] simulatedInterfaces = {"enp19s0", "wlan0", "lo", "eth1", "wlan1"};
                String[] simulatedIps = {"192.168.1.2", "192.168.1.3", "127.0.0.1", "192.168.1.4", "192.168.1.5"};
                String simulatedGateway = "192.168.1.1";

                for (int i = 0; i < simulatedInterfaces.length; i++) {
                    writer.write("Interface: " + simulatedInterfaces[i]);
                    writer.newLine();
                    writer.write("IP Address: " + simulatedIps[i]);
                    writer.newLine();
                    writer.write("Subnet Mask: " + getSubnetMask(24)); // Example subnet mask
                    writer.newLine();
                    writer.write("Gateway: " + (simulatedInterfaces[i].equals("lo") ? "N/A" : simulatedGateway));
                    writer.newLine();
                    writer.newLine(); // Extra line for spacing
                }
            } else {
                writer.write("Unsupported operating system: " + os);
                writer.newLine();
            }
        }
    }

    private String getSubnetMask(int prefixLength) {
        int mask = 0xFFFFFFFF << (32 - prefixLength);
        return (mask >> 24 & 0xFF) + "." + (mask >> 16 & 0xFF) + "." + (mask >> 8 & 0xFF) + "." + (mask & 0xFF);
    }
}
