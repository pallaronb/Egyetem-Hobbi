<?php
session_start();
if(!isset($_SESSION['user']) || $_SESSION['user']['is_admin'] != true){
    header("Location: index.php");
    exit();
}
include_once("projectStorage.php");
$ps = new projectStorage();
$current_project = $ps -> findById($_POST['project_id']);
if($current_project && isset($_POST['project_id'])){
    $newStatus = $_POST['newStatus'] ?? "";
    $project = $ps -> findById($_POST['project_id']);
    if($project && $_POST['newStatus'] == 'approved'){
        $ps -> update($_POST['project_id'],
        ['status' => $newStatus,
        "title" => $current_project['title'] ?? null,
        "description" => $current_project['description'] ?? null,
        "category" => $current_project['category'] ?? null,
        "postal_code" => $current_project['postal_code'] ?? null,
        "image" => $current_project['image'] ?? null,
        "owner" => $current_project['owner'] ?? null,
        "submitted" => $current_project['submitted'] ?? null,
        "approved" => date("Y-m-d H:i"),
        "id" => $current_project['id'] ?? null
        ]);
    }
    else {
        $ps -> update($_POST['project_id'],
        ['status' => $_POST['newStatus'],
        "title" => $current_project['title'] ?? null,
        "description" => $current_project['description'] ?? null,
        "category" => $current_project['category'] ?? null,
        "postal_code" => $current_project['postal_code'] ?? null,
        "image" => $current_project['image'] ?? null,
        "owner" => $current_project['owner'] ?? null,
        "submitted" => $current_project['submitted'] ?? null,
        "approved" => null,
        "id" => $current_project['id'] ?? null
        ]);
    }
}
header("Location: projects-admin.php");
exit();
