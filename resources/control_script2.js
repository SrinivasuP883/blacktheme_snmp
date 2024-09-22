 // Function to fetch IP data from the file and update the input fields
 async function fetchIpData() {
    try {
        const response = await fetch('ip_data.txt');
        const text = await response.text();
        
        // Parse the text data
        const lines = text.split('\n').filter(line => line.trim() !== '');
        
        let ipAddress = '';
        let subnetMask = '';
        let gateway = '';
        let trapIp = '';
        
        lines.forEach(line => {
            if (line.includes('IP Address:')) {
                ipAddress = line.split('IP Address:')[1].trim();
            } else if (line.includes('Subnet Mask:')) {
                subnetMask = line.split('Subnet Mask:')[1].trim();
            } else if (line.includes('Gateway:')) {
                gateway = line.split('Gateway:')[1].trim();
            } else if (line.includes('TRAP IP:')) {
                trapIp = line.split('TRAP IP:')[1].trim();
            }
        });

        // Update the HTML inputs
        document.getElementById('ip-address').value = ipAddress;
        document.getElementById('subnet-mask').value = subnetMask;
        document.getElementById('gateway').value = gateway;
        document.getElementById('trap-ip').value = trapIp;

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Fetch IP data every 5 seconds
setInterval(fetchIpData, 10000);

// Fetch IP data initially
fetchIpData();


function setIpSettings() {
    // Retrieve values from input fields
    var ipAddress = document.getElementById('ip-address').value;
    var subnetMask = document.getElementById('subnet-mask').value;
    var gateway = document.getElementById('gateway').value;
    var interfaceName = 'enp19s0';
    //document.getElementById('interface-name').value;

    // Call Java method through JavaBridge
    if (window.javaObject) {
        window.javaObject.performIpSet(ipAddress, subnetMask, gateway, interfaceName);
    } else {
        console.log('JavaBridge is not initialized');
    }
}
