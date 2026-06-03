<?php
include_once("storage.php");

class VoteStorage extends Storage {
  public function __construct() {
    parent::__construct(new JsonIO('votes.json'));
  }
}