<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="table-responsive">
  <h4 class="modal-title" id="employeeHistoryModalLabel">
                    Employee History
                </h4>
	<table class="table table-bordered table-striped table-hover">

		<thead>
			<tr>
				<th style="width: 60px;">Sr. No.</th>
				<th>Mobile No.</th>
				<th>Name</th>
				<th>Role Code</th>
				<th>Previous Remark</th>
				<th>Email ID</th>
			</tr>
		</thead>

		<tbody>

			<c:choose>

				<c:when test="${not empty History}">

					<c:forEach var="history" items="${History}" varStatus="status">

						<tr>
							<td>${status.index + 1}</td>

							<td><c:out value="${history.empmobno}" /></td>

							<td><c:out value="${history.empname}" /></td>

							<td><c:out value="${history.empRoleFlag}" /></td>

							<td><c:out value="${history.empRemark}" /></td>

							<td><c:out value="${history.autEmail}" /></td>
						</tr>

					</c:forEach>

				</c:when>

				<c:otherwise>

					<tr>
						<td colspan="6" class="text-center">No employee history
							found.</td>
					</tr>

				</c:otherwise>

			</c:choose>

		</tbody>

	</table>

</div>