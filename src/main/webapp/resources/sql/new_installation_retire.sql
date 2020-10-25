update bill
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update billitem
set
 `RETIRED` = true
`RETIRECOMMENTS` = "new installation";
update patient;
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update patientencounter
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update patientinvestigation
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update patientitem
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update patientsample
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";
update patientreport
set
 `RETIRED` = true,
`RETIRECOMMENTS` = "new installation";


