SELECT
	`ec`.`card1`,
	`ec`.`card2`,
	`ec`.`suited`,
	SUM(`r`.`rounds`) AS `rounds`

FROM `preflop_ec` AS `ec`
INNER JOIN `preflop_ec_runs` AS `r` ON `r`.`ec` = `ec`.`id`

GROUP BY `ec`.`id`;
