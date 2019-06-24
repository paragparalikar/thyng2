<%@ page import="com.thyng.user.UserDTO" %>
<%@ page import="com.thyng.aspect.security.Authority" %>
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
                    <th>Description</th>
                    <th>Active</th>
                    <th>Gateway</th>
                    <% if(hasWriteAccess){ %>
                    <th>
                    	<%if(user.hasAuthority(Authority.THING_CREATE)){ %>
                        <a class="btn btn-primary btn-sm" href="#things/edit/0">
                            <span class="fa fa-plus"></span> New Thing
                        </a>
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
		
		var thingsDataTable = $("#things").DataTable({
            rowId: "id",
            language: {
                processing: "Loading ..."
            },
            processing: true,
            columns: [
                {
                    data: "name",
                    render: function (data, type, row) {
                        return user.hasAuthority("THING_VIEW") ? '<a href="#things/view/'+row.id+'">' + data + '</a>' : data;
                    }
                },
                { data: "description" },
                {
                	data: "active",
                	render: function(data, type, row, meta){
                		return data ? "<i class='fa fa-check text-success'></i> " : "<i class='fa fa-times text-danger'></i> ";
                	}
                },
                { 
                	data: "gatewayName",
                	render: function(data, type, row, meta){
                		return user.hasAuthority("GATEWAY_VIEW") ? '<a href="" onclick="$(\'#things\').trigger(\'view-gateway\', [this, event,'+row.id+'])">' + data + '</a>' : data;
                	}
                }
            	<% if(hasWriteAccess){%>
                ,{
                    mRender: function (data, type, row, meta) {
                    	var copyHtml = user.hasAuthority("THING_CREATE") ? 
	                    		'<a class="btn btn-success btn-xs" href="#things/copy/'+row.id+'">' +
		                        	'<span class="fa fa-copy"></span> Copy' +
		                        '</a>' : "";
                    	var editHtml = user.hasAuthority("THING_UPDATE") ? 
                    		'<a class="btn btn-warning btn-xs" href="#things/edit/'+row.id+'">' +
                                '<span class="fa fa-edit"></span> Edit' +
                            '</a>' : "";
                        var deleteHtml = user.hasAuthority("THING_DELETE") ?
                        	'<button type="button" class="btn btn-danger btn-xs" onclick="$(\'#things\').trigger(\'delete-thing\', [this, event,'+row.id+'])">' +
                            	'<span class="fa fa-trash"></span> Delete' +
                            '</button>' : "";
                        return '<div class="btn-group" role="group">' + copyHtml + editHtml + deleteHtml + '</div>';
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
            ]
        });
		
		$("#things").on("delete-thing", function(event, element, originalEvent, id){
			element.blur();
			originalEvent.preventDefault();
			row = thingsDataTable.row("#" + id);
			$("#things").trigger("show-delete-thing-modal", [row.data(), function(){
				row.remove().draw();	
			}]);
		});
		
		$("#things").on("view-gateway", function(event, element, originalEvent, id){
			element.blur();
			originalEvent.preventDefault();
			row = thingsDataTable.row("#" + id);
			$.publish("show-view-gateway-modal", row.data().gatewayId);
		});
		
		return function(things){
			$("#page-title").html("Things");
			thingsDataTable.clear().rows.add(things).draw();
		}
	})(jQuery);

</script>