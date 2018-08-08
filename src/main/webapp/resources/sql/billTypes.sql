select distinct(BillType)
from bill
where `FROMINSTITUTION_ID` is not null;
