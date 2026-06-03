<?php
session_start();
include_once("userStorage.php");
$us = new userStorage();
$error = $_SESSION['login_errors'] ?? null;
unset($_SESSION['login_errors']);
$old = $_SESSION['user'] ?? [];
unset($_SESSION['user']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bejelentkezés</title>
</head>
<body>
    <h2>Bejelentkezés</h2>
    <form action="authenticateLogin.php" method="post">
        <label for="username">Felhasználónév:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="password">Jelszó:</label>
        <input type="password" id="password" name="password" required><br><br>
        <input type="submit" value="Bejelentkezés">
    </form>
    <?php if (!empty($error)): ?>
        <p style="color: red;"><?= $error ?></p>
    <?php endif; ?>
    <br>
    <button><a href="register.php">Regisztráció</a></button>
    <br>
    <button><a href="index.php">Főoldal</a></button>
</body>
</html>