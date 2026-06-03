<?php
session_start();
include_once("projectStorage.php");
$ps = new projectStorage();

$errors = $_SESSION['create_project_errors'] ?? [];
unset($_SESSION['create_project_errors']);

$old = $_SESSION['form_data'] ?? [];
unset($_SESSION['form_data']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Új Projekt leadása</title>
</head>
<body>
    <h2>Új Projekt leadása</h2>
    <form action="authenticate-project.php" method="post">
        <label for="name">Projekt neve:</label>
        <input type="text" id="name" name="name" required value="<?= $old['name'] ?? '' ?>"><br><br>
        <label for="description">Leírás:</label>
        <input type="text" id="description" name="description" required value="<?= $old['description'] ?? '' ?>"><br><br>
        <label for="category">Kategória:</label>
        <select name="category" id="cat">
            <option value="small">Helyi kis projekt</option>
            <option value="big">Helyi nagy projekt</option>
            <option value="opportunity">Esélyteremtő Budapest</option>
            <option value="green">Zöld Budapest</option>
        </select>
        <label for="postcode">Irányítószám:</label>
        <input type="text" id="postcode" name="postcode" required value="<?= $old['postcode'] ?? '' ?>"><br><br>
        <label for="image">Kép URL-je:</label>
        <input type="text" id="image" name="image" value="<?= $old['image'] ?? '' ?>"><br><br>
        <button type="submit">Leadás</button>
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