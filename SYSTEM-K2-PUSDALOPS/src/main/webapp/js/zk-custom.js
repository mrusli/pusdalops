/**
 * 
 * REF: https://zkfiddle.org/sample/1n2c0ol/68-Break-line-in-label-manually#source-1
 */
zk.afterLoad("zul.wgt", function () {
	zul.wgt.Label.prototype.getEncodedText = function () {
		return zUtl.encodeXML(this._value, {multiline:this._multiline,pre:this._pre, maxlength: this._maxlength}).replace(/_r_n/g, "<br />");
	}
});

