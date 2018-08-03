SET FOREIGN_KEY_CHECKS=0;
DELETE FROM billfee where `RETIRED`=true;
DELETE FROM billfeepayment where `RETIRED`=true;
DELETE FROM bill  where `RETIRED`=true;
DELETE FROM billitem  where `RETIRED`=true;
DELETE FROM bill  where `RETIRED`=true;
DELETE FROM billnumber  where `RETIRED`=true;
DELETE FROM billsession  where `RETIRED`=true;
DELETE FROM billcomponent  where `RETIRED`=true;


DELETE FROM patient where `RETIRED`=true;
DELETE FROM patientencounter where `RETIRED`=true;
DELETE FROM patientflag where `RETIRED`=true;
DELETE FROM patientinvestigation where `RETIRED`=true;
DELETE FROM patientitem where `RETIRED`=true;
DELETE FROM patientreport where `RETIRED`=true;
DELETE FROM patientroom where `RETIRED`=true;
DELETE FROM payment where `RETIRED`=true;
Delete from person where `RETIRED`=true;

SET FOREIGN_KEY_CHECKS=1;