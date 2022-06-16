<#-- @ftlvariable name="entries" type="kotlin.collections.List<com.jetbrains.handson.website.BlogEntry>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Угадайка</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<br><br>
<h3>Тут можно задать свое значение</h3>
<div>
    <p><i>Не знаю, правда зачем тебе это, ведь так не интересно играть, но дело твое</p></i>
    <form action="/choice/change" method="post">
        <input name="newNumber">
        <br><br>
        <input type="submit" value="Отправить">
    </form>
</div>
<br><br>
<a href="/"> Назад </a>
</body>
</html>