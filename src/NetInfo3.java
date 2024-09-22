

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.io.InputStreamReader;
// import java.net.InetAddress;
// import java.net.InterfaceAddress;
// import java.net.NetworkInterface;
// import java.net.SocketException;
// import java.util.ArrayList;
// import java.util.Enumeration;
// import java.util.List;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.ScheduledFuture;
// import java.util.concurrent.TimeUnit;

// public class NetInfo3 {

//     private static final String FILE_PATH = "resources/ip_data.txt";
//     private static final int INTERVAL_SECONDS = 10; // Interval for periodic tasks

//     private ScheduledExecutorService scheduler;
//     private ScheduledFuture<?> scheduledFuture;

//     public static void main(String[] args) {
//         NetInfo3 netInfo = new NetInfo3();
//         netInfo.startPeriodicTask();

//         // Add a shutdown hook to stop the periodic task when the application exits
//         Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//             netInfo.stopPeriodicTask();
//         }));
//     }

//     public void startPeriodicTask() {
//         scheduler = Executors.newScheduledThreadPool(1);
//         Runnable task = () -> {
//             try {
//                 writeNetworkInfoToFile();
//                 writeTrapIpToFile();
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         };
//         scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, INTERVAL_SECONDS, TimeUnit.SECONDS);
//         System.out.println("Scheduled task to run every " + INTERVAL_SECONDS + " seconds.");
//     }

//     public void stopPeriodicTask() {
//         if (scheduledFuture != null) {
//             scheduledFuture.cancel(true);
//         }
//         if (scheduler != null) {
//             scheduler.shutdown();
//         }
//         System.out.println("Scheduled task stopped.");
//     }

//     private void writeNetworkInfoToFile() throws Exception {
//         // Overwrite the file with new data
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
//             List<NetworkInterface> networkInterfaces = enumerationToList(NetworkInterface.getNetworkInterfaces());

//             for (NetworkInterface networkInterface : networkInterfaces) {
//                 writer.write("Interface: " + networkInterface.getName());
//                 writer.newLine();

//                 if (networkInterface.isLoopback() || !networkInterface.isUp()) {
//                     continue;
//                 }

//                 List<InetAddress> inetAddresses = enumerationToList(networkInterface.getInetAddresses());
//                 for (InetAddress inetAddress : inetAddresses) {
//                     writer.write("IP Address: " + inetAddress.getHostAddress());
//                     writer.newLine();

//                     // Get subnet mask
//                     String subnetMask = getSubnetMask(inetAddress.getHostAddress());
//                     writer.write("Subnet Mask: " + subnetMask);
//                     writer.newLine();

//                     // Get gateway
//                     String gateway = getGateway();
//                     writer.write("Gateway: " + gateway);
//                     writer.newLine();
//                 }

//                 writer.newLine();
//             }
//         }
//     }

//     private void writeTrapIpToFile() throws Exception {
//         // Append TRAP IP to file
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
//             // Write TRAP IP (placeholder for actual implementation)
//             writer.write("TRAP IP: " + getTrapIp());
//             writer.newLine();
//         }
//     }

//     private <T> List<T> enumerationToList(Enumeration<T> enumeration) {
//         List<T> list = new ArrayList<>();
//         while (enumeration.hasMoreElements()) {
//             list.add(enumeration.nextElement());
//         }
//         return list;
//     }

//     private String getSubnetMask(String ipAddress) {
//         try {
//             InetAddress address = InetAddress.getByName(ipAddress);
//             NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);

//             if (networkInterface == null) {
//                 return "Network interface not found";
//             }

//             List<InterfaceAddress> interfaceAddresses = new ArrayList<>(networkInterface.getInterfaceAddresses());
//             for (InterfaceAddress ifaceAddress : interfaceAddresses) {
//                 if (ifaceAddress.getAddress().getHostAddress().equals(ipAddress)) {
//                     short prefixLength = ifaceAddress.getNetworkPrefixLength();
//                     return getSubnetMaskFromPrefixLength(prefixLength);
//                 }
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "Unable to determine subnet mask";
//     }

//     private String getSubnetMaskFromPrefixLength(short prefixLength) {
//         int mask = 0xffffffff << (32 - prefixLength);
//         return String.format("%d.%d.%d.%d",
//             (mask >> 24) & 0xff,
//             (mask >> 16) & 0xff,
//             (mask >> 8) & 0xff,
//             mask & 0xff);
//     }

//     private String getGateway() throws Exception {
//         String command = "ip route show"; // For Linux/Unix systems
//         Process process = Runtime.getRuntime().exec(command);
//         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

//         String line;
//         String gateway = "Unknown";
//         while ((line = reader.readLine()) != null) {
//             if (line.startsWith("default") || line.contains("default")) {
//                 String[] parts = line.split(" ");
//                 for (int i = 0; i < parts.length; i++) {
//                     if (parts[i].equals("via")) {
//                         gateway = parts[i + 1];
//                         break;
//                     }
//                 }
//                 break;
//             }
//         }
//         reader.close();
//         return gateway;
//     }

//     private String getTrapIp() {
//         // TRAP IP setting is not directly supported in Java. Configure SNMP settings accordingly.
//         return "TRAP IP retrieval not implemented";
//     }
// }
