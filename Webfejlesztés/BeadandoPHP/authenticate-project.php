<?php
session_start();
include_once("projectStorage.php");
$ps = new projectStorage();
$errors = [];
$old = [];
$old['name'] = $_POST['name'] ?? "";
$old['description'] = $_POST['description'] ?? "";
$old['postcode'] = $_POST['postcode'] ?? "";
$old['image'] = $_POST['image'] ?? "";

function startsWithOne($str){
    return isset($str[0]) && $str[0] === '1';
}

function isFourDigitNumber($str){
    return preg_match('/^\d{4}$/', $str);
}

function isValidDistrict($str){
    if(isset($str[1]) && isset($str[2])){
        $num = $str[1] . $str[2];
        $districtNum = (int)$num;
        return $districtNum >= 1 && $districtNum <= 23;
    }
    return false;
}

function isValidPostalCode($str){
    if(!isFourDigitNumber($str)){
        return false;
    }
    if(startsWithOne($str)){
        return isValidDistrict($str);
    }
    return true;
}

if($_POST){
    $name = trim($_POST['name'] ?? "");
    $description = trim($_POST['description'] ?? "");
    $category = trim($_POST['category'] ?? "");
    $postcode = trim($_POST['postcode'] ?? "");
    $image = trim($_POST['image'] ?? "");

    switch($category){
        case 'small':
            $nemCat = 0;
            break;
        case 'big':
            $nemCat = 1;
            break;
        case 'opportunity':
            $nemCat = 2;
            break;
        case 'green':
            $nemCat = 3;
            break;
        default:
            $errors[] = "Érvénytelen kategória!";
    }

    if(strlen($name) < 10){
        $errors[] = "A projekt nevének legalább 10 karakter hosszúnak kell lennie!";
    }

    if(strlen($description) < 150){
        $errors[] = "A leírásnak legalább 150 karakter hosszúnak kell lennie!";
    }

    if($postcode !== "1007" && !isValidPostalCode($postcode)){
        $errors[] = "Az irányítószám nem megfelelő!";
    }

    if(!empty($image) && !filter_var($image, FILTER_VALIDATE_URL)){
        $errors[] = "A kép URL nem érvényes!";
    }

    if(empty($errors)){
        $ps -> add([
            'title' => $name,
            'description' => $description,
            'category' => $nemCat,
            'postal_code' => $postcode,
            'image' => $image,
            'status' => 'pending',
            'owner' => $_SESSION['user']['id'] ?? null,
            'submitted' => date('Y-m-d H:i'),
            'approved' => null
        ]);
        header("Location: index.php");
        exit();
    }
    else{
        $_SESSION['create_project_errors'] = $errors;
        $_SESSION['form_data'] = $_POST;
        header("Location: create-project.php");
        exit();
    }
}