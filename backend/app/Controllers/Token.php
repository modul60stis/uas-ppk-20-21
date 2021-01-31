<?php

namespace App\Controllers;

use \Firebase\JWT\JWT;

class Token
{
  
  /**
   * secret key
   *
   * @return void
   */
  private static function getSecretKey()
  {
    return 'inirahasiabuatapikeynyajangandirubahataupundihapus';
  }

  /**
   * --------------------------------------------------------------------
   * Token maker
   * --------------------------------------------------------------------
   * Semua token di generate dari fungsi ini. Expired time nya adalah 1 jam
   */
  public static function encode($id, $username)
  {
    $issuer_claim = "THE_CLAIM";
    $audience_claim = "THE_AUDIENCE";
    $issuedat_claim = Time();
    $notbefore_claim = $issuedat_claim;
    $expire_claim = $issuedat_claim + HOUR;
    $token = [
      "iss" => $issuer_claim,
      "aud" => $audience_claim,
      "iat" => $issuedat_claim,
      "nbf" => $notbefore_claim,
      "exp" => $expire_claim,
      "data" => [
        "id" => $id,
        "username" => $username,
        "time" => $issuedat_claim
      ]
    ];

    $key = Token::getSecretKey();
    return JWT::encode($token, $key);
  }


  /**
   * --------------------------------------------------------------------
   * Token checker
   * --------------------------------------------------------------------
   *
   * token yang di authorization header 
   * akan dicek apakah masih valid apa nggak
   * pertama di decode dulu tokennya
   * ya lanjut kalo gak ya tolak
   */
  public static function checkHeader($authHeader = NULL)
  {
    try {

      if ($authHeader == NULL) throw new \Exception("Missing Authorization Header", 401);

      $arr = explode(" ", $authHeader);

      $token = $arr[1];

      if (!$token) throw new \Exception("Check your token", 401);

      $secret_key = Token::getSecretKey();


      $decoded = JWT::decode($token, $secret_key, array('HS256'));

      return $decoded->data;
    } catch (\Throwable $th) {
      throw new \Exception($th->getMessage(), 401);
    }
  }
}
