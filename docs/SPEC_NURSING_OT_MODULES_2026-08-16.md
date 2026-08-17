# SPEC — NURSING MODULE (21) + OT MODULE (13), batched

SERVED-BRANCH: `fix/patient-type-details-status` @ 0f4a2101. Core scope:
worklists read-only; the clinical entry forms carry save; the discharge
ladder screens are status-display in the first build. SHARED screens (already
rebuilt — the route ALIASES the existing feature, NOT rebuilt): the drug
administration (Pharmacy's drugAdministrator), the nursing allocation +
shift dashboards, the nursing worklist (encounterQueueManagement), the I/O
charts, and the OT masters (operationTable/Type/theatreType). DATE
CONVENTION: String dates via formatLegacyNow (the registration/prescription
convention, NOT the encounter's Long). Contract: the corrected ADT discharge
family (/adt/getDischagePatientList, getPatientReadyForBillingList,
getERDischargePatientList — the ER module's verified endpoints), the
NursingOrderMapperDto family (/api/opd), and the OTIMS
(/api/otims/dashboard — getSurgeriesOfTheDayList,
getProcedureSchedulingDetails, saveProcedureSchedulingDetails).

## NURSING (21)

1. Nursing Allocation Dashboard — SHARED (nursing-allocation-dashboard rebuilt).
2. Nursing Shift Dashboard — SHARED (nursing-shift-dashboard rebuilt).
3. Nursing Worklist — SHARED (encounterQueueManagement rebuilt — the route
   aliases the existing feature).
4. Patient Acceptance — build: the admission-acceptance worklist
   (/adt — the ADT admission family). Core: the read-only grid.
5. Allergies — build: the patient allergies entry (the clinical form).
   Core: the worklist + the allergy entry (save).
6. Map of Ward Bed — build: the bed-management grid (/adt — the bed map
   family). Core: the read-only bed map.
7. Drug Administration — SHARED (Pharmacy's drug-administration rebuilt).
8. Intake Fluid Output Charts — SHARED (input-output-charts rebuilt).
9. OT Transfer/Move — build: the transfer-to-OT request worklist
   (/adt — the admission transfer family). Core: the read-only grid.
10. Labor Room Transfer Request Acceptance — build: the labor-room transfer
    acceptance. Core: the read-only grid.
11. Marked for Hospital Discharge — build: the discharge worklist
    (/adt/getDischagePatientList — the verified endpoint). Core: status-display.
12. Discharge Medication Reconciliation — build: the reconciliation worklist
    (the discharge-medication family). Core: the read-only grid.
13. Ready for Billing — build: /adt/getPatientReadyForBillingList.
    Core: status-display.
14. Final Hospital Discharge — build: the final-discharge list
    (/adt — the discharge family). Core: status-display.
15. Request Short Leave — build: the short-leave request worklist.
    Core: the read-only grid.
16. Service Rendering — build: the service-rendering worklist
    (/api/opd — the order-driven family). Core: the read-only grid.
17. Periodicity Service — build: the periodicity-service worklist.
    Core: the read-only grid.
18. Administration Worklist — build: the administration worklist
    (/api/opd/pendingOrders family — the verified pending-orders
    endpoints). Core: the read-only grid.
19. Patient Discharged List — build (from the legacy's ADT states):
    /adt — the discharged-patients list. Core: status-display.
20. Discharge Medication Handover — build: the handover worklist.
    Core: the read-only grid.
21. ER Discharge Medication Handover — build: the ER handover variant.
    Core: the read-only grid.

## OT (13)

22. OT Reservation — build: NewOTreservation — the reservation worklist +
    the save (the OTIMS reservation family). Core: the worklist + the
    reservation form (save).
23. OT Manager Dashboard — build: /api/otims/dashboard —
    getSurgeriesOfTheDayList. Core: the read-only surgeries-of-the-day grid.
24. Surgical Calendar — build: the surgical calendar grid (the
    SurgicalCalendarMasterController family). Core: the read-only calendar.
25. Operation Theatre Type — SHARED (operation-table-type rebuilt).
26. Operation Table — SHARED (operation-table rebuilt).
27. Operation Table Type — SHARED (operation-table-type rebuilt).
28. Pre-Operative Details — build: the pre-op worklist
    (PreOperativeDetailsController family). Core: the read-only grid.
29. Staff Scheduling — build: the staff-scheduling worklist
    (StaffSchedulingController family). Core: the read-only grid.
30. Equipment Scheduling — build: the equipment-scheduling worklist
    (EquipmentSchedulingController family). Core: the read-only grid.
31. OT Checklist — build: the checklist worklist (ChecklistMasterController
    family). Core: the read-only grid.
32. OT Notes — build: the notes worklist (NotesMasterController family).
    Core: the read-only grid.
33. Procedure Scheduling — build: /api/otims/dashboard —
    getProcedureSchedulingDetails + saveProcedureSchedulingDetails. Core:
    the worklist + the schedule form (save).
34. Surgical Calendar Master — build: the calendar-master worklist
    (SurgicalCalendarMasterController). Core: the read-only grid.

All 34: tsc clean, routes registered (the legacy state names; the SHARED
routes alias the existing features), ONE commit, verified with git show.
