<?php
session_start();

$errors = $_SESSION['registration_errors'] ?? [];
unset($_SESSION['registration_errors']);

$old = $_SESSION['form_data'] ?? [];
unset($_SESSION['form_data']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Regisztráció</title>
</head>
<body>
    <h2>Regisztráció</h2>
    <form action="authenticateRegistration.php" method="post">
        <label for="username">Felhasználónév:</label>
        <input type="text" id="username" name="username" required value="<?= $old['username'] ?? ''?>"><br><br>
        <label for="email">E-mail cím:</label>
        <input type="email" id="email" name="email" required value="<?= $old['email'] ?? '' ?>"><br><br>
        <label for="password">Jelszó:</label>
        <input type="password" id="password" name="password" required><br><br>
        <label for="passwordAgain">Jelszó újra:</label>
        <input type="password" id="passwordAgain" name="passwordAgain" required><br><br>
        <input type="submit" value="Regisztráció">
    </form>
    <button><a href="login.php">Bejelentkezéshez</a></button>
    <button><a href="index.php">Főoldalra</a></button>
    <?php if (!empty($errors)): ?>
    <ul style="color: red;">
        <?php foreach($errors as $error): ?>
            <li><?= $error ?></li>
        <?php endforeach; ?>
    </ul>
    <?php endif; ?>
</body>
</html>