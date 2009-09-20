SELECT
	`ec`.`card1`,
	`ec`.`card2`,
	`ec`.`suited`,
	`r`.`players`,
	ROUND(SUM(`r`.`wins`) / SUM(`r`.`rounds`), 9) AS `probab`

FROM `preflop_ec` AS `ec`
INNER JOIN `preflop_ec_runs` AS `r` ON `r`.`ec` = `ec`.`id`

GROUP BY `r`.`ec`, `r`.`players`;
