let stompClient = null;
let isConnected = false;
let isSubscribed = false;

// Initialize connection when page loads
document.addEventListener('DOMContentLoaded', function() {
    connect();
    getUnreadNotification()
});

function connect() {
    const socket = new SockJS('/ws-notifications');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        if (!isSubscribed) {
            stompClient.subscribe('/topic/notifications', function(notification) {
                const data = JSON.parse(notification.body);
                updateNotification(data);
            });
            isSubscribed = true;
        }
    }, function(error) {
        console.log('Connection error:', error);
        setTimeout(connect, 5000);
    });
}

function getUnreadNotification(){
    fetch('/notification/get')
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json(); // or response.text(), depending on expected format
    }).then(data => {
         updateNotification(data)
    }).catch(error => {
        console.error('Fetch error:', error);
    });
}

function updateNotification(data){

    const notificationBadge = document.getElementById("notification-badge");
    const notificationMenu = document.getElementById("notification-menu")

    notificationBadge.textContent = data.countNotification
    notificationMenu.innerHtml =   ``;
    notificationMenu.innerHtml =+ `
                <li class="dropdown-header">
                    "You have ${data.countNotification} new notifications"
                </li>
                <li>
                    <hr class="dropdown-divider">
                </li>`;

    data.notifications.forEach(noti => {
        notificationMenu.innerHtml =+ `
                    <li class="notification-item">
                        <i class="bi bi-exclamation-circle text-warning"></i>
                        <div>
                            <h4>${noti.title}</h4>
                            <p>${noti.message}</p>
                            <p>${noti.timePass}</p>
                        </div>
                    </li>
                    <li>
                        <hr class="dropdown-divider">
                    </li>`
    });

}