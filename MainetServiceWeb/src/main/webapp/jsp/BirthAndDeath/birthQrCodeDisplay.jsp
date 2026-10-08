<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="apptags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">



<link href="assets/css/print.css" media="print" rel="stylesheet"
	type="text/css" />
<!-- <link href="assets/css/printing-page.css" media="print" rel="stylesheet" type="text/css"/> -->
<script src="assets/libs/jquery-datatables/js/jquery.dataTables.min.js"></script>
<script src="assets/libs/jquery-datatables/js/dataTables.bootstrap.js"></script>
<script type="text/javascript" src="js/mainet/validation.js"></script>
<script type="text/javascript" src="js/mainet/file-upload.js"></script>
<script type="text/javascript"
	src="js/birthAndDeath/printCertificate.js"></script>
<jsp:useBean id="marathiConvert"
	class="com.abm.mainet.common.utility.Utility"></jsp:useBean>
<style>
        body {
           /*  background-color: #f0f8ff;  */
            background-color: #ffffff; 
            font-family: "Roboto", sans-serif; 
/*             font-family: Arial, sans-serif;  */
        }

        h1 {
            text-align: center;
            color: #333;
        }

        table {
            width: 90%;
/*             width: 80%; */
            margin: 20px auto;
            border-collapse: collapse;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 7px;
/*             padding: 12px; */
            text-align: left;
            border: 1px solid #ddd;
            color: #555;
            font-size: 0.9em;
        }

       th {
    background-color: #157eb8;
    color: #ffff;
}

        tr:nth-child(even) {
            background-color: #f2f2f2; /* Light gray for even rows */
        }

        tr:hover {
            background-color: #e0e0e0; /* Gray background on hover */
        }
 h2.text-center {
    text-align: center;
    color: #555;
}
.border-div{
    margin: 80px 10%;
    text-align: left;
    /* background: rgb(211, 230, 249); */
    /* padding: 10px 20px 20px 20px; */
    color: #fff;
    /* border: 0px solid rgba(153, 153, 153, 0.4); */
    box-shadow: 0 24px 38px 3px rgba(0, 0, 0, 0.14), 0 9px 46px 8px rgba(0, 0, 0, 0.12), 0 11px 15px -7px rgba(0, 0, 0, 0.2);
    /* border-radius: 10px; */
    border: 2px solid #157eb8;
}
    </style>


<script>
	
</script>
</head>
<body>
    <!-- Start Content here -->
    <div class="content animated slideInDown border-div" id="printDiv">
        <!-- Start info box -->
        <!-- <h1>Scanned QR Code Data</h1> -->
        <h2 class="text-center"><spring:message code="birth.auth.title"
						text="Authorized Birth Details" /></h2>
        <table>

			<tr>
				<th><spring:message code="birth.certificate.regNo"
						text="Registration Number" /></th>
				<td>${ragistrationNo}</td>
			</tr>

			
			<tr>
                <th><spring:message code="birth.certificate.childName" text="Child Name" /></th>
                <td>${childName}</td>
            </tr>
            <tr>
                <th><spring:message code="birth.certificate.dob" text="Date Of Birth" /></th>
                <td>${dateOfBirth}</td>
            </tr>
            <tr>
                <th><spring:message code="BirthRegistrationDTO.brSex" text="Gender" /></th>
                <td>${gender}</td>
            </tr>
            <tr>
                <th><spring:message code="birth.qrCode.fatherName" text="Father Name" /></th>
                <td>${fatherName}</td>
            </tr>
            <tr>
                <th><spring:message code="birth.qrCode.mothersName" text="Mother Name" /></th>
                <td>${motherName}</td>
            </tr>
            <tr>
                <th><spring:message code="birth.certificate.birthPlace" text="Place of Birth" /></th>
                <td>${birthPlace}</td>
            </tr>
            <tr>
				<th><spring:message code="birth.certificate.regDate"
						text="Date of Registration" /></th>
				<td>${ragistrationNoDate}</td>
			</tr>
           
        </table>
    </div>
    <!-- End content here -->
</body>
</html>



