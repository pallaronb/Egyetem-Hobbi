<?php
session_start();
include_once("userStorage.php");
$us = new userStorage();

$errors = [];

if($_POST){
    $username = trim($_POST['username'] ?? "");
    $password = $_POST['password'] ?? "";
    $passwordAgain = $_POST['passwordAgain'] ?? "";
    $email = trim($_POST['email'] ?? "");

    if($password !== $passwordAgain){
        $errors[] = "A jelszavak nem egyeznek!";
    }

    if($us->findOne(['username' => $username]) && !empty($username)){
        $errors[] = "A felhasználónév már létezik!";
    }

    if($username !== trim($username) || empty($username) || str_contains($username, ' ')){
        $errors[] = "A felhasználónévben nem lehet szóköz!";
    }

    if(!str_contains($email, '@')){
        $errors[] = "Érvénytelen e-mail cím!";
    }

    if(strlen($password) < 8){
        $errors[] = "A jelszónak legalább 8 karakter hosszúnak kell lennie!";
    }

    if(!checkLetters($password)){
        $errors[] = "A jelszónak tartalmaznia kell nagy- és kisbetűt és számjegyet is!";
    }

    if(empty($errors)){
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $us -> add([
            'username' => $username,
            'password' => $hashedPassword,
            'email' => $email,
            'is_admin' => false]);
        header("Location: login.php");
        exit();
    }
    else{
        $_SESSION['registration_errors'] = $errors;
        $_SESSION['form_data'] = $_POST;
        header("Location: register.php");
        exit();
    }
}

function checkLetters($str){
    $upperCase = preg_match('/[A-Z]/', $str);
    $lowerCase = preg_match('/[a-z]/', $str);
    $number = preg_match('/[0-9]/', $str);
    return ($upperCase && $lowerCase && $number);
}