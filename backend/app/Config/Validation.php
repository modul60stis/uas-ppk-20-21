<?php

namespace Config;

class Validation
{
	//--------------------------------------------------------------------
	// Setup
	//--------------------------------------------------------------------

	/**
	 * Stores the classes that contain the
	 * rules that are available.
	 *
	 * @var array
	 */
	public $ruleSets = [
		\CodeIgniter\Validation\Rules::class,
		\CodeIgniter\Validation\FormatRules::class,
		\CodeIgniter\Validation\FileRules::class,
		\CodeIgniter\Validation\CreditCardRules::class,
	];

	/**
	 * Specifies the views that are used to display the
	 * errors.
	 *
	 * @var array
	 */
	public $templates = [
		'list'   => 'CodeIgniter\Validation\Views\list',
		'single' => 'CodeIgniter\Validation\Views\single',
	];

	//--------------------------------------------------------------------
	// Rules
	//--------------------------------------------------------------------
	public $register = [
		'email'        => 'required|valid_email',
		'name'         => 'required|max_length[20]',
		'username'     => 'required|alpha_numeric|min_length[5]|max_length[20]',
		'password'     => 'required',
		'pass_confirm' => 'required|matches[password]',
	];

	public $login = [
		'username'     => 'required|alpha_numeric|min_length[5]|max_length[20]',
		'password'     => 'required'
	];

	public $update = [
		'email'        => 'required|valid_email',
		'name'         => 'required|max_length[20]',
		'username'     => 'required|alpha_numeric|min_length[5]|max_length[20]',
	];
}
