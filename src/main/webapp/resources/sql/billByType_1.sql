select id, `BILLTIME`, `BILLTYPE` , `BILLCLASSTYPE` , `TOTAL`, `NETTOTAL`,`PAYMENTMETHOD` from bill where `BILLTYPE` = 'PharmacySale' and `BILLCLASSTYPE`= 'RefundBill' order by id desc limit 50;
