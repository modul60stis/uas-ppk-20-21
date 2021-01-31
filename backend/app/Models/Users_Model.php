<?php

namespace App\Models;

use CodeIgniter\Model;

class Users_Model extends Model
{

  protected $table = "users";
  protected $primaryKey = 'id';
  protected $allowedFields = ['username', 'email', 'name'];


  /**
   * Fungsi ini untuk melakukan 
   * insert data ke dalam table users
   * 
   * @param  mixed $data
   * @return void
   */
  public function register($data)
  {
    $query = $this->db->table($this->table)->insert($data);
    // Di cek apakah ada duplikat data atau tidak
    // code 1062 itu error ketika ada duplikat data
    // pada atribut yang unique.
    // pada kasus ini atribut username bersifat unique

    $integrityError = !$query || $this->db->error()["code"] == 1062;
    if ($integrityError) throw new \Exception("Your username has already taken", 401);
    return true;
  }


  /**
   * Untuk mengambil data pengguna berdasarkan username nya
   *
   * @param  mixed $username
   * @return void
   */
  public function checkLogin($username)
  {
    $count = $this->table($this->table)->where('username', $username)->countAllResults();

    if ($count == 0) throw new \Exception("No account found for this email address", 404);

    $result = $this->table($this->table)->where('username', $username)->limit(1)->get()->getRow();

    return $result;
  }


  /**
   * getUser
   *
   * @param  mixed $id
   * @return void
   */
  public function getUser($id)
  {
    if ($id == null) {
      $query = $this->db->query("SELECT id, email, name, username FROM {$this->table};");
      return $query->getResult();
    }
    $query = $this->db->query("SELECT id, email, name, username FROM {$this->table} where id = {$id};");
    return $query->getRow();
  }

  /**
   * updateUsers
   * Untuk mengupdate data user, true jika berhasil dan false jika gagal.
   * Gagal bisa saja karena ada duplikat username
   *
   * @param  mixed $id
   * @param  mixed $data
   * @return void
   */
  public function updateUsers($id, $data)
  {
    $query =  $this->db->table($this->table)->update($data, ['id' => $id]);
    $integrityError = !$query || $this->db->error()["code"] == 1062;
    if ($integrityError) throw new \Exception("Your username has already taken", 400);
    return;
  }
}
