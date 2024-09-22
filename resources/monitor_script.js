
let lastFetchTime = null; // Variable to store the last fetch time

function updateButtonColors(data) {
    // List of button IDs and their corresponding OIDs
    const buttonConfigs = [
        { id: 'bt1', oid: '1.3.6.1.4.1.29686.1.1.4.1.2.0' },
        { id: 'bt2', oid: '1.3.6.1.4.1.29686.1.1.5.1.3.0' },
        { id: 'bt3', oid: '1.3.6.1.4.1.29686.1.1.5.1.4.0' },
        { id: 'bt4', oid: '1.3.6.1.4.1.29686.1.1.3.1.4.0' },
        { id: 'bt5', oid: '1.3.6.1.4.1.29686.1.1.3.1.5.0' },
        { id: 'bt6', oid: '1.3.6.1.4.1.29686.1.1.3.1.6.0' },
        { id: 'bt7', oid: '1.3.6.1.4.1.29686.1.1.3.1.9.0' },
        { id: 'bt8', oid: '1.3.6.1.4.1.29686.1.1.3.1.7.0' },
        { id: 'bt9', oid: '1.3.6.1.4.1.29686.1.1.3.1.8.0' },
        { id: 'bt10', oid: '1.3.6.1.4.1.29686.1.1.3.1.2.0' }
    ];

    buttonConfigs.forEach(({ id, oid }) => {
        const button = document.getElementById(id);
        const value = data[oid];
        if (value === '0') {
            button.style.backgroundColor = 'green';
        } else if (value === '1') {
            button.style.backgroundColor = 'red';
        } else {
            button.style.backgroundColor = 'grey';
        }
    });
}

function updateCurrentTime() {
    if (!lastFetchTime) return; // Do nothing if no fetch time is available

    // Use the stored fetch time
    const now = lastFetchTime;

    // Get date components
    const day = now.getDate().toString().padStart(2, '0');
    const month = (now.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
    const year = now.getFullYear();

    // Get time components
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    const seconds = now.getSeconds().toString().padStart(2, '0');

    // Format the date and time
    const dateString = `${day}/${month}/${year}`;
    const timeString = `${hours}:${minutes}:${seconds}`;
    
    // Update the DOM element with date and time
    document.getElementById('current-time').textContent = `${dateString} ${timeString}`;
}

function warning_hide() {
    const warningElement = document.getElementById('warning');
    if (warningElement) {
        warningElement.style.display = 'none';

        const label = document.getElementById('online_offline_label');
        const button = document.getElementById('online_offline');

         // Update label and button styles for the "ONLINE" state
         label.innerHTML = '<strong>BUC ONLINE</strong>';
         button.style.backgroundColor = 'green'; // Change button color to green
    }
}

function warning_show() {
    const warningElement = document.getElementById('warning');
    if (warningElement) {
        warningElement.style.display = 'block';

        const label = document.getElementById('online_offline_label');
        const button = document.getElementById('online_offline');
        // Update label and button styles for the "OFFLINE" state
        label.innerHTML = '<strong>BUC OFFLINE</strong>';
        button.style.backgroundColor = 'red'; // Change button color to red
    }
}

function fetchData() {
    fetch('write.txt')
        .then(response => response.text())
        .then(text => {
            const lines = text.split('\n');
            const data = {};
            lines.forEach(line => {
                const [oid, value] = line.split(' : ').map(str => str.trim());
                if (oid && value) {
                    data[oid] = value;
                }
            });

            if (Object.keys(data).length > 0) {
                // Data is not empty
                updateButtonColors(data);
                lastFetchTime = new Date();
                updateCurrentTime(); // Update the displayed time with the last fetch time
                warning_hide(); // Hide the warning
            } else {
                warning_show(); // Show warning if data is empty
                updateButtonColors(data);

            }
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            warning_show(); // Show warning if there was an error
        });
}

// Fetch data every 10 seconds
setInterval(fetchData, 10000);

// Initial fetch
fetchData();

// Ensure the elements exist before manipulating them
document.addEventListener('DOMContentLoaded', () => {
    const offline = document.getElementById('offline');
    if (offline) offline.style.backgroundColor = 'grey';

    const online = document.getElementById('online');
    if (online) online.style.backgroundColor = 'green';

    const buttonConfigs = [
        'bt1', 'bt2', 'bt3', 'bt4', 'bt5', 'bt6', 'bt7', 'bt8', 'bt9', 'bt10'
    ];

    buttonConfigs.forEach(id => {
        const button = document.getElementById(id);
        if (button) button.style.backgroundColor = 'grey';
    });

    // Initially show warning if data is not available
    warning_show();
});