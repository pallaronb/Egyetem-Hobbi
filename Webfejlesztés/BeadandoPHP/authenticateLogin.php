<?php
session_start();
include_once("userStorage.php");
$us = new userStorage();
$error = "";
if($_POST){
    $username = $_POST['username'] ?? "";
    $password = $_POST['password'] ?? "";

    $user = $us -> findOne(['username' => $username]);
    if($user != null && password_verify($password, $user['password'])){
        $_SESSION['user'] = $user;
        header("Location: index.php");
        exit();
    } else {
        $error = "Hibás felhasználónév vagy jelszó!";
        $_SESSION['login_errors'] = $error;
        header("Location: login.php");
        exit();
    }
}