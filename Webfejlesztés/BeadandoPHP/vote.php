<?php
session_start();
include_once("svoteStorage.php");
$vs = new VoteStorage();
$vs->add([
    'user' => $_SESSION['user']['id'],
    'project1' => $_POST['project1'] ?? null,
    'project2' => $_POST['project2'] ?? null,
    'project3' => $_POST['project3'] ?? null
]);
header("Location: index.php");
exit();