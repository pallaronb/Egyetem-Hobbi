<?php
include_once("storage.php");

class StatusStorage extends Storage {
  public function __construct() {
    parent::__construct(new JsonIO('status.json'));
  }
}