<?php
session_start();
unset($_SESSION['project']);
include_once("projectStorage.php");
include_once("voteStorage.php");
$ps = new projectStorage();
$vs = new voteStorage();
$votes = $vs -> findAll();
$cat = $_POST['categories'] ?? 'all';
if($cat != 'all'){
    switch($cat){
        case 'small':
            $filterCat = 0;
            break;
        case 'big':
            $filterCat = 1;
            break;
        case 'opportunity':
            $filterCat = 2 ;
            break;
        case 'green':
            $filterCat = 3;
            break;
        default:
            $filterCat = null;
            break;
    }
    $projects = $ps -> findAll(['category' => $filterCat]);
}
else{
    $projects = $ps -> findAll();
}
?>
<!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Főoldal</title>
</head>
<body>
    <h2>Helló, <?=$_SESSION['user']['username'] ?? 'Vendég' ?></h2>
    <form action="index.php" method="POST">
    <label for="categories">Szűrő:</label>
    <select name="categories" id="categories">
        <option value="all" <?= $cat === 'all' ? 'selected' : '' ?>>Összes projekt</option>
        <option value="small" <?= $cat === 'small' ? 'selected' : '' ?>>Helyi kis projekt</option>
        <option value="big" <?= $cat === 'big' ? 'selected' : '' ?>>Helyi nagy projekt</option>
        <option value="opportunity" <?= $cat === 'opportunity' ? 'selected' : '' ?>>Esélyteremtő Budapest</option>
        <option value="green" <?= $cat === 'green' ? 'selected' : '' ?>>Zöld Budapest</option>
    </select>
    <button type="submit">Szűrés</button>
</form>
    <?php if (isset($_SESSION['vote_error'])): ?>
    <div style="color: red;">
        <?= htmlspecialchars($_SESSION['vote_error']) ?>
    </div>
    <?php unset($_SESSION['vote_error']); ?>
    <?php endif; ?>
    <form action="save_votes.php" method="POST">
        <table>
            <tr>
                <?php
                if(!isset($_SESSION['user'])): ?>
                    <th>Projekt neve</th>
                    <th>Szavazatok száma</th>
                    <th>Kategória</th>
                <?php else: ?>
                <th>Projekt neve</th>
                <th>Szavazatok száma</th>
                <th>Szavazó doboz</th>
                <th>Kategória</th>
                <?php endif; ?>
            </tr>
            <?php $numberOfProjectVotedFor = 0; ?>
            <?php foreach($projects as $p): ?>
            <?php $voteCount = 0;
            ?>
            <?php
            if(isset($_SESSION['user']) && $_SESSION['user']['is_admin'] == true):
                if((isset($p['status']) && $p['status'] == 'approved') || (isset($_SESSION['user']) && $p['owner'] == $_SESSION['user']['id'] || $_SESSION['user']['is_admin'] == true)): ?>
                <tr>
                    <td> <a href="project.php?id=<?php echo $p['id']?>"> <?= htmlspecialchars($p['title'], ENT_QUOTES, 'UTF-8'); ?> </a> </td>
                            <?php if(isset($votes)):
                                foreach($votes as $vote):
                                    if($vote['project1'] == $p['id'] || $vote['project2'] == $p['id'] || $vote['project3'] == $p['id']):
                                     $voteCount += 1;?>
                                    <?php endif;
                                endforeach; ?>
                                <td><?= $voteCount ?></td>
                                <?php 
                                    switch($p['category']){
                                        case '0': $name = "small_selected_projects[]"; break;
                                        case '1': $name = "big_selected_projects[]"; break;
                                        case '2': $name = "opp_selected_projects[]"; break;
                                        case '3': $name = "green_selected_projects[]"; break;
                                    }
                                ?>
                                <td>
                                    <input type="checkbox" name="<?= $name ?>" value="<?= $p['id'] ?>">
                                </td>
                            <?php endif; ?>
                                <td>
                                    <?php
                                    switch($p['category']){
                                        case '0': echo "Helyi kis projekt"; break;
                                        case '1': echo "Helyi nagy projekt"; break;
                                        case '2': echo "Esélyteremtő Budapest"; break;
                                        case '3': echo "Zöld Budapest"; break;
                                    }
                                    ?>
                                </td>
                </tr>
                <?php endif; ?>
            <?php
            elseif(isset($_SESSION['user']) && $_SESSION['user']['is_admin'] == false):
                if((isset($p['status']) && $p['status'] == 'approved') || (isset($_SESSION['user']) && $p['owner'] == $_SESSION['user']['id'])): ?>
                <tr>
                    <td> <a href="project.php?id=<?php echo $p['id']?>"><?= htmlspecialchars($p['title'], ENT_QUOTES, 'UTF-8'); ?></a> </td>
                            <?php if(isset($votes)):
                                foreach($votes as $vote):
                                    if($vote['project1'] == $p['id'] || $vote['project2'] == $p['id'] || $vote['project3'] == $p['id']):
                                     $voteCount += 1;?>
                                    <?php endif;
                                endforeach; ?>
                                <td><?= $voteCount ?></td>
                                <td>
                                    <?php
                                    switch($p['category']){
                                        case '0': echo "Helyi kis projekt"; break;
                                        case '1': echo "Helyi nagy projekt"; break;
                                        case '2': echo "Esélyteremtő Budapest"; break;
                                        case '3': echo "Zöld Budapest"; break;
                                    }
                                    ?>
                                </td>
                            <?php endif; ?>
                </tr>
                <?php endif; ?>
            <?php else:
                if((isset($p['status']) && $p['status'] == 'approved')): ?>
                <tr>
                    <td> <a href="project.php?id=<?php echo $p['id']?>"><?= htmlspecialchars($p['title'], ENT_QUOTES, 'UTF-8'); ?></a> </td>
                            <?php if(isset($votes)):
                                foreach($votes as $vote):
                                    if($vote['project1'] == $p['id'] || $vote['project2'] == $p['id'] || $vote['project3'] == $p['id']):
                                     $voteCount += 1;?>
                                    <?php endif;
                                endforeach; ?>
                                <td><?= $voteCount ?></td>
                                <td>
                                    <?php
                                    switch($p['category']){
                                        case '0': echo "Helyi kis projekt"; break;
                                        case '1': echo "Helyi nagy projekt"; break;
                                        case '2': echo "Esélyteremtő Budapest"; break;
                                        case '3': echo "Zöld Budapest"; break;
                                    }
                                    ?>
                                </td>
                            <?php endif; ?>
                <?php endif; ?>
            <?php endif; ?>
            </tr>
            <?php endforeach; ?>
        </table>
    </form>
    <?php if(!isset($_SESSION['user'])): ?>
        <a href="login.php"><button>Bejelentkezés</button></a>
        <a href="register.php"><button>Regisztráció</button></a>
    <?php endif; ?>
    <br></br>
    <?php if(isset($_SESSION['user'])): ?>
        <a href="projects-own.php"><button>Saját projektek</button></a>
        <?php if($_SESSION['user']['is_admin']): ?>
        <a href="projects-admin.php"><button>Ítélkezésre váró projektek</button></a>
        <?php endif; ?>
        <a href="create-project.php"><button>Új projekt létrehozása</button></a>
        <br></br>
        <button type="submit">Szavazás</button>
        <?php if($numberOfProjectVotedFor > 3):?>
            <p style="color:red;">Már leadta a szavazatát a három projektre!</p>
        <?php endif; ?>
        <a href="logout.php"><button>Kijelentkezés</button></a>
    <?php endif; ?>
</body>
</html>