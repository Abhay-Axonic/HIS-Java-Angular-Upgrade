-- BR-135: local dev DB was missing one whole table and several columns
-- referenced by the UserRoleMapper/UserRoleMaster fix's live verification --
-- same class as BR-076/101/103/104/128/131/132/134, not code bugs.
--
-- Found while live-verifying the UserRoleMapperDaoImpl/UserRoleMasterDaoImpl
-- cross-tenant IDOR fix (see fix/userrolemapper-userrolemaster-crosstenant-idor).
--
-- 1. Whole table missing: abdm.m_vaccination_role_mapper's sibling,
--    public.m_vaccination_role_mapper (VaccinationRoleMapper.java) --
--    UserRoleMasterServiceImpl.updateUserRoleMaster always queries this via
--    VaccinationRoleMapperDaoImp.getVaccinationRoleMapperByRoleId before
--    reaching the DAO's own findById, so it blocked testing entirely.
--
-- 2. t_user_role_mapper (UserRoleMapper.java) missing 3 columns.
-- 3. m_role_master (UserRoleMaster.java) missing 1 column.
-- 4. t_user_unit_mapper (UserUnitMapper.java) missing 4 columns --
--    UserRoleMapperDaoImpl.updateUserUnitMapper's raw SQL UPDATE touches
--    all of them unconditionally, so any one being absent blocked testing.
--
-- Additive only, per Expand-Migrate-Contract. All nullable/defaulted, no
-- existing row affected.

CREATE TABLE IF NOT EXISTS public.m_vaccination_role_mapper (
  vaccination_role_mapper_id serial4 NOT NULL,
  vaccination_id integer NULL,
  organization_id integer NULL,
  unit_id integer NULL,
  created_by integer NULL,
  created_date timestamp NULL,
  updated_by integer NULL,
  updated_date timestamp NULL,
  status character(1) NULL,
  role_id integer NOT NULL,
  CONSTRAINT m_vaccination_role_mapper_pkey PRIMARY KEY (vaccination_role_mapper_id),
  CONSTRAINT m_vaccination_role_mapper_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.m_role_master(role_id)
);

ALTER TABLE public.t_user_role_mapper
  ADD COLUMN IF NOT EXISTS is_ris_recall boolean;
ALTER TABLE public.t_user_role_mapper
  ADD COLUMN IF NOT EXISTS manual_mpesa boolean;
ALTER TABLE public.t_user_role_mapper
  ADD COLUMN IF NOT EXISTS enable_block_print boolean;

ALTER TABLE public.m_role_master
  ADD COLUMN IF NOT EXISTS prophylactic_vaccination_application character(1);

ALTER TABLE public.t_user_unit_mapper
  ADD COLUMN IF NOT EXISTS pharmacy_bill_cancellation boolean NOT NULL DEFAULT false;
ALTER TABLE public.t_user_unit_mapper
  ADD COLUMN IF NOT EXISTS pharmacy_receipt_cancellation boolean NOT NULL DEFAULT false;
ALTER TABLE public.t_user_unit_mapper
  ADD COLUMN IF NOT EXISTS sample_recollection boolean NOT NULL DEFAULT false;
ALTER TABLE public.t_user_unit_mapper
  ADD COLUMN IF NOT EXISTS ip_rendered_date_change boolean NOT NULL DEFAULT false;
