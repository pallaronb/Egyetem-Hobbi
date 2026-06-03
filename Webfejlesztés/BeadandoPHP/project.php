<?php
session_start();
include_once('projectStorage.php');
include_once('userStorage.php');
$us = new userStorage();
$ps = new projectStorage();
$project = $_SESSION['project'] = $ps -> findById($_GET['id'] ?? null);
if(!$project){
    header("Location: index.php");
    exit();
}
if(!isset($_SESSION['user'])){
    if($project['status'] != 'approved'){
    header("Location: index.php");
    exit();
}
}else{
    if($project['status'] != 'approved' && $_SESSION['user']['is_admin'] == false && $project['owner'] != $_SESSION['user']['id']){
        header("Location: index.php");
        exit();
    }
}
$id = $_GET['id'] ?? null;
$isLoggedIn = isset($_SESSION['user']);
$isAdmin = $isLoggedIn && $_SESSION['user']['is_admin'] == true;
$user = $isLoggedIn ? $_SESSION['user'] : null;
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Projekt</title>
</head>
<body>
    <nav>
        <a href="index.php">Főoldal</a>
        <?php if($isLoggedIn): ?>
            <a href="logout.php"><button>Kijelentkezés</button></a>
        <?php else: ?>
            <a href="login.php"><button>Bejelentkezés</button></a>
            <a href="register.php"><button>Regisztráció</button></a>
        <?php endif; ?>
        <form action="update_project_status.php" method="POST">
            <input type="hidden" name="project_id" value="<?= $project['id'] ?? '' ?>">
            <?php 
            if($isAdmin):?>
                <select name="newStatus" id="newStatus">
                    <option value="approved" <?= ($project['status'] === 'approved') ? 'selected' : '' ?>>Elfogad</option>
                    <option value="rejected" <?= ($project['status'] === 'rejected') ? 'selected' : '' ?>>Elutasít</option>
                    <option value="rework"   <?= ($project['status'] === 'rework')   ? 'selected' : '' ?>>Újra</option>
                    <option value="pending"  <?= ($project['status'] === 'pending')  ? 'selected' : '' ?>>Várakozik</option>
                </select>
                <button type="submit">Státusz frissítése</button>
            <?php endif; ?>
        </form>
    </nav>
    <?php
    if($project):
    ?>
    <ul>
        <?php
        foreach($project as $key => $value):
            $owner = $us -> findById($project['owner'] ?? null);
            $approvedWhen = $project['approved'] ?? null;
                switch($key){
                    case 'title':
                        ?><li>Projekt neve: <?= htmlspecialchars($value, ENT_QUOTES, 'UTF-8') ?></li><?php
                        break;
                    case 'description':
                        ?><li>Leírás: <?= nl2br(htmlspecialchars($value, ENT_QUOTES, 'UTF-8')) ?></li><?php
                        break;
                    case 'category':
                    switch($value){
                        case 1:
                            $catName = 'Nagy projekt';
                            break;
                        case 2:
                            $catName = 'Lehetőség projekt';
                            break;
                        case 3:
                            $catName = 'Zöld projekt';
                            break;
                        default:
                            $catName = 'Kis projekt';
                    }
                    ?><li>Kategória: <?= $catName ?></li><?php
                    break;
                    case 'postal_code':
                        ?><li>Irányítószám: <?= htmlspecialchars($value, ENT_QUOTES, 'UTF-8') ?></li><?php
                        break;
                    case 'image':
                        ?><li>Kép: <img src="<?= htmlspecialchars($value, ENT_QUOTES, 'UTF-8') ?>" alt="Project Image"></li><?php
                        break;
                    case 'owner':
                        ?><li>Tulajdonos: <?= htmlspecialchars($owner['username'] ?? 'Ismeretlen', ENT_QUOTES, 'UTF-8') ?></li><?php
                        break;
                    case 'submitted':
                        ?><li>Leadva: <?= htmlspecialchars($value, ENT_QUOTES, 'UTF-8') ?></li><?php
                        break;
                    case 'approved':
                        ?><li>Jóváhagyva: <?= htmlspecialchars($approvedWhen ?? 'Még nincs jóváhagyva', ENT_QUOTES, 'UTF-8') ?></li><?php
                        break;
                }
                ?>
        <?php endforeach;?>
        </ul>
        <?php else: ?>
        <p>A projekt nem található!</p>
        <?php
    endif;
        ?>
</body>
</html>