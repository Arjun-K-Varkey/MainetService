<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	response.setContentType("text/html; charset=utf-8");
%>
<script>
$(document).ready(function(){
$("#sidebar-menu ul").not(':has(li)').remove(); //used for menu new one
$("#sidebar-menu li").not(':has(ul)').removeClass('submenu');

$('ul#nav li:has(ul)').addClass('submenu');
});

$("#sidebar-menu ul ul a").on('click',function(e){
if($(this).parent().hasClass("has_sub")) {
e.preventDefault();
}

if(!$(this).hasClass("subdrop")) {
$("ul",$(this).parents("ul:first")).slideUp(350);
$("a",$(this).parents("ul:first")).removeClass("subdrop");
$("#sidebar-menu .pull-right i").removeClass("fa-angle-up").addClass("fa-angle-down");

$(this).next("ul").slideDown(350);
$(this).addClass("subdrop");
}else if($(this).hasClass("subdrop")) {
$(this).removeClass("subdrop");
$(this).next("ul").slideUp(350);
}

});
</script>

<c:forEach var="masters" items="${menuRoleEntitlement.parentList}">
	<div class="tab-pane" id="${masters.entitle.smfid}">
		<div id="sidebar-menu" class="has_sub active">
			<h1>${masters.entitle.smfname}</h1>
			<ul class="scroll-menu" id="nav">
				<c:forEach items="${menuRoleEntitlement.childList}" var="data">
					<c:if test="${masters.entitle.smfid eq  data.parentId}">
						<c:set var="action0" value="${data.entitle.smfaction}" />
 							
						<li class="navigation" id="${data.entitle.smfid}"><a
							role="link" title="${data.entitle.smfname}"
							<c:choose> <c:when test="${fn:containsIgnoreCase(action0 , '.html')}">onclick="openRelatedForm('${action0}','this','${data.entitle.smfdescription}');breadcrumb('${masters.entitle.smfname}+${data.entitle.smfname}',${masters.entitle.smfid});" href="#" class="nolink" </c:when><c:otherwise> href="${action0}" </c:otherwise></c:choose>><i
								class="fa fa-cube" aria-hidden="true"></i>
								${data.entitle.smfname} </a>
							<ul class="scroll-menu1">
								<c:forEach items="${menuRoleEntitlement.childList}" var="data1">

									<c:if test="${data.entitle.smfid eq  data1.parentId}">
										<c:set var="action1" value="${data1.entitle.smfaction}" />
											<c:if test="${data1.entitle.smfname ne 'NA' && data1.entitle.smfname ne '#'}">
										<li class="folder " id="${data1.entitle.smfid}"><a
											title="${data1.entitle.smfname}"
											<c:choose><c:when test="${fn:containsIgnoreCase(action1 , '.html')}">onclick="openRelatedForm('${action1}','','${data.entitle.smfname}','${data1.entitle.smShortDesc}');breadcrumb('${masters.entitle.smfname}+${data1.entitle.smfname}+${data1.entitle.smfname}',${masters.entitle.smfid});" href="#"</c:when><c:otherwise>href="${action1}"</c:otherwise></c:choose>>${data1.entitle.smfname}</a>
											<ul>
												<c:forEach items="${menuRoleEntitlement.childList}" var="data2">
													<c:if test="${data1.entitle.smfid eq  data2.parentId}">
														<c:set var="action2" value="${data2.entitle.smfaction}" />
														<li class="folder" id="${data2.entitle.smfid}"><a
															role="link" title="${data2.entitle.smfname}"
															<c:choose><c:when test="${fn:containsIgnoreCase(action2 , '.html')}">onclick="openRelatedForm('${action2}');breadcrumb('${masters.entitle.smfname}+${data.entitle.smfname}+${data1.entitle.smfname}+${data2.entitle.smfname}',${masters.entitle.smfid});" href="#"</c:when><c:otherwise>href="${action2}"</c:otherwise></c:choose>>${data2.entitle.smfname}</a>
															<ul>
																<c:forEach items="${menuRoleEntitlement.childList}"
																	var="data3">
																	<c:if test="${data2.entitle.smfid eq  data3.parentId}">
																		<c:set var="action3"
																			value="${data3.entitle.smfaction}" />
																		<li class="folder" id="${data3.entitle.smfid}"><a
																			role="link" title="${data3.entitle.smfname}"
																			<c:choose><c:when test="${fn:containsIgnoreCase(action3 , '.html')}">onclick="openRelatedForm('${action3}');breadcrumb('${masters.entitle.smfname}+${data.entitle.smfname}+${data1.entitle.smfname}+${data2.entitle.smfname}+${data3.entitle.smfname}',${masters.entitle.smfid});" href="#"</c:when><c:otherwise>href="${action3}"</c:otherwise></c:choose>>${data3.entitle.smfname}</a></li>
																		<%-- 	<ul>
																				<c:forEach items="${menuRoleEntitlement.childList}"
																					var="data4">
																					<c:if
																						test="${data3.entitle.smfid eq  data4.parentId}">
																						<c:set var="action4"
																							value="${data4.entitle.smfaction}" />
																						<li class="folder" id="${data4.entitle.smfid}"><a
																							role="link" title="${data4.entitle.smfname}"
																							<c:choose><c:when test="${fn:containsIgnoreCase(action4 , '.html')}">onclick="openRelatedForm('${action4}');breadcrumb('${masters.entitle.smfname}+${data.entitle.smfname}+${data1.entitle.smfname}+${data2.entitle.smfname}+${data3.entitle.smfname}+${data4.entitle.smfname}',${masters.entitle.smfid});" href="#"</c:when><c:otherwise>href="${action4}"</c:otherwise></c:choose>>${data4.entitle.smfname}</a></li>
																					</c:if>
																				</c:forEach>
																			</ul> --%>
																		</c:if>
																</c:forEach>
															</ul></li>
													</c:if>
												</c:forEach>
											</ul></li>
											</c:if>
									</c:if>
								</c:forEach>
							</ul></li>
							
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:forEach>