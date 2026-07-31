-- BR-138: local dev DB was missing public.t_ai_recording_transcription_credentials
-- entirely -- same class as BR-076/101/103/104/128/131/132/134/135, not a code bug.
--
-- Found while live-verifying the OTIMS getAiTranscriptionCreds cross-tenant IDOR
-- fix (see fix/otims-aitranscription-equipmentlookup-crosstenant-idor). Column
-- list confirmed against PreOperativeDetailsDaoImpl.getAiTranscriptionCreds's own
-- raw SQL query and the (commented-out) reference insert in
-- legacy/Database/Seed_Data_Customerwise.sql.
--
-- Additive only, per Expand-Migrate-Contract.

CREATE TABLE IF NOT EXISTS public.t_ai_recording_transcription_credentials (
  ai_voice_transcription_id serial4 NOT NULL,
  transcription_url character varying(500) NULL,
  api_key character varying(500) NULL,
  user_prompt text NULL,
  status character(1) NULL,
  organization_id integer NULL,
  unit_id integer NULL,
  created_by integer NULL,
  created_date timestamp NULL,
  updated_by integer NULL,
  updated_date timestamp NULL,
  CONSTRAINT t_ai_recording_transcription_credentials_pkey PRIMARY KEY (ai_voice_transcription_id)
);
