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
		<c:choose>
			<c:when test="${totalResults != 0}">
				<table>
					<tr>
						<th>Title</th>
						<th>Year</th>
						<th>IMDb</th>
					</tr>
					<c:forEach var="m" items="${movies}">
						<tr>
							<td>${m.title}</td>
							<td>${m.year}</td>
							<td><a href="http://www.imdb.com/title/${m.imdbID}/">link</a></td>
						</tr>
					</c:forEach>
				</table>

				<p>
					${page*10-9}-${page*10 < totalResults ? page*10 : totalResults} of ${totalResults} results
				</p>
				<p>
					<c:if test="${page > 1}">
						<a href="search?title=${title}&page=${page-1}">Previous</a>
					</c:if>
					<c:if test="${page*10 < totalResults}">
						<a href="search?title=${title}&page=${page+1}">Next</a>
					</c:if>
				</p>
			</c:when>
			<c:otherwise>
				<p>
					There don't appear to be any!
				</p>
			</c:otherwise>
		</c:choose>
		<p>
			<a href="index.jsp">Home</a>
		</p>
	</center>
</body>
</html>