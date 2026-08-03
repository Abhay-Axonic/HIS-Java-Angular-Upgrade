-- Additive schema-drift fix: public.t_service_identity
-- BR-155 (reliability): the ServiceIdentity entity + DAO (ScheduledLogsScheduler heartbeat,
-- ServiceIdentityDaoImpl.saveServiceId / checkServiceIdAvailable) reference this table, which
-- was missing from the dev DB. The missing table caused a heartbeat query to throw inside
-- scheduler transactions, contaminating concurrent app transactions and producing generic
-- 500s ("Something went wrong. Reference: ERR-...") on unrelated endpoints.
--
-- Mirrors the entity exactly (ServiceIdentity.java):
--   @Table(name = "t_service_identity", schema = "public")
--   serviceIdentityId        -> service_identity_id        int PK (seq service_identity_seq)
--   serviceId                -> service_id                 varchar
--   lastHeartBeatDateTime    -> last_heart_beat_date_time  timestamp (Long millis via converter)
--   primaryHost              -> primary_host               boolean
--
-- Expand-Migrate-Contract: purely additive (CREATE IF NOT EXISTS); nothing dropped/renamed.
-- Applied to dev DB 2026-08-03. Live-verified: heartbeat SELECT now succeeds (14:38:14 log).
--
-- NOTE (verification finding): the frozen-section accept 500s seen earlier were NOT caused by
-- this drift — they were the detached-update-on-nonexistent-row behavior (requestId 99999).
-- A legit row accepts cleanly with the intended "Request Aknowledged Sucessfully." message.

CREATE SEQUENCE IF NOT EXISTS public.service_identity_seq START 1 INCREMENT 1;
CREATE TABLE IF NOT EXISTS public.t_service_identity (
    service_identity_id        integer NOT NULL DEFAULT nextval('public.service_identity_seq'),
    service_id                 varchar(255),
    last_heart_beat_date_time  timestamp,
    primary_host               boolean,
    CONSTRAINT t_service_identity_pkey PRIMARY KEY (service_identity_id)
);
