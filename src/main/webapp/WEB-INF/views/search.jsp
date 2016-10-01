<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results - Find movies that have "${title}" in the title</title>
</head>
<body>
	<center>
		<h1>Movies that have "${title}" in the title:</h1>
		<table>
			${tableRows}
		</table>
		<table>
			<c:forEach var="m" items="${movies}">
				<tr>
					<td>${m.title}</td>
					<td>${m.year}</td>
					<td><a href="http://www.imdb.com/title/${m.imdbID}/">IMDb</a></td>
				</tr>
			</c:forEach>
		</table>

	</center>
</body>
</html>