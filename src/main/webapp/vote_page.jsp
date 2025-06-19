<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Голосование</title>
</head>
<body>

<c:if test="${empty submitted}">
    <h1>Форма голосования</h1>
    <form action="vote" method="post">
        <fieldset>
            <legend><b>Выберите любимого исполнителя</b></legend>
            <label><input type="radio" name="singer" value="Tool" required> Tool</label><br>
            <label><input type="radio" name="singer" value="Metallica"> Metallica</label><br>
            <label><input type="radio" name="singer" value="Megadeth"> Megadeth</label><br>
        </fieldset>

        <br>

        <fieldset>
            <legend><b>Выберите 3–5 любимых поджанров</b></legend>
            <label><input type="checkbox" name="genre" value="Doom"> Doom</label><br>
            <label><input type="checkbox" name="genre" value="Groove"> Groove</label><br>
            <label><input type="checkbox" name="genre" value="Thrash"> Thrash</label><br>
            <label><input type="checkbox" name="genre" value="Progressive"> Progressive</label><br>
            <label><input type="checkbox" name="genre" value="Industrial"> Industrial</label><br>
        </fieldset>

        <br>

        <label for="about"><b>Расскажите о себе</b></label><br>
        <textarea name="about" id="about" rows="5" cols="50"></textarea>

        <p><input type="submit" value="Отправить"></p>
    </form>

    <c:if test="${not empty error}">
        <p style="color: red;"><b>${error}</b></p>
    </c:if>
</c:if>

<c:if test="${submitted}">
    <h1>Результаты голосования за исполнителей</h1>
    <ul>
        <c:forEach var="entry" items="${singerVotes}">
            <li>${entry.key}: ${entry.value}</li>
        </c:forEach>
    </ul>

    <h1>Результаты голосования за поджанры</h1>
    <ul>
        <c:forEach var="entry" items="${genreVotes}">
            <li>${entry.key}: ${entry.value}</li>
        </c:forEach>
    </ul>

    <h2>Комментарии:</h2>
    <div>
        ${comments}
    </div>

    <hr>
    <p><a href="vote.jsp">Назад к форме</a></p>
</c:if>

</body>
</html>
