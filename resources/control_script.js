//=========================================================
// FETCH TESTING FOR TWO BLOCKS START
//=========================================================
async function fetchData() {
    try {
        const response = await fetch('write.txt');
        const text = await response.text();
        return text.trim(); // Ensure there are no extra whitespace characters
    } catch (error) {
        console.warn('Error fetching data:', error);
        return null;
    }
}

// Function to update a specific custom dropdown based on the OID and value
function updateDropdown(dropdownId, targetOid, data) {
    const dropdown = document.getElementById(dropdownId);
    const items = dropdown.getElementsByClassName('dropdown-item');
    const button = document.querySelector(`#${dropdownId}_dropdownButton`);
    let foundValue = null;

    for (const item of items) {
        if (item.getAttribute('data-value') === '0' || item.getAttribute('data-value') === '1') {
            if (data.includes(targetOid)) {
                const value = data.split('\n').find(line => line.startsWith(targetOid))?.split(':')[1].trim();
                if (value === item.getAttribute('data-value')) {
                    foundValue = value;
                    break;
                }
            }
        }
    }

    if (foundValue !== null) {
        // Remove 'selected' class from all items
        for (const item of items) {
            item.classList.remove('selected');
        }
        // Add 'selected' class to the found item
        const selectedItem = Array.from(items).find(item => item.getAttribute('data-value') === foundValue);
        if (selectedItem) {
            selectedItem.classList.add('selected');
            // Update button text
            button.textContent = selectedItem.textContent;
        }
    } else {
        button.textContent = "NA";
        console.warn(`OID ${targetOid} not found or unexpected value.`);
    }
}

// Main function to update all custom dropdowns
async function updateDropdowns() {
    const data = await fetchData();

    if (data !== null) {
        // Update the first custom dropdown (rfmute_layout)
        updateDropdown('rfmute_layout', '1.3.6.1.4.1.29686.1.1.4.1.6.0', data);

        // Update the second custom dropdown (extref_layout)
        updateDropdown('extref_layout', '1.3.6.1.4.1.29686.1.1.4.1.4.0', data);
    } else {
        console.warn('Failed to update dropdowns due to data fetching issue.');
    }
}

// Fetch and update dropdowns every 30 seconds
setInterval(updateDropdowns, 10000); // Updated to 30000 milliseconds for 30 seconds

// Initial call to update dropdowns immediately upon page load
updateDropdowns();

//==========================================================
// FETCH TESTING FOR TWO BLOCKS END
//==========================================================

document.addEventListener('DOMContentLoaded', () => {
    // Function to fetch digital attention value and update the dropdown
    function fetchDigitalAttn() {
        fetch('write.txt')
            .then(response => response.text())
            .then(text => {
                console.log('Fetched text:', text); // Debug: Check fetched text
                // Extract the OID and number from the text
                const oidMatch = text.match(/1\.3\.6\.1\.4\.1\.29686\.1\.1\.4\.1\.3\.0\s*:\s*(\d+)/);
                if (oidMatch) {
                    const number = parseInt(oidMatch[1], 10);
                    const result = (number / 100).toFixed(2);

                    // Debug: Check the result value
                    console.log('Extracted result:', result);

                    // Update the dropdown with the new digital attention value
                    updateDigitalAttn(result);
                } else {
                    console.error('Failed to find the OID or parse the number from the text file.');
                }
            })
            .catch(error => console.error('Error fetching the text file:', error));
    }

    // Function to update the custom dropdown with the new digital attention value
    function updateDigitalAttn(selectedValue) {
        const dropdownMenu = document.getElementById('digital_attn_layout');
        const items = dropdownMenu.getElementsByClassName('dropdown-item');
        const button = document.querySelector('#digital_attn_layout_dropdownButton');

        if (!dropdownMenu || !button) {
            console.error('Dropdown menu or button not found.');
            return;
        }

        // Remove 'selected' class from all items
        Array.from(items).forEach(item => item.classList.remove('selected'));

        // Find the item with the matching value and add 'selected' class
        const itemToSelect = Array.from(items).find(item => item.getAttribute('data-value') === selectedValue);
        if (itemToSelect) {
            itemToSelect.classList.add('selected');
            // Update button text
            button.textContent = itemToSelect.textContent;
        } else {
            button.textContent = "NA";
            console.warn(`Value ${selectedValue} not found in dropdown items.`);
        }
    }

    // Fetch and update immediately when the page loads
    fetchDigitalAttn();

    // Set up the interval to fetch and update every 5 seconds
    setInterval(fetchDigitalAttn, 10000); // 5000 milliseconds = 5 seconds
});

//===================================================================================================
//===================================================================================================
//===================================================================================================
//===================================================================================================
//===================================================================================================
//===================================================================================================


document.addEventListener('DOMContentLoaded', function() {
    var dropdownItems = document.querySelectorAll('#rfmute_layout .dropdown-item');
    var dropdownButton = document.getElementById('rfmute_layout_dropdownButton');
    var selectedValue = 'NA'; // Default value

    dropdownItems.forEach(function(item) {
        item.addEventListener('click', function() {
            selectedValue = item.getAttribute('data-value');
            dropdownButton.textContent = item.textContent; // Update button text
        });
    });

    document.getElementById('rf_mute_set_button').addEventListener('click', function() {
        perform_rfmuteset();
    });

    function perform_rfmuteset() {
        var set_community = "newPublic";
        var set_ipAddress = "10.0.0.2";
        var set_oid = "1.3.6.1.4.1.29686.1.1.4.1.6.0";
        var set_value = selectedValue;

        // Assuming javaObject is available globally or imported
        javaObject.performSnmpSet(set_community, set_ipAddress, set_oid, set_value);
    }
});


document.addEventListener('DOMContentLoaded', function() {
    // Handle the extref_layout dropdown
    var extrefDropdownItems = document.querySelectorAll('#extref_layout .dropdown-item');
    var extrefDropdownButton = document.getElementById('extref_layout_dropdownButton');
    var extrefSelectedValue = 'NA'; // Default value

    extrefDropdownItems.forEach(function(item) {
        item.addEventListener('click', function() {
            extrefSelectedValue = item.getAttribute('data-value');
            extrefDropdownButton.textContent = item.textContent; // Update button text
        });
    });

    document.getElementById('extref_set_button').addEventListener('click', function() {
        perform_extrefset();
    });

    function perform_extrefset() {
        var set_community = "newPublic";
        var set_ipAddress = "10.0.0.2";
        var set_oid = "1.3.6.1.4.1.29686.1.1.4.1.4.0";
        var set_value = extrefSelectedValue;

        // Assuming javaObject is available globally or imported
        javaObject.performSnmpSet(set_community, set_ipAddress, set_oid, set_value);
    }
});


document.addEventListener('DOMContentLoaded', function() {
    const values = [
        "0.00", "0.25", "0.50", "0.75", "1.00", "1.25", "1.50", "1.75", "2.00",
        "2.25", "2.50", "2.75", "3.00", "3.25", "3.50", "3.75", "4.00", "4.25",
        "4.50", "4.75", "5.00", "5.25", "5.50", "5.75", "6.00", "6.25", "6.50",
        "6.75", "7.00", "7.25", "7.50", "7.75", "8.00", "8.25", "8.50", "8.75",
        "9.00", "9.25", "9.50", "9.75", "10.00", "10.25", "10.50", "10.75", "11.00",
        "11.25", "11.50", "11.75", "12.00", "12.25", "12.50", "12.75", "13.00",
        "13.25", "13.50", "13.75", "14.00", "14.25", "14.50", "14.75", "15.00",
        "15.25", "15.50", "15.75", "16.00", "16.25", "16.50", "16.75", "17.00",
        "17.25", "17.50", "17.75", "18.00", "18.25", "18.50", "18.75", "19.00",
        "19.25", "19.50", "19.75", "20.00", "20.25", "20.50", "20.75", "21.00",
        "21.25", "21.50", "21.75", "22.00", "22.25", "22.50", "22.75", "23.00",
        "23.25", "23.50", "23.75", "24.00", "24.25", "24.50", "24.75", "25.00",
        "25.25", "25.50", "25.75", "26.00", "26.25", "26.50", "26.75", "27.00",
        "27.25", "27.50", "27.75", "28.00", "28.25", "28.50", "28.75", "29.00",
        "29.25", "29.50", "29.75", "30.00", "30.25", "30.50", "30.75", "31.00",
        "31.25", "31.50", "31.75"
    ];

    const inputField = document.getElementById('number-input');
    const suggestionsDiv = document.getElementById('suggestions');
    const setButton = document.getElementById('digital_attn_set_button');
    let selectedValue = '';

    function filterValues() {
        const input = inputField.value;
        suggestionsDiv.innerHTML = '';

        if (input === '') {
            suggestionsDiv.style.display = 'none';
            return;
        }

        const filteredValues = values.filter(val => val.startsWith(input));

        if (filteredValues.length > 0) {
            suggestionsDiv.style.display = 'block';
            filteredValues.forEach(val => {
                const item = document.createElement('div');
                item.className = 'suggestion-item';
                item.textContent = val;
                item.onclick = () => selectValue(val);
                suggestionsDiv.appendChild(item);
            });
        } else {
            suggestionsDiv.style.display = 'none';
        }
    }

    function selectValue(value) {
        inputField.value = value;
        suggestionsDiv.style.display = 'none';
        selectedValue = value;
    }

    function setValue() {
        const currentInputValue = inputField.value;

        if (values.includes(currentInputValue)) {
            selectedValue = currentInputValue;
            performDigitalAttnSet(selectedValue);
        } else {
            // Clear previous value and show error
            selectedValue = '';
            alert('Please enter a valid value.');
        }
    }

    function performDigitalAttnSet(value) {
        const setCommunity = "newPublic";
        const setIpAddress = "10.0.0.2";
        const setOid = "1.3.6.1.4.1.29686.1.1.4.1.3.0";
        const setValue = value * 100;

        // Assuming javaObject is available globally or imported
        if (window.javaObject) {
            window.javaObject.performSnmpSet(setCommunity, setIpAddress, setOid, setValue);
        } else {
            console.error('javaObject is not defined');
        }
    }

    inputField.addEventListener('input', filterValues);
    setButton.addEventListener('click', setValue);

    document.addEventListener('click', function(e) {
        if (!e.target.closest('#number-input') && !e.target.closest('#suggestions')) {
            suggestionsDiv.style.display = 'none';
        }
    });
});



//=========================================================================
//=========================================================================
//  HIDE AND SHOW CODE WHEN SCRIPT LOADED
//=========================================================================
//=========================================================================

document.addEventListener('DOMContentLoaded', function() {


    document.getElementById('network_setting_row').style.display = 'block';
    document.getElementById('rf_parameters_row').style.display = 'none';
    document.getElementById('system_settings_row').style.display = 'none';
});


// =================================================================================
// =================================================================================
// HIDE AND SHOW CODE START
// =================================================================================
// =================================================================================

document.getElementById('network_setting_card').addEventListener('click', function () {
    operation_nsc();
});


document.getElementById('rf_parameters_card').addEventListener('click', function () {
    operation_rpc();
});


document.getElementById('system_settings_card').addEventListener('click', function () {
    operation_ssc();
});

function operation_nsc()
{
    document.getElementById('network_setting_row').style.display = 'block';
    document.getElementById('rf_parameters_row').style.display = 'none';
    document.getElementById('system_settings_row').style.display = 'none';
}


function operation_rpc()
{
    document.getElementById('network_setting_row').style.display = 'none';
    document.getElementById('rf_parameters_row').style.display = 'block';
    document.getElementById('system_settings_row').style.display = 'none';
}


function operation_ssc()
{
    document.getElementById('network_setting_row').style.display = 'none';
    document.getElementById('rf_parameters_row').style.display = 'none';
    document.getElementById('system_settings_row').style.display = 'block';
}
// =================================================================================
// =================================================================================
// HIDE AND SHOW CODE START
// =================================================================================
// =================================================================================


//==================================================================================
//==================================================================================
// IP NETWORK SETTINGS START
//==================================================================================
//==================================================================================
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
        // document.getElementById('trap-ip').value = trapIp;

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Fetch IP data every 5 seconds
setInterval(fetchIpData, 5000);

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



// =======================================
// =======================================
// =======================================


document.addEventListener('DOMContentLoaded', (event) => {
    // Select all cards
    const cards = document.querySelectorAll('.cards');

    cards.forEach(card => {
        card.addEventListener('click', () => {
            // Remove 'clicked' class from all cards
            cards.forEach(c => c.classList.remove('clicked'));

            // Add 'clicked' class to the clicked card
            card.classList.add('clicked');
        });
    });
});

