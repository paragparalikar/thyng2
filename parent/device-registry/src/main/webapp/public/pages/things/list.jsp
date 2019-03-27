<%@ page import="com.thyng.model.dto.UserDTO" %>
<%@ page import="com.thyng.model.enumeration.Authority" %>
<% 
	final UserDTO user = (UserDTO)session.getAttribute("user");
	final boolean hasWriteAccess = user.hasAuthority(Authority.THING_CREATE) || user.hasAuthority(Authority.THING_UPDATE) || user.hasAuthority(Authority.THING_DELETE);
%>
<div class="card">
    <div class="card-body">
        <table id="things" class="table table-bordered table-striped" style="width: 100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Key</th>
                    <th>Description</th>
                    <th>BiDirectional</th>
                    <th>Alive</th>
                    <% if(hasWriteAccess){ %>
                    <th>
                    	<%if(user.hasAuthority(Authority.THING_CREATE)){ %>
                        <button class="btn btn-primary btn-sm" id="newThingButton">
                            <span class="fa fa-plus"></span> New Thing
                        </button>
                        <%} %>
                    </th>
                    <%} %>
                </tr>
            </thead>
        </table>
    </div>
</div>

<script type="text/javascript">
	render = (function($){
		var thingsDataTable = null;
		
		var showThingEditModal = function(event, element, originalEvent, id){
			element.blur();
			originalEvent.preventDefault();
			row = thingsDataTable.row("#" + id);
			doShowThingEditModal(row.data());
		}
		var doShowThingEditModal = function(thing){
			$("#modal-container").loadTemplate("edit-thing", thing, {
				success : function(){
					$("#modal-container").modal();
					render(thing);
				},
		    	error: errorCallback
			});
		}
		
		var showDeleteThingConfirmationModal = function(event, element, originalEvent, id){
			element.blur();
			originalEvent.preventDefault();
			row = thingsDataTable.row("#" + id);
	        $.publish("show-confirmation-modal", [{
	            message: "Are you sure you want to delete thing " + row.data().name + " ?"
	        }, function () {
	            thingService.deleteById(id, function () {
	            	toast('Thing has been deleted successfully');
	                row.remove().draw();
	                $.modal.close();
	            });
	        }]);
		};
		
		var showThingsDataTable = function(){
			thingService.findAll(function (things) {
				thingsDataTable = $("#things").DataTable({
		            rowId: "id",
		            language: {
		                processing: "Loading ..."
		            },
		            processing: true,
		            columns: [
		                {
		                    data: "name",
		                    mRender: function (data, type, row) {
		                        return user.hasAuthority("THING_VIEW") ? '<a href="#" onclick="event.preventDefault(); this.blur(); $.publish(\'show-thing-view-modal\','+row.id+');">' + data + '</a>' : data;
		                    }
		                },
		                { data: "key" },
		                { data: "description" },
		                { 
		                	data: "biDirectional",
		                	render: function(data, type, row, meta){
		                		return data ? "<i class='fa fa-check text-success'></i> " : "<i class='fa fa-times text-danger'></i> ";
		                	}
	                	},
		                {
		                	data: "alive",
		                	render: function(data, type, row, meta){
		                		return data ? "<i class='fa fa-check text-success'></i> " : "<i class='fa fa-times text-danger'></i> ";
		                	}
		                }
	                	<% if(hasWriteAccess){%>
		                ,{
		                    mRender: function (data, type, row, meta) {
		                    	var editHtml = user.hasAuthority("THING_UPDATE") ? 
		                    		'<button class="btn btn-warning btn-xs" onclick="$(\'#things\').trigger(\'edit-thing\', [this, event,'+row.id+'])">' +
		                                '<span class="fa fa-edit"></span> Edit' +
		                            '</button>' : "";
		                        var deleteHtml = user.hasAuthority("THING_DELETE") ?
		                        	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#things\').trigger(\'delete-thing\', [this, event,'+row.id+'])">' +
		                            	'<span class="fa fa-trash"></span> Delete' +
		                            '</button>' : "";
		                        return '<div class="btn-group" role="group">' + editHtml + deleteHtml + '</div>';
		                    }
		                }
		                <%}%>
		                ],
		            columnDefs: [{
		                targets: -1,
		                orderable: false,
		                className: "text-center"
		            },
		            {
		                targets: -2,
		                orderable: true,
		                className: "text-center"
		            }
		            <% if(hasWriteAccess){%>
		            ,{
		                targets: -3,
		                orderable: true,
		                className: "text-center"
		            }
		            <%}%>
		            ],
		            data: things
		        });
		    });
		};
		
		return function(){
			$("#page-title").html("Things");
			showThingsDataTable();
			$("#things").on("edit-thing", showThingEditModal);
			$("#things").on("delete-thing", showDeleteThingConfirmationModal);
		}
	})(jQuery);

</script>