define(function(){
	function info(msg, msgClass , dom) {
		$(dom).find('ul').attr('style', 'position:relative;right:-100%');

		$(dom).find('ul')
			.removeClass('hidden')
			.animate({
				right:0
			}, '5')
			.find('li#message').html( msg )
			.removeAttr('class')
			.addClass(msgClass);
		
	}
	
	return {
		info: info
	}
});