TemplateService = function(){};
TemplateService.prototype.findAllUrl= function(){
	return window.location.origin + "/templates";
};
TemplateService.prototype.findAll= function(successCallback){
	$.get(this.findAllUrl(), successCallback);
};
TemplateService.prototype.findOneUrl= function(id){
	return window.location.origin + "/templates/" + id; 
};
TemplateService.prototype.findOne= function(id, successCallback){
	$.get(this.findOneUrl(id), successCallback);
};
var templateService = new TemplateService();