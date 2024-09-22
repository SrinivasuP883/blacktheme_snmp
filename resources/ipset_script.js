
let fetchInterval = null;

// Function to fetch and update IP data
async function fetchIPData() {
    try {
        console.log('Fetching IP data...');
        // Fetch the content of the ip_data.txt file
        const response = await fetch('ip_data.txt');
        
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        
        // Get the text content from the response
        const text = await response.text();
        console.log('Data fetched:', text);
        
        // Split the text into lines
        const lines = text.split('\n');
        
        // Variables to store the information
        let ipAddress = '';
        let subnetMask = '';
        let gateway = '';
        let trap = '';
        let interfaceName = '';
        
        let isEnp19s0 = false;
        
        // Iterate through lines to find information for the specified interface
        for (const line of lines) {
            if (line.startsWith('Interface: enp19s0')) {
                isEnp19s0 = true;
                continue;
            }
            
            if (line.startsWith('Interface:') && isEnp19s0) {
                isEnp19s0 = false;
                break;
            }
            
            if (isEnp19s0) {
                if (line.startsWith('IP Address:')) {
                    ipAddress = line.split(':')[1].trim();
                } else if (line.startsWith('Subnet Mask:')) {
                    subnetMask = line.split(':')[1].trim();
                } else if (line.startsWith('Gateway:')) {
                    gateway = line.split(':')[1].trim();
                } else if (line.startsWith('TRAP IP:')) {
                    trap = line.split(':')[1].trim();
                } else if (line.startsWith('Interface Name:')) {
                    interfaceName = line.split(':')[1].trim();
                }
            }
        }
        
        // Update form fields with the fetched data
        console.log('Updating fields:', { ipAddress, subnetMask, gateway, trap, interfaceName });
        document.getElementById('ipAddress').value = ipAddress;
        document.getElementById('subnetMask').value = subnetMask;
        document.getElementById('gateway').value = gateway;
        document.getElementById('trap').value = trap;
        document.getElementById('interfaceName').value = interfaceName;
        
    } catch (error) {
        console.error('Error fetching the IP data:', error);
        // Handle error states in the UI
        document.getElementById('ipAddress').value = 'Failed to fetch IP data.';
        document.getElementById('subnetMask').value = '';
        document.getElementById('gateway').value = '';
        document.getElementById('trap').value = '';
        document.getElementById('interfaceName').value = '';
    }
}

// Start fetching data immediately and then at regular intervals
function startFetching() {
    console.log('Starting data fetch...');
    fetchIPData(); // Fetch immediately
    fetchInterval = setInterval(fetchIPData, 30000); // 30 seconds interval
}

// Stop fetching data
function stopFetching() {
    if (fetchInterval) {
        clearInterval(fetchInterval);
        fetchInterval = null;
        console.log('Fetching stopped.');
    }
}

// Initialize fetching data as soon as the page is loaded
document.addEventListener('DOMContentLoaded', () => {
    startFetching();
    console.log('Document loaded and fetching started.');
});

// Perform IP set operation based on form data
function perform_set_ip() {
    const ipAddress = document.getElementById('ipAddress').value;
    const subnetMask = document.getElementById('subnetMask').value;
    const gateway = document.getElementById('gateway').value;
    const trap = document.getElementById('trap').value;
    const interfaceName = 'enp19s0'; // Fixed value as per your example

    console.log('Performing IP set with:', { ipAddress, subnetMask, gateway, interfaceName });
    if (typeof javaObject !== 'undefined' && typeof javaObject.performIpSet === 'function') {
        javaObject.performIpSet(ipAddress, subnetMask, gateway, interfaceName);
    } else {
        console.error('Java object or method performIpSet is not available.');
    }
}
