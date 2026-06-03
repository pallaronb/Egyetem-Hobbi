<?php
session_start();
include_once("projectStorage.php");
$ps = new projectStorage();
$projects = $ps -> findAll();
if(!isset($_SESSION['user'])){
    header("Location: index.php");
    exit();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Saját projekteim</title>
</head>
<body>
    <h2>Saját projekteim</h2>
    <a href="index.php"><button>Főoldalra</button></a>
    <table>
            <tr>
                <th>Projekt neve</th>
                <th>Kategória</th>
                <th>Státusz</th>
            </tr>
            <?php $numberOfProjectVotedFor = 0; ?>
            <?php foreach($projects as $p): ?>
            <tr>
            <?php if($p['owner'] == $_SESSION['user']['id'] && $p['status'] !== 'approved'): ?>
                <td> <a href="project.php?id=<?php echo $p['id']?>"><?php echo $p['title']; ?></a> </td>
                <td>
                    <?php 
                    switch($p['category']){
                        case '0':
                            echo "Helyi kis projekt";
                            break;
                        case '1':
                            echo "Helyi nagy projekt";
                            break;
                        case '2':
                            echo "Esélyteremtő Budapest";
                            break;
                        case '3':
                            echo "Zöld Budapest";
                            break;
                    }
                    ?>
                </td>
                <?php 
                    if($p['status'] === 'approved'){
                        ?><td><p style="color: green;">Elfogadva</p></td><?php
                    } elseif($p['status'] === 'rejected'){
                        ?><td><p style="color: red;">Elutasítva</p></td><?php
                    }elseif($p['status'] === 'pending'){
                        ?><td><p style="color: orange;">Döntésre vár</p></td><?php
                    }elseif($p['status'] === 'rework'){
                        ?><td><p style="color: blue;">Visszaküldve átdolgozásra</p></td><?php
                    }
                ?>
                <?php endif; ?>
            <?php endforeach; ?>
            </tr>
        </table>

</body>
</html>