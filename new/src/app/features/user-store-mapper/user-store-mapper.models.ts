/**
 * Field names taken directly from `UserStoreMapperDto`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/UserStoreMapperDto.java)
 * and the controller's own field usage -- not guessed.
 */
export interface StoreOption {
  storeId: number;
  storeName: string;
}

export interface UserOption {
  userId: number;
  empCode: string;
  employeeName: string;
}

/** One row of the bottom `commonList` table. */
export interface UserStoreMapperRow {
  userId: number;
  employeeName: string;
  isActive: boolean;
}

/** One row of `getUserStoreMapperById`'s response -- the user's currently-mapped stores. */
export interface UserStoreMapperByIdRow {
  userId: number;
  storeId: number;
  storeName: string;
}

export interface UserStoreMapperSavePayload {
  userId: number;
  organizationId: number;
  unitId: number;
  isActive: boolean;
  isDeleted: boolean;
  addedBy: number;
  addedDate: string;
  updatedBy: number;
  updatedDate: string;
  selectUserStoreMapperList: number[];
}
