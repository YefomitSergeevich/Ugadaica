<#-- @ftlvariable name="entries" type="kotlin.collections.List<com.jetbrains.handson.website.BlogEntry>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Угадайка</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<img src="/static/ktor.png">
<h1>Игра Угадайка</h1>
<p><i>Угадай число от 1 до 100</i></p>
<hr>
<#list entries as item>

        <h3>${item.numGuess}</h3>
        <p>${item.numAnswer}</p>

</#list>
<hr>
<div>
    <h3>У тебя 7 попыток</h3>
    <form action="/submit" method="post">
        <input name="guess">
        <br><br>
        <input type="submit" value="Отправить">
    </form>
    <form action="/restart" method="post">
        <br>
        <input type="submit" value="Сдаться">
    </form>
    <form action="/choice" method="get">
        <br>
        <input type="submit" value="Задать свое значение">
    </form>
</div>
</body>
</html>