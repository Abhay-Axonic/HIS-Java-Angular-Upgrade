/**
 * Phlebotomist home collection (module 155, LIS) model shapes.
 *
 * LIVE-VERIFIED 2026-08-13 (DQ-061): POST /LIS/PhlebotomistHomeCollection/save
 * (@RequestBody List<PhlebotomistHomeCollectionDto>) -> HTTP 200 "Added
 * Successfully." + lab.t_phlebotomist_home_collection row landed (id-based
 * verified, teardown clean) — BUILDABLE.
 *
 * CONTRACT: fromTime/toTime REQUIRED as 'HH:mm:ss' (the DAO's
 * LocalTime.parse NPEs on null — PhlebotomistHomeCollectionDaoImpl:45);
 * createdDate/updatedDate REQUIRED ('dd-MM-yyyy HH:mm:ss' — the DAO's
 * getDate NPEs on null at :59); the write targets its OWN table
 * (t_phlebotomist_home_collection), NOT the lab sample tables.
 */
