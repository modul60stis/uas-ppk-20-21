<?php

namespace App\Database\Migrations;

use CodeIgniter\Database\Migration;

class Users extends Migration
{
	public function up()
	{
		$this->forge->addField([
			'id'  => [
				'type'           => 'int',
				'constraint'     => 5,
				'unsigned'       => true,
				'auto_increment' => true
			],
			'username' => [
				'type'           => 'varchar',
				'constraint'     => 255,
			],
			'name' => [
				'type'           => 'varchar',
				'constraint'     => 255
			],
			'password' => [
				'type'           => 'varchar',
				'constraint'     => 255,
			],
			'email' => [
				'type'           => 'varchar',
				'constraint'     => 255,
			],
		]);

		$this->forge->addKey('id', TRUE);
		$this->forge->addUniqueKey('username');
		$this->forge->createTable('users', TRUE);
	}

	//--------------------------------------------------------------------

	public function down()
	{
		$this->forge->dropTable('users', true);
	}
}
