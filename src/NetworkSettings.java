// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// public class NetworkSettings {

//     public static void main(String[] args) {
//         // Example usage
//         String ipAddress = "10.42.0.5";
//         String subnetMask = "255.255.255.0";
//         String gateway = "192.168.29.204";
//         String interfaceName = "enp19s0";  // Example interface name

//         try {
//             String result = performIpSet(ipAddress, subnetMask, gateway, interfaceName);
//             System.out.println(result);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static String performIpSet(String ipaddress, String subnetmask, String gateway, String interfaceName) {
//         // Construct the commands
//         String addIpCommand = String.format("sudo ip addr add %s/%d dev %s", ipaddress, getSubnetPrefix(subnetmask), interfaceName);
//         String setGatewayCommand = String.format("sudo ip route add default via %s dev %s", gateway, interfaceName);

//         try {
//             // Execute the command to set the IP address
//             String addIpResult = executeCommand(addIpCommand);

//             // Execute the command to set the default gateway
//             String setGatewayResult = executeCommand(setGatewayCommand);

//             return "Network configuration updated successfully.\n" + addIpResult + "\n" + setGatewayResult;

//         } catch (Exception e) {
//             return "Failed to update network configuration: " + e.getMessage();
//         }
//     }

//     private static String executeCommand(String command) throws Exception {
//         // Execute the command
//         Process process = Runtime.getRuntime().exec(command);

//         // Capture the output
//         StringBuilder output = new StringBuilder();
//         try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
//              BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

//             // Print the standard output
//             String line;
//             output.append("Standard output:\n");
//             while ((line = stdInput.readLine()) != null) {
//                 output.append(line).append("\n");
//             }

//             // Print any errors
//             output.append("Standard error:\n");
//             while ((line = stdError.readLine()) != null) {
//                 output.append(line).append("\n");
//             }
//         }

//         // Wait for the command to complete
//         process.waitFor();
//         return output.toString();
//     }

//     private static int getSubnetPrefix(String subnetMask) {
//         // Convert subnet mask to CIDR prefix
//         String[] parts = subnetMask.split("\\.");
//         int prefix = 0;
//         for (String part : parts) {
//             int octet = Integer.parseInt(part);
//             while (octet > 0) {
//                 prefix += (octet & 1);
//                 octet >>= 1;
//             }
//         }
//         return prefix;
//     }
// }
