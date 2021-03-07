<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">

	$(function(){
		
		// 为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			/*
				操作模态窗口方式：
					需要操作的模态窗口的jQuery对象，调用modal方法，为该方法传递参数 show：打开模态窗口 hide: 关闭模态窗口
			 */
			//alert("234");
			//$("#createActivityModal").modal("show");

			// 走后台，目的是取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url : "workbench/activity/getUserList.do",
				data : {

				},
				type : "get",
				dataType : "json",
				success : function (data) {
					/*
						data :
							[{用户1},{用户2}...]
					 */
					var html = "<option></option>";
					// 遍历出来的每一个n,就是每一个user对象
					$.each(data, function (i, n) {
						html += "<option value='" + n.id + "'>" + n.name
						+"</option>";
					})
					$("#create-owner").html(html);

					// 将当前登陆的用户设置为下拉框默认值
					$("#create-owner").val("${user.id}");
					// 所用者处理完毕后，打开模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})

		// 为按钮绑定事件，执行添加操作
		$("#saveBtn").click(function () {

			$.ajax({
				url : "workbench/activity/save.do",
				data : {

					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())

				},
				type : "post",
				dataType : "json",
				success : function (data) {
					/*
						data
							{"success":true/false}
					 */

					if(data.success) {
						// 添加成功，刷新市场信息列表（局部刷新）

						// 清空添加操作模态窗口中的数据
						/*
							拿到form表单的jQuery对象
							对于表单的jQuery对象，提供了submit方法提交表单方法
							但是没有reset()方法
							 js为我们提供了reset方法
							 将jQuery转换为原生的dom对象

							 jQuery对象转换为dom对象
							 	jquery对象[下标]

							 dom对象转换为jQuery对象
							 	$(dom)

						 */
						//$("#activityAddForm")[0].reset();

						// 关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
						pageList(1, 2);
					} else {
						alert("添加失败！");
					}
				}
			})
		})

		// 页面加载完毕触发一个方法
		// 默认展开列表的两条记录
		pageList(1, 2);

		// 为查询按钮绑定事件，除法pageList方法
		$("#search-button").click(function () {

			/*
				点击查询按钮时，应该将搜索框中的信息保存在隐藏域中
			 */

			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1, 2);
		})

		// 为全选的复选框提供按钮绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked", this.checked);

		})

		// 以下做法不行，因为动态生成的元素不能够一普通绑定事件的形式来进行操作
		/*$("input[name=xz]").click(function () {

		})*/

		/*
			动态生成的元素以 on方法触发事件
			 $(需要绑定元素的有效外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)
		 */
		$("#activityBody").on("click", $("input[name=xz]"), function () {
			$("#qx").prop("checked", $("input[name=xz]").length ==
					$("input[name=xz]:checked").length);
		})

		// 为删除按钮绑定事件，执行市场信息删除操作
		$("#deleteBtn").click(function () {
			// 找到复选框中所有选中的复选框jquery对象
			var $xz = $("input[name=xz]:checked");
			if($xz.length == 0) {
				alert("请选择要删除的记录");
			} else {

				if(confirm("你确定删除已选择的信息吗？")){
					// url:workbench/activity/delete.do?id=xxx&id=xxx...
					// 拼接参数
					var param = "";

					// 将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了要删除的数据的id值
					for(i = 0; i < $xz.length; i++) {
						param += "id=" + $($xz[i]).val();

						if(i < $xz.length - 1) {
							param += "&";
						}
					}

					$.ajax({
						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function (data) {
							/*
                                data
                                    {"success":true/false}
                             */

							if(data.success) {
								// 删除成功
								pageList(1, 2);

							} else {
								alert("删除失败");
							}
						}
					})
				}

			}

		})

	});

	/*
		对于所有的关系型数据库，做前端分页相关操作的基础组件
		就是pageNo(页码)和pageSize（每页条数）

		pageList方法：发出ajax请求到后台，从后台去的数据
			通过响应来的数据，局部刷新数据

		需要刷新列表的情况（调用pageList（）方法）：
			① 点击市场活动链接时
			② 添加、修改、删除数据之后
			③ 点击查询按钮时
			④ 点击分页组件时
	 */
	function pageList(pageNo, pageSize) {

		// 将全选的复选框取消
		$("#qx").prop("checked", false);
		// 查询前，将隐藏域中信息取出来放到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url : "workbench/activity/pageList.do",
			data : {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			type : "get",
			dataType : "json",
			success : function (data) {

				/*
					data
						{"total":10, "datalast":[{市场活动1},...]}
							市场活动信息列表
								[{市场活动1},...]
							分页插件需要：查询出来的总记录数
								{"total":10}


				 */
				var html = "";
				// 每一个n是一个市场活动对象
				$.each(data.datalist, function (i, n) {
					html += '<tr class="active">';
					html +=
							'<td><input type="checkbox"  name="xz" value="' +
							n.id +
					'"/></td>';
					html +=
							'<td><a style="text-decoration: none; cursor:pointer;"onclick="window.location.href=\'workbench/activity/detail.jsp\';">' + n.name + '</a></td>';
					html += '<td>' + n.owner + '</td>';
					html += '<td>' + n.startDate + '</td>';
					html += '<td>' + n.endDate +'</td>';
					html += '</tr>';
				})
				$("#activityBody").html(html);

				// 计算总页数
				var totalPages = data.total % pageSize ==0 ? data.total /
						pageSize : parseInt(data.total / pageSize) + 1;

				// 数据处理完毕，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					// 该回调函数，在点击分页组件时触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name" />
	<input type="hidden" id="hidden-owner" />
	<input type="hidden" id="hidden-startDate" />
	<input type="hidden" id="hidden-endDate" />

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal"
						  role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control"
										id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control"
									   id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time"
									   id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time"
									   id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3"
										  id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">

					<%--
						data-dismiss : 关闭模态窗口
					--%>

					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
							id="saveBtn">保存
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startTime" value="2020-10-10">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endTime" value="2020-10-20">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text"
							 id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text"
							 id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="search-button"
						  class="btn btn-default">查询
				  </button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary"
						  id="addBtn"><span
						  class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"
						  id="deleteBtn"><span
						  class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称123</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor:
							pointer;"
								   onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor:
                            pointer;"
								   onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>