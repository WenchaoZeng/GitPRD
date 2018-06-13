//开启socket连接
connect();


var webSocket = null;
function connect() {
    var path = 'ws://' + window.location.host +  '/notice_socket';
    console.log(path);
    // 连接到websocket 服务器端，使用构造函数 new WebSocket()
    webSocket = new WebSocket(path);
    // onError 当客户端-服务器通信发生错误时将会调用此方法。
    webSocket.onerror = function(event) {
        onError(event)
    };
    // onOpen 我们创建一个连接到服务器的连接时将会调用此方法。
    webSocket.onopen = function(event) {
        onOpen(event);
    };
    // onMessage 当从服务器接收到一个消息时将会调用此方法。在我们的例子中，我们只是将从服务器获得的消息添加到DOM。
    webSocket.onmessage = function(event) {
        onMessage(event)
    };
}

function onOpen(event) {
    //$.toast('success', "与服务器建立连接！");
    console.log('与服务器建立连接！');
}
function onError(event) {
    console.log("无法与服务器建立连接，请检查网络或浏览器版本！！");
}

// 服务器端推送回来的消息
function onMessage(event) {
    var data = JSON.parse(event.data.trim());
    // console.log(data);
    if( typeof noticeCallback === 'function' ){
        noticeCallback(data);
    }
}