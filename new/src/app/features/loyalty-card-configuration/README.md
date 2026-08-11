# Loyalty Card Configuration — Unit Master 531 master/unit batch 4 (DQ-2026-08-12-013)

Route `loyaltyCardConfiguration`, module 743.

Lists the loyalty card configuration (min start amount, threshold, min
recharge). POST /api/unit/getLoyaltyCardConfigurationList with the legacy
payload {organisationId, unitId, offset, noOfRecordsPerPage} — NOTE the
British spelling organisationId (verified in the legacy controller). Confirmed
live 2026-08-12: SUCCESS with 1 real row (cardConfigurationId 1, 1000/5000/
1000).
