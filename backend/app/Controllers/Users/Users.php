<?php

namespace App\Controllers\Users;

use App\Controllers\Token;
use App\Models\Users_Model;
use CodeIgniter\RESTful\ResourceController;

class Users extends ResourceController
{
  public function __construct()
  {
    $this->users = new Users_Model();
    $this->validation = \Config\Services::validation();
  }

  /**
   * --------------------------------------------------------------------
   * getUsers
   * --------------------------------------------------------------------
   * Jika id = null akan mengembalikan data list seluruh admin
   * Jika id != null akan mengembalikan data admin berdasarkan id 
   * 
   * @param  mixed $id
   * @return void
   */
  public function getUsers($id = null)
  {
    try {
      Token::checkHeader($this->request->getServer('HTTP_AUTHORIZATION'));
      $data = $this->users->getUser($id);
      if ($data === null) throw new \Exception("No users found", 404);
      $output = [
        'status' => 200,
        'message' => 'Successful',
        'data' => $data
      ];
      return $this->respond($output, 200);
    } catch (\Exception $e) {
      $output = [
        'status' => $e->getCode(),
        'message' => $e->getMessage(),
      ];
      return $this->respond($output, $e->getCode());
    }
  }

  /**
   * --------------------------------------------------------------------
   * Update
   * --------------------------------------------------------------------
   * Ini untuk mengupdate data users, sementara yang bisa di update hanya
   * email, nama lengkap, sama username
   * @param  mixed $id
   * @return void
   */
  public function update($id = null)
  {
    try {
      $authorized = Token::checkHeader($this->request->getServer('HTTP_AUTHORIZATION'));
      if ($authorized->id != $id) throw new \Exception("Forbidden", 403);

      $json = $this->request->getJSON();

      $updateData = [
        'email'    => $json->email,
        'name'     => $json->name,
        'username' => $json->username,
      ];

      $this->validation->run($updateData, 'update');
      foreach ($updateData as $key => $item) {
        if ($this->validation->hasError($key))
          throw new \Exception($this->validation->getError($key), 401);
      };

      $this->users->updateUsers($id, $updateData);

      $output = [
        'status'  => 200,
        'message' => 'Update Was Successful',
      ];
      return $this->respond($output, 200);
    } catch (\Exception $e) {
      $output = [
        'status'  => $e->getCode(),
        'message' => $e->getMessage(),
      ];
      return $this->respond($output, $e->getCode());
    }
  }
}
