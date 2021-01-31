<?php

namespace App\Controllers\Users;

use App\Models\Users_Model;
use App\Controllers\Token;
use CodeIgniter\RESTful\ResourceController;

class Auth extends ResourceController
{
  public function __construct()
  {
    $this->model = new Users_Model();
    $this->validation = \Config\Services::validation();
  }

  /**
   * --------------------------------------------------------------------
   * Register
   * --------------------------------------------------------------------
   * Register users dengan request berupa JSON
   * dengan isi email, name, username, password, dan pass_confirm
   * Dilakukan pengecekan juga menggunakan validasi
   * 
   * @return respond
   */
  public function register()
  {
    try {
      // get respons request
      $json = $this->request->getJSON();

      // destructuring json data
      $email = $json->email;
      $name = $json->name;
      $username = $json->username;
      $password = $json->password;
      $pass_confirm = $json->pass_confirm;

      // make validation data
      $validateData = [
        'email' => $email,
        'name' => $name,
        'username' => $username,
        'password' => $password,
        'pass_confirm' => $pass_confirm
      ];

      // run validation on validation data
      $this->validation->run($validateData, 'register');
      foreach ($validateData as $key => $data) {
        if ($this->validation->hasError($key))
          throw new \Exception($this->validation->getError($key), 401);
      };

      
      $password_hash = password_hash($password, PASSWORD_BCRYPT);
      $registerData = [
        'email'    => $email,
        'name'     => $name,
        'username' => $username,
        'password' => $password_hash,
      ];

      // insert to database
      $this->model->register($registerData);

      // if success return 201 CREATED respons
      $output = [
        'status' => 201,
        'message' => 'Register Success',
      ];
      return $this->respond($output, 201);

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
   * Login
   * --------------------------------------------------------------------
   * Login bagi yang sudah membuat dengan request berupa JSON
   * dengan isi username, password.
   * Dilakukan pengecekan juga menggunakan validasi
   *
   * @return respond
   */
  public function login()
  {
    try {
      $json = $this->request->getJSON();
      $username = $json->username;
      $password = $json->password;

      $loginData = [
        'username' => $username,
        'password' => $password
      ];

      $this->validation->run($loginData, 'login');
      foreach ($loginData as $key => $data) {
        if ($this->validation->hasError($key))
          throw new \Exception($this->validation->getError($key), 401);
      };

      $login = $this->model->checkLogin($username);

      $verified = $login && password_verify($password, $login->password);
      if (!$verified) throw new \Exception('Wrong username or Password', 401);

      $token = Token::encode($login->id, $login->username);

      $output = [
        'status' => 200,
        'message' => 'Login Success',
        "data" => [
          "id" => $login->id,
          "token" => $token,
        ],
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
   * validateToken
   * 
   * di cek apakah token masih bisa dipakai, valid, dan belum expired
   *
   * @return void
   */
  public function validateToken(){
    try{
      $authorized = Token::checkHeader($this->request->getServer('HTTP_AUTHORIZATION'));
      $token = Token::encode($authorized->id, $authorized->username);
      $output = [
        'status' => 200,
        'message' => 'Token Valid',
        "data" => [
          "id" => $authorized->id,
          "token" => $token,
        ],
      ];
      return $this->respond($output, 200);
    } catch(\Exception $e){
      $output = [
        'status' => $e->getCode(),
        'message' => $e->getMessage(),
      ];
      return $this->respond($output, $e->getCode());
    }
  }
}
