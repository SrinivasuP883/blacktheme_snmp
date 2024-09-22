// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// public class set_java {

//     public static void main(String[] args) {
//         String interfaceName = "enp19s0";
//         String ipAddress = "10.42.0.5";
//         String subnetMask = "255.255.255.0";
//         String gateway = "192.168.29.204";

//         // Construct the commands
//         String addIpCommand = String.format("sudo ip addr add %s/24 dev %s", ipAddress, interfaceName);
//         String setGatewayCommand = String.format("sudo ip route add default via %s dev %s", gateway, interfaceName);

//         try {
//             // Run the command to set the IP address
//             executeCommand(addIpCommand);

//             // Run the command to set the default gateway
//             executeCommand(setGatewayCommand);

//             System.out.println("Network configuration updated successfully.");

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private static void executeCommand(String command) throws Exception {
//         // Execute the command
//         Process process = Runtime.getRuntime().exec(command);

//         // Capture the output
//         BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
//         BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

//         // Print the standard output
//         String s;
//         System.out.println("Standard output:");
//         while ((s = stdInput.readLine()) != null) {
//             System.out.println(s);
//         }

//         // Print any errors
//         System.out.println("Standard error:");
//         while ((s = stdError.readLine()) != null) {
//             System.out.println(s);
//         }

//         // Wait for the command to complete
//         process.waitFor();
//     }
// }
