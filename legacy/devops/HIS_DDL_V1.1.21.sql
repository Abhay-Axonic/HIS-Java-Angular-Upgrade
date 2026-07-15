select * from m_organization_master

--commit
	alter table pharmacy.t_prescription_detail  add column is_manually_added_item boolean NULL DEFAULT false;
--rollback
--	alter table pharmacy.t_prescription_detail  drop column is_manually_added_item;


--commit 
INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(477, 6, 50, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1); (edited) 
10:00
--commit

INSERT INTO public.m_screen_status_mapper
(id, status_id, screen_id, is_active, updated_by, updated_date, added_by, added_date, is_deleted, order_by)
VALUES(478, 6, 51, true, 1, '2018-05-31 17:08:46.000', 1, '2018-05-31 17:08:46.000', false, 1); (edited) 

--commit
alter table lab.t_serology_result_test_mppr add column is_abnormal character default 'N';
--rollback
--ALTER TABLE lab.t_serology_result_test_mppr DROP COLUMN is_abnormal;
--commit
ALTER TABLE pharmacy.t_sale_detail ALTER COLUMN batch_id SET NOT NULL;

--rollback
--ALTER TABLE pharmacy.t_sale_detail ALTER COLUMN batch_id DROP NOT NULL;
--commit
alter table lab.t_lab_test_result add column status bpchar(1) default 'A';
--rollback
--alter table lab.t_lab_test_result DROP COLUMN status;
