window.onload = function() {
	$.socketchat = function (container, address, options) {
		var self = [];
		
		$(container).addClass('socketchat').append(
			$('<table/>').addClass('socketchat_table').append(
				$('<tr/>').addClass('socketchat_toprow').append(
					self.users = $('<td/>').addClass('socketchat_users').append(
						$('<select size="2"/>').append(
							$('<option/>').addClass('socketchat_alluser').html('All')
						)
					),
					self.messages = $('<td/>').addClass('socketchat_messages').append(
						$('<section/>')
					)
				),
				$('<tr/>').addClass('socketchat_botrow').append(
					self.namefield = $('<td/>').addClass('socketchat_name').append(
						$('<input type="text"/>')
					),
					self.entry = $('<td/>').addClass('socketchat_entry').append(
						$('<input type="text"/>').attr('disabled', true)
					)
				)
			)
		);
		
		self.socket = $.websocket(address, {
			events: {
				users: function (msg) {
					for (var i = 0; i < msg.users.length; i++)
						self.users.find('>select').append(
							$('<option/>').html(msg.users[i])
						);
				},
				
				signin: function (msg) {
					self.users.find('>select').append(
						$('<option/>').html(msg.name)
					);
				},
				signout: function (msg) {
					self.users.find('>select>option').each(function () {
						if (this.value == msg.name)
							$(this).remove();
					});
				},
				
				broadcast: function (msg) {
					self.messages.find('>section').append(msg.from + ': ' + msg.message + '<br />');
				}
			}
		});
		
		self.namefield.find('>input').keypress(function (e) {
			if (e.which == 13) {
				self.sn = e.currentTarget.value;
				self.namefield.html(
					$('<div/>').html(self.sn)
				);
				self.socket.send('message', {
					type: 'signin',
					name: self.sn
				});
				self.entry.find('>input').removeAttr('disabled');
			}
		});
		
		self.entry.find('>input').keypress(function (e) {
			if (e.which == 13) {
				self.socket.send('message', {
					type: 'broadcast',
					from: self.sn,
					message: e.currentTarget.value
				});
				e.currentTarget.value = '';
			}
		});
		
		return self;
	}
	
	var sc = $.socketchat('#area', 'ws://127.0.0.1:8080/chat');
}