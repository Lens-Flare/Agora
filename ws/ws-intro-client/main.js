window.onload = function() {
	var ws = $.websocket("ws://127.0.0.1:8080/websocket", {
		events: {
			broadcast: function(e) {
				$("#messages>section").append(e.message + "<br />");
			}
		}
	});
	$('#entry>input').change(function(){
		ws.send('message',
			{
				type: "broadcast",
				message: $("#entry>input").val()
			}
		);
	});
}