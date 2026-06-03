<?php
session_start();
include_once("voteStorage.php");
$vs = new VoteStorage();

if ($_POST && isset($_SESSION['user'])) {
    $userId = $_SESSION['user']['id'];
    
    $votes = [
        'small' => $_POST['small_selected_projects'] ?? [],
        'big'   => $_POST['big_selected_projects'] ?? [],
        'opp'   => $_POST['opp_selected_projects'] ?? [],
        'green' => $_POST['green_selected_projects'] ?? []
    ];

    foreach ($votes as $category => $selected) {
        if (count($selected) > 3) {
            $_SESSION['vote_error'] = "Egy kategóriában legfeljebb 3 projektre szavazhatsz!";
            header("Location: index.php");
            exit();
        }
    }

    $existingVote = $vs->findOne(['user' => $userId]);
    if ($existingVote) {
        $_SESSION['vote_error'] = "Ön már leadta a szavazatát!";
        header("Location: index.php");
        exit();
    }

    $vs->add([
        'user'      => $userId,
        'small_ids' => $votes['small'],
        'big_ids'   => $votes['big'],
        'opp_ids'   => $votes['opp'],
        'green_ids' => $votes['green'],
        'voted_at'  => date('Y-m-d H:i:s')
    ]);

    header("Location: index.php");
    exit();
}