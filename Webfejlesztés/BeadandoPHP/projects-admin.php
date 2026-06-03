<?php
session_start();
include_once("projectStorage.php");
$ps = new projectStorage();
$projects = $ps -> findAll();
if($_SESSION['user']['is_admin'] != true){
    header("Location: index.php");
    exit();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin projektek</title>
</head>
<body>
    <h2>Admin projektek</h2>
    <a href="index.php"><button>Főoldalra</button></a>
    <table>
            <tr>
                <th>Projekt neve</th>
                <th>Kategória</th>
                <th>Státusz</th>
            </tr>
            <?php foreach($projects as $p): ?>
                <?php if($p['status'] === 'pending'):?>
            <tr>
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
                <td>
                    <?php 
                        ?><p style="color: orange;">Döntésre vár</p><?php
                    ?>
                </td>
                <?php endif; ?>
            </tr><?php endforeach; ?>
        </table>
</body>
</html>