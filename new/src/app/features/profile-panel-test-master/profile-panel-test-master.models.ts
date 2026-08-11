export type PanelStatus = 'A' | 'I';

/**
 * Row shape returned by GET /LIS/unit/PanelList/{orgId}/{unitId}/{offset}/{size}
 * (legacy profilepanelTestMasterController.js:171-172 list URI1). Projection
 * of TPanelMasterDto via the GET_PAGINATED_TPANEL_TEST_MASTER_LIST query
 * (PanelTestDaoImpl.java:234-239).
 *
 * Dev-empty: the DAO answers status 'error' with the plain message
 * "Panel Test Records Not Found." when the dev DB has no rows
 * (PANEL_RECORDS_NOT_FOUND, ICommonConstants.java:842, returned at
 * PanelTestDaoImpl.java:241-243) -- the component surfaces that message
 * verbatim instead of masking it behind a generic banner.
 */
export interface ProfilePanelTestRow {
  panelId: number;
  panelCode: string;
  panelName: string;
  panelAlies: string;
  serviceId: number;
  status: PanelStatus;
}
