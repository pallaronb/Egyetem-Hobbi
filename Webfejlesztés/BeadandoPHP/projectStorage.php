<?php
include_once("storage.php");

class ProjectStorage extends Storage {
  public function __construct() {
    parent::__construct(new JsonIO('jsons/projects.json'));
  }
}