
document.getElementById('login-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent the form from submitting normally

    // Get the username and password values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Check the username and password
    if (username === 'admin' && password === 'admin') {
        // Redirect to the monitor page
        window.location.href = 'monitor.html';
    } else {
        
        // Show the error message
        document.getElementById('error-message').style.display = 'block';
        
        // Clear the username and password fields
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
    }
});