// tab-3/main.js
define(['t3map'], function(t3map) {
	$('#start-3').click( function() {
		var key3 = $.trim( $('#key-3').val() );
		if(key3) {
			t3map.getBoundary( key3 );
		}else {
			$('#key-3').focus();
		}
	})
});
