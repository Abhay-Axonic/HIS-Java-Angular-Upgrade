/**
 * Field names/types read directly from
 * `DiscountCategoryTypeMasterDto.java`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/DiscountCategoryTypeMasterDto.java)
 * and `discountCategoryMasterController.js` -- not guessed.
 *
 * Legacy's own form object is `$scope.discountType` (ctrl:58-64):
 * `{ code:"", type:"", discountTypeId:"" }` -- every field starts as an
 * empty string, including the id (not `undefined`/`null`). The wire field
 * `type` is the screen's "Description" input (view:30-31, label literally
 * reads "Description", bound to `discountType.type`) -- there is no
 * separate `discountCategory`/`description` field anywhere on the wire in
 * either direction for THIS screen, despite the DTO also carrying an unused
 * `discountCategory` property (dto:18-19) that this controller never reads
 * or writes.
 */
export interface DiscountCategoryForm {
  discountTypeId?: number;
  code: string;
  type: string;
}

export function emptyDiscountCategoryForm(): DiscountCategoryForm {
  return { discountTypeId: undefined, code: '', type: '' };
}

/** Shape of one row in `getDiscountCategoryList`'s `listObject`
 * (DiscountTypeMasterDaoImpl.java:112-120, raw SQL aliased onto this DTO --
 * `discountTypeId`/`type`/`code`/`isActive`, a REAL boolean column
 * `is_active`, not an 'A'/'I' string like several sibling screens). */
export interface DiscountCategoryRow {
  discountTypeId: number;
  code: string;
  type: string;
  isActive: boolean;
}

/** Shape of `getDiscountCategoryById`'s `listObject[0]`
 * (DiscountTypeMasterDaoImpl.java:156-164) -- same 4 fields as the list row. */
export type DiscountCategoryDetail = DiscountCategoryRow;

/** POST body for `saveDiscountCategory` (ctrl:182-234, payload at
 * ctrl:188-197). No `discountTypeId` -- DB-generated
 * (`m_discount_category_id_seq`). */
export interface DiscountCategorySavePayload {
  code: string;
  type: string;
  addedBy: number;
  updatedBy: number;
  organizationId: number;
  unitId: number;
  addedDate: string;
  updatedDate: string;
}

/** POST body for `updateDiscountCategory` (ctrl:309-361, payload at
 * ctrl:314-323) -- no `addedBy`/`addedDate`, matching legacy exactly. */
export interface DiscountCategoryUpdatePayload {
  discountTypeId: number;
  code: string;
  type: string;
  updatedBy: number;
  organizationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for `updateDiscountCategoryStatus` (ctrl:269-306, payload at
 * ctrl:276-281) -- legacy sends exactly these 4 fields here (unlike some
 * sibling screens that replay the whole in-progress form object), so no
 * minimal-payload divergence to flag. */
export interface DiscountCategoryStatusPayload {
  discountTypeId: number;
  isActive: boolean;
  updatedBy: number;
  updatedDate: string;
}
