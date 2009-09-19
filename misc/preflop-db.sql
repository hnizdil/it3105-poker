CREATE TABLE `preflop_ec` (
	`id` INT UNSIGNED NOT NULL,
	`card1` ENUM("TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"),
	`card2` ENUM("TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"),
	`suited` BOOL DEFAULT FALSE
) CHARSET "ascii" COLLATE "ascii_general_ci";

CREATE TABLE `preflop_ec_runs` (
	`ec` INT UNSIGNED NOT NULL,
	`players` TINYINT UNSIGNED NOT NULL,
	`rounds` INT UNSIGNED NOT NULL,
	`wins` INT UNSIGNED NOT NULL,
	`ties` INT UNSIGNED NOT NULL,
	`losses` INT UNSIGNED NOT NULL
) CHARSET "ascii" COLLATE "ascii_general_ci";
